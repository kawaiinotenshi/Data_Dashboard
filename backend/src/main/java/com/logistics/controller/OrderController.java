package com.logistics.controller;

import com.alibaba.excel.EasyExcel;
import com.logistics.common.Result;
import com.logistics.service.OrderService;
import com.logistics.util.EasyExcelListener;
import com.logistics.util.WebSocketUtils;
import com.logistics.vo.OrderRequestVO;
import com.logistics.vo.OrderVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    private final WebSocketUtils webSocketUtils;

    public OrderController(OrderService orderService, WebSocketUtils webSocketUtils) {
        this.orderService = orderService;
        this.webSocketUtils = webSocketUtils;
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
        // 广播订单更新
        webSocketUtils.broadcastOrderUpdate();
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateOrder(@PathVariable Long id, @RequestBody OrderRequestVO orderRequestVO) {
        orderService.updateOrder(id, orderRequestVO);
        // 广播订单更新
        webSocketUtils.broadcastOrderUpdate();
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        boolean success = orderService.deleteOrder(id);
        if (success) {
            // 广播订单更新
            webSocketUtils.broadcastOrderUpdate();
            return Result.success();
        } else {
            return Result.error("删除订单失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteOrders(@RequestBody List<Long> ids) {
        boolean success = orderService.batchDeleteOrders(ids);
        if (success) {
            // 广播订单更新
            webSocketUtils.broadcastOrderUpdate();
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

    /**
     * Excel导入订单数据
     */
    @PostMapping("/import")
    public Result<Void> importOrders(@RequestParam("file") MultipartFile file) {
        try {
            // 使用EasyExcel读取Excel文件
            EasyExcel.read(file.getInputStream(), OrderRequestVO.class, new EasyExcelListener(orderService))
                    .sheet()
                    .doRead();
            
            // 广播订单更新
            webSocketUtils.broadcastOrderUpdate();
            
            return Result.success();
        } catch (IOException e) {
            return Result.error("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            return Result.error("Excel导入失败: " + e.getMessage());
        }
    }
}
