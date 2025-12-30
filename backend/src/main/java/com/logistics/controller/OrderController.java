package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.OrderService;
import com.logistics.vo.OrderVO;
import com.logistics.vo.OrderRequestVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/list")
    public Result<List<OrderVO>> getAllOrders() {
        List<OrderVO> list = orderService.getAllOrderVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<OrderVO> getOrderById(@PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderById(id);
        return Result.success(orderVO);
    }

    @GetMapping("/status/{status}")
    public Result<List<OrderVO>> getOrdersByStatus(@PathVariable String status) {
        List<OrderVO> list = orderService.getOrdersByStatus(status);
        return Result.success(list);
    }

    @PostMapping
    public Result<Void> createOrder(@RequestBody OrderRequestVO orderRequestVO) {
        orderService.createOrder(orderRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateOrder(@PathVariable Long id, @RequestBody OrderRequestVO orderRequestVO) {
        orderService.updateOrder(id, orderRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        boolean success = orderService.deleteOrder(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除订单失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteOrders(@RequestBody List<Long> ids) {
        boolean success = orderService.batchDeleteOrders(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除订单失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> statistics = orderService.getOrderStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/status-group")
    public Result<List<Map<String, Object>>> getOrdersByStatusGroup() {
        List<Map<String, Object>> list = orderService.getOrdersByStatusGroup();
        return Result.success(list);
    }

    @GetMapping("/date-range")
    public Result<List<Map<String, Object>>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<Map<String, Object>> list = orderService.getOrdersByDateRange(startDate, endDate);
        return Result.success(list);
    }

    @GetMapping("/customer/{customerName}")
    public Result<List<OrderVO>> getOrdersByCustomerName(@PathVariable String customerName) {
        List<OrderVO> list = orderService.getOrdersByCustomerName(customerName);
        return Result.success(list);
    }
}
