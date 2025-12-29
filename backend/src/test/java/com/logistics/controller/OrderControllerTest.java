package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.OrderService;
import com.logistics.vo.OrderVO;
import com.logistics.vo.OrderRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderVO orderVO;
    private OrderRequestVO orderRequestVO;

    @BeforeEach
    void setUp() {
        orderVO = new OrderVO();
        orderVO.setId(1L);
        orderVO.setOrderNo("ORD001");
        orderVO.setCustomerName("客户A");
        orderVO.setOrderAmount(new java.math.BigDecimal("1000.00"));
        orderVO.setStatus("delivered");

        orderRequestVO = new OrderRequestVO();
        orderRequestVO.setOrderNo("ORD001");
        orderRequestVO.setCustomerName("客户A");
        orderRequestVO.setOrderAmount(new java.math.BigDecimal("1000.00"));
        orderRequestVO.setStatus("delivered");
    }

    @Test
    void getAllOrders_ShouldReturnOrderList() {
        List<OrderVO> expectedList = Arrays.asList(orderVO);
        when(orderService.getAllOrderVOs()).thenReturn(expectedList);

        Result<List<OrderVO>> result = orderController.getAllOrders();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(orderService, times(1)).getAllOrderVOs();
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        when(orderService.getOrderById(1L)).thenReturn(orderVO);

        Result<OrderVO> result = orderController.getOrderById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(orderVO, result.getData());
        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void createOrder_ShouldReturnSuccess() {
        doNothing().when(orderService).createOrder(any(OrderRequestVO.class));

        Result<Void> result = orderController.createOrder(orderRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(orderService, times(1)).createOrder(orderRequestVO);
    }

    @Test
    void updateOrder_ShouldReturnSuccess() {
        doNothing().when(orderService).updateOrder(eq(1L), any(OrderRequestVO.class));

        Result<Void> result = orderController.updateOrder(1L, orderRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(orderService, times(1)).updateOrder(1L, orderRequestVO);
    }

    @Test
    void deleteOrder_ShouldReturnSuccess() {
        when(orderService.deleteOrder(1L)).thenReturn(true);

        Result<Void> result = orderController.deleteOrder(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void deleteOrder_ShouldReturnErrorWhenFailed() {
        when(orderService.deleteOrder(1L)).thenReturn(false);

        Result<Void> result = orderController.deleteOrder(1L);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("删除订单失败", result.getMessage());
        verify(orderService, times(1)).deleteOrder(1L);
    }
}
