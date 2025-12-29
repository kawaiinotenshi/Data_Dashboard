package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.WarehouseService;
import com.logistics.vo.WarehouseVO;
import com.logistics.vo.WarehouseRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        warehouseVO = new WarehouseVO();
        warehouseVO.setId(1L);
        warehouseVO.setName("主仓库");
        warehouseVO.setLocation("北京市");
        warehouseVO.setCapacity(BigDecimal.valueOf(10000));
        warehouseVO.setUtilizationRate(BigDecimal.valueOf(0.75));

        warehouseRequestVO = new WarehouseRequestVO();
        warehouseRequestVO.setName("主仓库");
        warehouseRequestVO.setLocation("北京市");
        warehouseRequestVO.setCapacity(BigDecimal.valueOf(10000));
        warehouseRequestVO.setUtilizationRate(BigDecimal.valueOf(0.75));
    }

    @Test
    void getAllWarehouses_ShouldReturnWarehouseList() {
        List<WarehouseVO> expectedList = Arrays.asList(warehouseVO);
        when(warehouseService.getAllWarehouseVOs()).thenReturn(expectedList);

        Result<List<WarehouseVO>> result = warehouseController.getAllWarehouses();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(warehouseService, times(1)).getAllWarehouseVOs();
    }

    @Test
    void getWarehouseById_ShouldReturnWarehouse() {
        when(warehouseService.getWarehouseVOById(1L)).thenReturn(warehouseVO);

        Result<WarehouseVO> result = warehouseController.getWarehouseById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(warehouseVO, result.getData());
        verify(warehouseService, times(1)).getWarehouseVOById(1L);
    }

    @Test
    void createWarehouse_ShouldReturnSuccess() {
        doNothing().when(warehouseService).createWarehouse(any(WarehouseRequestVO.class));

        Result<Void> result = warehouseController.createWarehouse(warehouseRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(warehouseService, times(1)).createWarehouse(warehouseRequestVO);
    }

    @Test
    void updateWarehouse_ShouldReturnSuccess() {
        doNothing().when(warehouseService).updateWarehouse(eq(1L), any(WarehouseRequestVO.class));

        Result<Void> result = warehouseController.updateWarehouse(1L, warehouseRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(warehouseService, times(1)).updateWarehouse(1L, warehouseRequestVO);
    }

    @Test
    void deleteWarehouse_ShouldReturnSuccess() {
        when(warehouseService.deleteWarehouse(1L)).thenReturn(true);

        Result<Void> result = warehouseController.deleteWarehouse(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(warehouseService, times(1)).deleteWarehouse(1L);
    }

    @Test
    void deleteWarehouse_ShouldReturnErrorWhenFailed() {
        when(warehouseService.deleteWarehouse(1L)).thenReturn(false);

        Result<Void> result = warehouseController.deleteWarehouse(1L);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("删除仓库失败", result.getMessage());
        verify(warehouseService, times(1)).deleteWarehouse(1L);
    }

    @Test
    void batchDeleteWarehouses_ShouldReturnSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(warehouseService.batchDeleteWarehouses(ids)).thenReturn(true);

        Result<Void> result = warehouseController.batchDeleteWarehouses(ids);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(warehouseService, times(1)).batchDeleteWarehouses(ids);
    }

    @Test
    void batchDeleteWarehouses_ShouldReturnErrorWhenFailed() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(warehouseService.batchDeleteWarehouses(ids)).thenReturn(false);

        Result<Void> result = warehouseController.batchDeleteWarehouses(ids);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("批量删除仓库失败", result.getMessage());
        verify(warehouseService, times(1)).batchDeleteWarehouses(ids);
    }

    @Test
    void getWarehouseStatistics_ShouldReturnStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 10);
        statistics.put("average", 0.75);
        when(warehouseService.getWarehouseStatistics()).thenReturn(statistics);

        Result<Map<String, Object>> result = warehouseController.getWarehouseStatistics();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(statistics, result.getData());
        verify(warehouseService, times(1)).getWarehouseStatistics();
    }

    @Test
    void getWarehousesByLocation_ShouldReturnWarehouseList() {
        List<WarehouseVO> expectedList = Arrays.asList(warehouseVO);
        when(warehouseService.getWarehousesByLocation("北京市")).thenReturn(expectedList);

        Result<List<WarehouseVO>> result = warehouseController.getWarehousesByLocation("北京市");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(warehouseService, times(1)).getWarehousesByLocation("北京市");
    }

    @Test
    void getWarehousesByUtilizationRange_ShouldReturnWarehouseList() {
        List<WarehouseVO> expectedList = Arrays.asList(warehouseVO);
        BigDecimal minRate = BigDecimal.valueOf(0.5);
        BigDecimal maxRate = BigDecimal.valueOf(0.8);
        when(warehouseService.getWarehousesByUtilizationRange(minRate, maxRate)).thenReturn(expectedList);

        Result<List<WarehouseVO>> result = warehouseController.getWarehousesByUtilizationRange(minRate, maxRate);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(warehouseService, times(1)).getWarehousesByUtilizationRange(minRate, maxRate);
    }

    @Test
    void getWarehouseCapacityRanking_ShouldReturnRankingList() {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("name", "主仓库");
        ranking.put("capacity", 10000);
        List<Map<String, Object>> expectedList = Arrays.asList(ranking);
        when(warehouseService.getWarehouseCapacityRanking()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = warehouseController.getWarehouseCapacityRanking();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(warehouseService, times(1)).getWarehouseCapacityRanking();
    }
}
