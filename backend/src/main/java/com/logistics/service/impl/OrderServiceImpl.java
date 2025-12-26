package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Order;
import com.logistics.mapper.OrderMapper;
import com.logistics.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Override
    public List<Order> getAllOrders() {
        return list();
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return list(wrapper);
    }
}
