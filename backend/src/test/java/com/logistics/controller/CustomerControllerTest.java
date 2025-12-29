package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.CustomerService;
import com.logistics.vo.CustomerVO;
import com.logistics.vo.CustomerRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerVO customerVO;
    private CustomerRequestVO customerRequestVO;

    @BeforeEach
    void setUp() {
        customerVO = new CustomerVO();
        customerVO.setId(1L);
        customerVO.setName("客户A");
        customerVO.setAddress("北京市朝阳区");
        customerVO.setPhone("13800138000");

        customerRequestVO = new CustomerRequestVO();
        customerRequestVO.setName("客户A");
        customerRequestVO.setAddress("北京市朝阳区");
        customerRequestVO.setPhone("13800138000");
    }

    @Test
    void getAllCustomers_ShouldReturnCustomerList() {
        List<CustomerVO> expectedList = Arrays.asList(customerVO);
        when(customerService.getAllCustomerVOs()).thenReturn(expectedList);

        Result<List<CustomerVO>> result = customerController.getAllCustomers();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(customerService, times(1)).getAllCustomerVOs();
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        when(customerService.getCustomerById(1L)).thenReturn(customerVO);

        Result<CustomerVO> result = customerController.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(customerVO, result.getData());
        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void createCustomer_ShouldReturnSuccess() {
        doNothing().when(customerService).createCustomer(any(CustomerRequestVO.class));

        Result<Void> result = customerController.createCustomer(customerRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(customerService, times(1)).createCustomer(customerRequestVO);
    }

    @Test
    void updateCustomer_ShouldReturnSuccess() {
        doNothing().when(customerService).updateCustomer(eq(1L), any(CustomerRequestVO.class));

        Result<Void> result = customerController.updateCustomer(1L, customerRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(customerService, times(1)).updateCustomer(1L, customerRequestVO);
    }

    @Test
    void deleteCustomer_ShouldReturnSuccess() {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        Result<Void> result = customerController.deleteCustomer(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    void deleteCustomer_ShouldReturnErrorWhenFailed() {
        when(customerService.deleteCustomer(1L)).thenReturn(false);

        Result<Void> result = customerController.deleteCustomer(1L);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("删除客户失败", result.getMessage());
        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    void batchDeleteCustomers_ShouldReturnSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(customerService.batchDeleteCustomers(ids)).thenReturn(true);

        Result<Void> result = customerController.batchDeleteCustomers(ids);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(customerService, times(1)).batchDeleteCustomers(ids);
    }

    @Test
    void batchDeleteCustomers_ShouldReturnErrorWhenFailed() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(customerService.batchDeleteCustomers(ids)).thenReturn(false);

        Result<Void> result = customerController.batchDeleteCustomers(ids);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("批量删除客户失败", result.getMessage());
        verify(customerService, times(1)).batchDeleteCustomers(ids);
    }

    @Test
    void getCustomerStatistics_ShouldReturnStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 100);
        statistics.put("active", 80);
        when(customerService.getCustomerStatistics()).thenReturn(statistics);

        Result<Map<String, Object>> result = customerController.getCustomerStatistics();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(statistics, result.getData());
        verify(customerService, times(1)).getCustomerStatistics();
    }

    @Test
    void getCustomersByName_ShouldReturnCustomerList() {
        List<CustomerVO> expectedList = Arrays.asList(customerVO);
        when(customerService.getCustomersByName("客户A")).thenReturn(expectedList);

        Result<List<CustomerVO>> result = customerController.getCustomersByName("客户A");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(customerService, times(1)).getCustomersByName("客户A");
    }

    @Test
    void getCustomersByAddress_ShouldReturnCustomerList() {
        List<CustomerVO> expectedList = Arrays.asList(customerVO);
        when(customerService.getCustomersByAddress("北京市朝阳区")).thenReturn(expectedList);

        Result<List<CustomerVO>> result = customerController.getCustomersByAddress("北京市朝阳区");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(customerService, times(1)).getCustomersByAddress("北京市朝阳区");
    }

    @Test
    void getCustomerOrderCount_ShouldReturnOrderCountList() {
        Map<String, Object> orderCount = new HashMap<>();
        orderCount.put("customerId", 1L);
        orderCount.put("customerName", "客户A");
        orderCount.put("orderCount", 10);
        List<Map<String, Object>> expectedList = Arrays.asList(orderCount);
        when(customerService.getCustomerOrderCount()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = customerController.getCustomerOrderCount();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(customerService, times(1)).getCustomerOrderCount();
    }
}
