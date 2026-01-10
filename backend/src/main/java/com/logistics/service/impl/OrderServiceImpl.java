package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.Order;
import com.logistics.entity.Transport;
import com.logistics.entity.Warehouse;
import com.logistics.mapper.OrderMapper;
import com.logistics.service.OrderService;
import com.logistics.service.TransportService;
import com.logistics.service.WarehouseService;
import com.logistics.service.BusinessQuarterlyService;
import com.logistics.vo.InventoryAlertVO;
import com.logistics.vo.OrderRequestVO;
import com.logistics.vo.OrderVO;
import com.logistics.vo.TransportRequestVO;
import com.logistics.util.WebSocketUtils;
import com.logistics.util.OrderNoGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends BaseEntityServiceImpl<OrderMapper, Order> implements OrderService {
    
    @Autowired
    private WarehouseService warehouseService;
    
    @Autowired
    private TransportService transportService;
    
    @Autowired
    private WebSocketUtils webSocketUtils;
    
    @Autowired
    private BusinessQuarterlyService businessQuarterlyService;
    
    @Autowired
    private OrderNoGenerator orderNoGenerator;
    
    @Override
    public String getEntityName() {
        return "订单";
    }

    @Override
    public Class<?> getVOClass() {
        return OrderVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return OrderRequestVO.class;
    }

    /**
     * 广播所有数据更新
     */
    private void broadcastAllUpdates() {
        webSocketUtils.broadcastAllUpdates();
    }

    @Override
    public List<Order> getAllOrders() {
        return getAllEntities();
    }

    @Override
    public List<OrderVO> getAllOrderVOs() {
        return getAllEntityVOs(this::convertToOrderVO);
    }

    @Override
    public List<OrderVO> getOrdersByStatus(String status) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        List<Order> orders = list(wrapper);
        return convertToVOList(orders, this::convertToOrderVO);
    }

    @Override
    public OrderVO getOrderById(Long id) {
        return getEntityById(id, this::convertToOrderVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(OrderRequestVO orderRequestVO) {
        // 创建订单
        Order order = new Order();
        BeanUtils.copyProperties(orderRequestVO, order);
        
        // 自动生成订单号
        String orderNo = orderNoGenerator.generateOrderNo();
        order.setOrderNo(orderNo);
        
        // 设置创建时间
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        // 保存订单
        boolean result = save(order);
        if (result) {
            // 1. 扣减仓库库存
            Warehouse warehouse = warehouseService.getWarehouseById(order.getWarehouseId());
            if (warehouse != null) {
                // 计算当前可用容量（通过capacity和throughput计算）
                BigDecimal currentAvailableCapacity = warehouse.getCapacity().subtract(warehouse.getThroughput());
                // 计算新的可用容量
                BigDecimal newAvailableCapacity = currentAvailableCapacity.subtract(order.getRequiredCapacity());
                
                // 检查容量是否足够
                if (newAvailableCapacity.compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("仓库容量不足");
                }
                
                // 更新仓库吞吐量（throughput表示已使用容量）
                BigDecimal newThroughput = warehouse.getThroughput().add(order.getRequiredCapacity());
                warehouse.setThroughput(newThroughput);
                // 更新利用率
                BigDecimal utilization = newThroughput.divide(warehouse.getCapacity(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                warehouse.setUtilizationRate(utilization);
                
                // 设置仓库状态
                int status = 0; // 0: 正常
                if (utilization.compareTo(BigDecimal.valueOf(90)) > 0) {
                    status = 2; // 2: 高库存警告
                } else if (utilization.compareTo(BigDecimal.valueOf(10)) < 0) {
                    status = 1; // 1: 低库存警告
                }
                warehouse.setStatus(status);
                
                warehouseService.updateById(warehouse);
                
                // 利用率 >90% 或 <10% 时触发预警
                if (status != 0) {
                    InventoryAlertVO alert = new InventoryAlertVO();
                    alert.setWarehouseId(warehouse.getId());
                    alert.setWarehouseName(warehouse.getName());
                    alert.setAlertType(status == 2 ? "burst" : "vacancy");
                    alert.setAlertMessage("仓库" + warehouse.getName() + "利用率已" + (status == 2 ? "超过90%" : "低于10%") + "，当前可用容量：" + newAvailableCapacity);
                    alert.setTimestamp(System.currentTimeMillis());
                    // 广播库存预警
                    webSocketUtils.broadcastInventoryAlert(alert);
                }
            }
            
            // 2. 生成物流任务
            TransportRequestVO transportRequest = new TransportRequestVO();
            transportRequest.setVehicleType("货车"); // 根据业务逻辑确定
            transportRequest.setVehicleCount(1);
            transportRequest.setTotalDistance(new BigDecimal(100)); // 实际应计算
            transportRequest.setStatus("待发货");
            transportRequest.setMonth(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            transportRequest.setOrderId(order.getId());
            
            // 调用物流服务创建任务
            transportService.createTransport(transportRequest);
            
            // 3. 更新业务季度统计表
            if (order.getOrderDate() != null && order.getOrderAmount() != null) {
                java.time.LocalDate orderDate = order.getOrderDate().toLocalDate();
                int year = orderDate.getYear();
                // 计算季度（1-4）
                int quarter = (orderDate.getMonthValue() - 1) / 3 + 1;
                
                // 默认业务类型为运输业务
                String businessType = "运输业务";
                businessQuarterlyService.updateQuarterlyAmount(year, quarter, businessType, order.getOrderAmount());
            }
            
            // 广播所有数据更新
            webSocketUtils.broadcastAllUpdates();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(Long id, OrderRequestVO orderRequestVO) {
        // 获取原订单信息
        Order oldOrder = getById(id);
        if (oldOrder == null) {
            return false;
        }
        
        // 更新订单
        Order newOrder = new Order();
        BeanUtils.copyProperties(orderRequestVO, newOrder);
        newOrder.setId(id);
        boolean result = updateById(newOrder);
        
        if (result) {
            // 检查订单状态是否变为processing
            if ("processing".equals(newOrder.getStatus()) && !"processing".equals(oldOrder.getStatus())) {
                // 自动生成运输任务
                TransportRequestVO transportRequest = new TransportRequestVO();
                transportRequest.setVehicleType("货车"); // 实际应用中应该根据订单类型选择
                transportRequest.setVehicleCount(1);
                transportRequest.setTotalDistance(new BigDecimal(100)); // 实际应用中应该计算
                transportRequest.setStatus("pending");
                transportRequest.setMonth(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
                
                // 创建运输任务
                boolean transportResult = transportService.createTransport(transportRequest);
            }
            
            // 广播所有数据更新
            webSocketUtils.broadcastAllUpdates();
        }
        
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long id) {
        // 获取订单信息
        Order order = getById(id);
        if (order != null) {
            // 回滚仓库容量
            Warehouse warehouse = warehouseService.getWarehouseById(order.getWarehouseId());
            if (warehouse != null) {
                // 更新仓库吞吐量（throughput表示已使用容量）
                BigDecimal newThroughput = warehouse.getThroughput().subtract(order.getRequiredCapacity());
                warehouse.setThroughput(newThroughput);
                
                // 计算可用容量
                BigDecimal newAvailableCapacity = warehouse.getCapacity().subtract(newThroughput);
                
                // 更新利用率
                BigDecimal utilization = newThroughput.divide(warehouse.getCapacity(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                warehouse.setUtilizationRate(utilization);
                
                // 设置仓库状态
                if (utilization.compareTo(BigDecimal.valueOf(90)) > 0) {
                    warehouse.setStatus(2); // 2: 高库存警告
                } else if (utilization.compareTo(BigDecimal.valueOf(10)) < 0) {
                    warehouse.setStatus(1); // 1: 低库存警告
                } else {
                    warehouse.setStatus(0); // 0: 正常
                }
                
                warehouseService.updateById(warehouse);
                
                // 检查回滚后的仓库利用率
                System.out.println("订单取消：仓库" + warehouse.getName() + "容量已回滚，当前可用容量：" + newAvailableCapacity + "，利用率：" + utilization + "%");
                
                // 发送库存预警（如果需要）
                if (utilization.compareTo(BigDecimal.valueOf(90)) > 0) {
                    InventoryAlertVO alert = new InventoryAlertVO();
                    alert.setWarehouseId(warehouse.getId());
                    alert.setWarehouseName(warehouse.getName());
                    alert.setAlertType("burst");
                    alert.setAlertMessage("仓库" + warehouse.getName() + "利用率已超过90%，当前可用容量：" + newAvailableCapacity);
                    alert.setTimestamp(System.currentTimeMillis());
                    webSocketUtils.broadcastInventoryAlert(alert);
                } else if (utilization.compareTo(BigDecimal.valueOf(10)) < 0) {
                    InventoryAlertVO alert = new InventoryAlertVO();
                    alert.setWarehouseId(warehouse.getId());
                    alert.setWarehouseName(warehouse.getName());
                    alert.setAlertType("vacancy");
                    alert.setAlertMessage("仓库" + warehouse.getName() + "利用率低于10%，当前可用容量：" + newAvailableCapacity);
                    alert.setTimestamp(System.currentTimeMillis());
                    webSocketUtils.broadcastInventoryAlert(alert);
                }
            }
        }
        
        // 删除订单
        boolean result = deleteEntity(id);
        if (result) {
            // 广播所有数据更新
            webSocketUtils.broadcastAllUpdates();
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteOrders(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateOrders(List<OrderRequestVO> orderRequestVOs) {
        boolean result = batchCreateEntities(orderRequestVOs, Order.class);
        if (result) {
            // 广播所有数据更新，确保随机生成订单时的连锁反应
            webSocketUtils.broadcastAllUpdates();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateOrders(List<Order> orders) {
        return batchUpdateEntities(orders);
    }

    @Override
    public Map<String, Object> getOrderStatistics() {
        return baseMapper.selectOrderStatistics();
    }

    @Override
    public List<Map<String, Object>> getOrdersByStatusGroup() {
        return baseMapper.selectOrdersByStatusGroup();
    }

    @Override
    public List<Map<String, Object>> getOrdersByDateRange(String startDate, String endDate) {
        return baseMapper.selectOrdersByDateRange(startDate, endDate);
    }

    @Override
    public List<OrderVO> getOrdersByCustomerName(String customerName) {
        List<Order> orders = baseMapper.selectOrdersByCustomerName(customerName);
        return convertToVOList(orders, this::convertToOrderVO);
    }

    private OrderVO convertToOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }
}
