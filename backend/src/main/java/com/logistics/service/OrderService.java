package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Order;

import java.util.List;

public interface OrderService extends IService<Order> {
    List<Order> getAllOrders();
    List<Order> getOrdersByStatus(String status);
}
