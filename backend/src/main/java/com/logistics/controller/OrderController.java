package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.Order;
import com.logistics.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result<List<Order>> getAllOrders() {
        List<Order> list = orderService.getAllOrders();
        return Result.success(list);
    }

    @GetMapping("/status/{status}")
    public Result<List<Order>> getOrdersByStatus(@PathVariable String status) {
        List<Order> list = orderService.getOrdersByStatus(status);
        return Result.success(list);
    }
}
