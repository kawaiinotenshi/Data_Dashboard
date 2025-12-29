package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.CustomerService;
import com.logistics.vo.CustomerVO;
import com.logistics.vo.CustomerRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin
@Api(tags = "客户管理")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    @ApiOperation("获取所有客户")
    public Result<List<CustomerVO>> getAllCustomers() {
        List<CustomerVO> list = customerService.getAllCustomerVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取客户")
    public Result<CustomerVO> getCustomerById(@ApiParam("客户ID") @PathVariable Long id) {
        CustomerVO customerVO = customerService.getCustomerById(id);
        return Result.success(customerVO);
    }

    @PostMapping
    @ApiOperation("创建客户")
    public Result<Void> createCustomer(@ApiParam("客户信息") @RequestBody CustomerRequestVO customerRequestVO) {
        customerService.createCustomer(customerRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新客户")
    public Result<Void> updateCustomer(@ApiParam("客户ID") @PathVariable Long id, @ApiParam("客户信息") @RequestBody CustomerRequestVO customerRequestVO) {
        customerService.updateCustomer(id, customerRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除客户")
    public Result<Void> deleteCustomer(@ApiParam("客户ID") @PathVariable Long id) {
        boolean success = customerService.deleteCustomer(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除客户失败");
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除客户")
    public Result<Void> batchDeleteCustomers(@ApiParam("客户ID列表") @RequestBody List<Long> ids) {
        boolean success = customerService.batchDeleteCustomers(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除客户失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取客户统计信息")
    public Result<Map<String, Object>> getCustomerStatistics() {
        Map<String, Object> statistics = customerService.getCustomerStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/name/{name}")
    @ApiOperation("根据名称搜索客户")
    public Result<List<CustomerVO>> getCustomersByName(@ApiParam("客户名称") @PathVariable String name) {
        List<CustomerVO> list = customerService.getCustomersByName(name);
        return Result.success(list);
    }

    @GetMapping("/address/{address}")
    @ApiOperation("根据地址搜索客户")
    public Result<List<CustomerVO>> getCustomersByAddress(@ApiParam("客户地址") @PathVariable String address) {
        List<CustomerVO> list = customerService.getCustomersByAddress(address);
        return Result.success(list);
    }

    @GetMapping("/order-count")
    @ApiOperation("获取客户订单数量")
    public Result<List<Map<String, Object>>> getCustomerOrderCount() {
        List<Map<String, Object>> list = customerService.getCustomerOrderCount();
        return Result.success(list);
    }
}
