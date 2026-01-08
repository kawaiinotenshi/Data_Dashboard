package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.WarehouseService;
import com.logistics.vo.WarehouseRequestVO;
import com.logistics.vo.WarehouseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    private WarehouseVO warehouseVO;
    private WarehouseRequestVO warehouseRequestVO;

    @BeforeEach
    void setUp() {
        // 创建 WarehouseVO
        warehouseVO = new WarehouseVO();
        warehouseVO.setId(1L);
        warehouseVO.setName("大连仓");
        warehouseVO.setLocation("大连市");
        warehouseVO.setArea(new BigDecimal(10000));
        warehouseVO.setCapacity(new BigDecimal(5000));
        warehouseVO.setUtilizationRate(new BigDecimal(0.5));
        
        // 创建 WarehouseRequestVO
        warehouseRequestVO = new WarehouseRequestVO();
        warehouseRequestVO.setName("大连仓");
        warehouseRequestVO.setLocation("大连市");
        warehouseRequestVO.setArea(new BigDecimal(10000));
        warehouseRequestVO.setCapacity(new BigDecimal(5000));
    }

    @Test
    void testGetAllWarehouses() {
        List<WarehouseVO> warehouses = Arrays.asList(warehouseVO);
        when(warehouseService.getAllWarehouseVOs()).thenReturn(warehouses);

        Result<List<WarehouseVO>> response = warehouseController.getAllWarehouses();

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(1, response.getData().size());
        assertEquals("大连仓", response.getData().get(0).getName());
        verify(warehouseService, times(1)).getAllWarehouseVOs();
    }

    @Test
    void testGetWarehouseById() {
        when(warehouseService.getWarehouseVOById(1L)).thenReturn(warehouseVO);

        Result<WarehouseVO> response = warehouseController.getWarehouseById(1L);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("大连仓", response.getData().getName());
        verify(warehouseService, times(1)).getWarehouseVOById(1L);
    }

    @Test
    void testCreateWarehouse() {
        when(warehouseService.createWarehouse(warehouseRequestVO)).thenReturn(true);

        Result<Void> response = warehouseController.createWarehouse(warehouseRequestVO);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        verify(warehouseService, times(1)).createWarehouse(warehouseRequestVO);
    }

    @Test
    void testUpdateWarehouse() {
        when(warehouseService.updateWarehouse(1L, warehouseRequestVO)).thenReturn(true);

        Result<Void> response = warehouseController.updateWarehouse(1L, warehouseRequestVO);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        verify(warehouseService, times(1)).updateWarehouse(1L, warehouseRequestVO);
    }

    @Test
    void testDeleteWarehouse() {
        when(warehouseService.deleteWarehouse(1L)).thenReturn(true);

        Result<Void> response = warehouseController.deleteWarehouse(1L);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        verify(warehouseService, times(1)).deleteWarehouse(1L);
    }
}
