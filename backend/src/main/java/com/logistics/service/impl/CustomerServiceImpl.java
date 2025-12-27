package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Customer;
import com.logistics.mapper.CustomerMapper;
import com.logistics.service.CustomerService;
import com.logistics.vo.CustomerRequestVO;
import com.logistics.vo.CustomerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Override
    public List<Customer> getAllCustomers() {
        return list();
    }

    @Override
    public List<CustomerVO> getAllCustomerVOs() {
        List<Customer> customers = list();
        return customers.stream().map(customer -> {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            return customerVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public CustomerVO getCustomerById(Long id) {
        Customer customer = getById(id);
        if (customer == null) {
            return null;
        }
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customer, customerVO);
        return customerVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomer(CustomerRequestVO customerRequestVO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerRequestVO, customer);
        return save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomer(Long id, CustomerRequestVO customerRequestVO) {
        Customer customer = getById(id);
        if (customer == null) {
            throw new ResourceNotFoundException("客户", id);
        }
        BeanUtils.copyProperties(customerRequestVO, customer);
        return updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomer(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCustomers(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateCustomers(List<CustomerRequestVO> customerRequestVOs) {
        if (customerRequestVOs == null || customerRequestVOs.isEmpty()) {
            return false;
        }
        List<Customer> customers = customerRequestVOs.stream().map(vo -> {
            Customer customer = new Customer();
            BeanUtils.copyProperties(vo, customer);
            return customer;
        }).collect(java.util.stream.Collectors.toList());
        return saveBatch(customers, 500);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCustomers(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            return false;
        }
        return updateBatchById(customers, 500);
    }

    @Override
    public Map<String, Object> getCustomerStatistics() {
        return baseMapper.selectCustomerStatistics();
    }

    @Override
    public List<CustomerVO> getCustomersByName(String name) {
        List<Customer> customers = baseMapper.selectCustomersByName(name);
        return customers.stream().map(customer -> {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            return customerVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<CustomerVO> getCustomersByAddress(String address) {
        List<Customer> customers = baseMapper.selectCustomersByAddress(address);
        return customers.stream().map(customer -> {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            return customerVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCustomerOrderCount() {
        return baseMapper.selectCustomerOrderCount();
    }
}
