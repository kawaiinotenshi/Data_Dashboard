package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.OrderService;
import com.logistics.vo.OrderVO;
import com.logistics.vo.OrderRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin
@Api(tags = "订单管理")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/list")
    @ApiOperation("获取所有订单")
    public Result<List<OrderVO>> getAllOrders() {
        List<OrderVO> list = orderService.getAllOrderVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取订单")
    public Result<OrderVO> getOrderById(@ApiParam("订单ID") @PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderById(id);
        return Result.success(orderVO);
    }

    @GetMapping("/status/{status}")
    @ApiOperation("根据状态获取订单")
    public Result<List<OrderVO>> getOrdersByStatus(@ApiParam("订单状态") @PathVariable String status) {
        List<OrderVO> list = orderService.getOrdersByStatus(status);
        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("创建订单")
    public Result<Void> createOrder(@RequestBody OrderRequestVO orderRequestVO) {
        orderService.createOrder(orderRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新订单")
    public Result<Void> updateOrder(@ApiParam("订单ID") @PathVariable Long id, @RequestBody OrderRequestVO orderRequestVO) {
        orderService.updateOrder(id, orderRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除订单")
    public Result<Void> deleteOrder(@ApiParam("订单ID") @PathVariable Long id) {
        boolean success = orderService.deleteOrder(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除订单失败");
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除订单")
    public Result<Void> batchDeleteOrders(@RequestBody List<Long> ids) {
        boolean success = orderService.batchDeleteOrders(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除订单失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取订单统计")
    public Result<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> statistics = orderService.getOrderStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/status-group")
    @ApiOperation("按状态分组获取订单")
    public Result<List<Map<String, Object>>> getOrdersByStatusGroup() {
        List<Map<String, Object>> list = orderService.getOrdersByStatusGroup();
        return Result.success(list);
    }

    @GetMapping("/date-range")
    @ApiOperation("按日期范围获取订单")
    public Result<List<Map<String, Object>>> getOrdersByDateRange(
            @ApiParam("开始日期") @RequestParam String startDate,
            @ApiParam("结束日期") @RequestParam String endDate) {
        List<Map<String, Object>> list = orderService.getOrdersByDateRange(startDate, endDate);
        return Result.success(list);
    }

    @GetMapping("/customer/{customerName}")
    @ApiOperation("按客户名称获取订单")
    public Result<List<OrderVO>> getOrdersByCustomerName(@ApiParam("客户名称") @PathVariable String customerName) {
        List<OrderVO> list = orderService.getOrdersByCustomerName(customerName);
        return Result.success(list);
    }
}
