package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Order;
import com.logistics.vo.OrderRequestVO;
import com.logistics.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    List<Order> getAllOrders();
    List<OrderVO> getAllOrderVOs();
    List<OrderVO> getOrdersByStatus(String status);
    OrderVO getOrderById(Long id);
    boolean createOrder(OrderRequestVO orderRequestVO);
    boolean updateOrder(Long id, OrderRequestVO orderRequestVO);
    boolean deleteOrder(Long id);
    boolean batchDeleteOrders(List<Long> ids);
    
    boolean batchCreateOrders(List<OrderRequestVO> orderRequestVOs);
    boolean batchUpdateOrders(List<Order> orders);
    
    Map<String, Object> getOrderStatistics();
    List<Map<String, Object>> getOrdersByStatusGroup();
    List<Map<String, Object>> getOrdersByDateRange(String startDate, String endDate);
    List<OrderVO> getOrdersByCustomerName(String customerName);
}
