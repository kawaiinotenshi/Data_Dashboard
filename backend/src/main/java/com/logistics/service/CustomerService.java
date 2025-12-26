package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Customer;

import java.util.List;

public interface CustomerService extends IService<Customer> {
    List<Customer> getAllCustomers();
}
