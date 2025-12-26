package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.Customer;
import com.logistics.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public Result<List<Customer>> getAllCustomers() {
        List<Customer> list = customerService.getAllCustomers();
        return Result.success(list);
    }
}
