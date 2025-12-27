package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Override
    public List<Order> getAllOrders() {
        return list();
    }

    @Override
    public List<OrderVO> getAllOrderVOs() {
        List<Order> orders = list();
        return orders.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            return orderVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<OrderVO> getOrdersByStatus(String status) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        List<Order> orders = list(wrapper);
        return orders.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            return orderVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public OrderVO getOrderById(Long id) {
        Order order = getById(id);
        if (order == null) {
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(OrderRequestVO orderRequestVO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderRequestVO, order);
        return save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(Long id, OrderRequestVO orderRequestVO) {
        Order order = getById(id);
        if (order == null) {
            throw new ResourceNotFoundException("订单", id);
        }
        BeanUtils.copyProperties(orderRequestVO, order);
        return updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteOrders(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateOrders(List<OrderRequestVO> orderRequestVOs) {
        if (orderRequestVOs == null || orderRequestVOs.isEmpty()) {
            return false;
        }
        List<Order> orders = orderRequestVOs.stream().map(vo -> {
            Order order = new Order();
            BeanUtils.copyProperties(vo, order);
            return order;
        }).collect(java.util.stream.Collectors.toList());
        return saveBatch(orders, 500);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return false;
        }
        return updateBatchById(orders, 500);
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
        return orders.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            return orderVO;
        }).collect(java.util.stream.Collectors.toList());
    }
}
