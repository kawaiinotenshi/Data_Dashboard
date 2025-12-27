package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Customer;
import com.logistics.vo.CustomerRequestVO;
import com.logistics.vo.CustomerVO;

import java.util.List;
import java.util.Map;

public interface CustomerService extends IService<Customer> {
    List<Customer> getAllCustomers();
    List<CustomerVO> getAllCustomerVOs();
    CustomerVO getCustomerById(Long id);
    boolean createCustomer(CustomerRequestVO customerRequestVO);
    boolean updateCustomer(Long id, CustomerRequestVO customerRequestVO);
    boolean deleteCustomer(Long id);
    boolean batchDeleteCustomers(List<Long> ids);
    
    boolean batchCreateCustomers(List<CustomerRequestVO> customerRequestVOs);
    boolean batchUpdateCustomers(List<Customer> customers);
    
    Map<String, Object> getCustomerStatistics();
    List<CustomerVO> getCustomersByName(String name);
    List<CustomerVO> getCustomersByAddress(String address);
    List<Map<String, Object>> getCustomerOrderCount();
}
