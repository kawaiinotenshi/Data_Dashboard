package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.Order;
import com.logistics.mapper.OrderMapper;
import com.logistics.service.OrderService;
import com.logistics.vo.OrderRequestVO;
import com.logistics.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends BaseEntityServiceImpl<OrderMapper, Order> implements OrderService {
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
        return createEntity(orderRequestVO, Order.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(Long id, OrderRequestVO orderRequestVO) {
        return updateEntity(id, orderRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteOrders(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateOrders(List<OrderRequestVO> orderRequestVOs) {
        return batchCreateEntities(orderRequestVOs, Order.class);
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
