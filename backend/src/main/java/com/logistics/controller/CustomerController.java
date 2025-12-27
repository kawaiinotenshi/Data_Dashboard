package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.CustomerService;
import com.logistics.vo.CustomerVO;
import com.logistics.vo.CustomerRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public Result<List<CustomerVO>> getAllCustomers() {
        List<CustomerVO> list = customerService.getAllCustomerVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<CustomerVO> getCustomerById(@PathVariable Long id) {
        CustomerVO customerVO = customerService.getCustomerById(id);
        return Result.success(customerVO);
    }

    @PostMapping
    public Result<Void> createCustomer(@RequestBody CustomerRequestVO customerRequestVO) {
        customerService.createCustomer(customerRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestVO customerRequestVO) {
        customerService.updateCustomer(id, customerRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        boolean success = customerService.deleteCustomer(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除客户失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteCustomers(@RequestBody List<Long> ids) {
        boolean success = customerService.batchDeleteCustomers(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除客户失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getCustomerStatistics() {
        Map<String, Object> statistics = customerService.getCustomerStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/name/{name}")
    public Result<List<CustomerVO>> getCustomersByName(@PathVariable String name) {
        List<CustomerVO> list = customerService.getCustomersByName(name);
        return Result.success(list);
    }

    @GetMapping("/address/{address}")
    public Result<List<CustomerVO>> getCustomersByAddress(@PathVariable String address) {
        List<CustomerVO> list = customerService.getCustomersByAddress(address);
        return Result.success(list);
    }

    @GetMapping("/order-count")
    public Result<List<Map<String, Object>>> getCustomerOrderCount() {
        List<Map<String, Object>> list = customerService.getCustomerOrderCount();
        return Result.success(list);
    }
}
