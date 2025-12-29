package com.logistics.service.impl;

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
public class CustomerServiceImpl extends BaseEntityServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Override
    public String getEntityName() {
        return "客户";
    }

    @Override
    public Class<?> getVOClass() {
        return CustomerVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return CustomerRequestVO.class;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return getAllEntities();
    }

    @Override
    public List<CustomerVO> getAllCustomerVOs() {
        return getAllEntityVOs(this::convertToCustomerVO);
    }

    @Override
    public CustomerVO getCustomerById(Long id) {
        return getEntityById(id, this::convertToCustomerVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomer(CustomerRequestVO customerRequestVO) {
        return createEntity(customerRequestVO, Customer.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomer(Long id, CustomerRequestVO customerRequestVO) {
        return updateEntity(id, customerRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomer(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCustomers(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateCustomers(List<CustomerRequestVO> customerRequestVOs) {
        return batchCreateEntities(customerRequestVOs, Customer.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCustomers(List<Customer> customers) {
        return batchUpdateEntities(customers);
    }

    @Override
    public Map<String, Object> getCustomerStatistics() {
        return baseMapper.selectCustomerStatistics();
    }

    @Override
    public List<CustomerVO> getCustomersByName(String name) {
        List<Customer> customers = baseMapper.selectCustomersByName(name);
        return convertToVOList(customers, this::convertToCustomerVO);
    }

    @Override
    public List<CustomerVO> getCustomersByAddress(String address) {
        List<Customer> customers = baseMapper.selectCustomersByAddress(address);
        return convertToVOList(customers, this::convertToCustomerVO);
    }

    @Override
    public List<Map<String, Object>> getCustomerOrderCount() {
        return baseMapper.selectCustomerOrderCount();
    }

    private CustomerVO convertToCustomerVO(Customer customer) {
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customer, customerVO);
        return customerVO;
    }
}
