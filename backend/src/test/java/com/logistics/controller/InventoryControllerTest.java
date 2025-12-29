package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.InventoryService;
import com.logistics.vo.InventoryVO;
import com.logistics.vo.InventoryRequestVO;
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
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private InventoryVO inventoryVO;
    private InventoryRequestVO inventoryRequestVO;

    @BeforeEach
    void setUp() {
        inventoryVO = new InventoryVO();
        inventoryVO.setId(1L);
        inventoryVO.setEnterpriseName("企业A");
        inventoryVO.setRatio(new java.math.BigDecimal("0.5"));
        inventoryVO.setMonth("2024-01");

        inventoryRequestVO = new InventoryRequestVO();
        inventoryRequestVO.setEnterpriseName("企业A");
        inventoryRequestVO.setRatio(new java.math.BigDecimal("0.5"));
        inventoryRequestVO.setMonth("2024-01");
    }

    @Test
    void getAllInventory_ShouldReturnInventoryList() {
        List<InventoryVO> expectedList = Arrays.asList(inventoryVO);
        when(inventoryService.getAllInventoryVOs()).thenReturn(expectedList);

        Result<List<InventoryVO>> result = inventoryController.getAllInventory();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(inventoryService, times(1)).getAllInventoryVOs();
    }

    @Test
    void getInventoryById_ShouldReturnInventory() {
        when(inventoryService.getInventoryById(1L)).thenReturn(inventoryVO);

        Result<InventoryVO> result = inventoryController.getInventoryById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(inventoryVO, result.getData());
        verify(inventoryService, times(1)).getInventoryById(1L);
    }

    @Test
    void getInventoryByWarehouse_ShouldReturnInventoryList() {
        List<InventoryVO> expectedList = Arrays.asList(inventoryVO);
        when(inventoryService.getInventoryVOsByWarehouse(1L)).thenReturn(expectedList);

        Result<List<InventoryVO>> result = inventoryController.getInventoryByWarehouse(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(inventoryService, times(1)).getInventoryVOsByWarehouse(1L);
    }

    @Test
    void createInventory_ShouldReturnSuccess() {
        doNothing().when(inventoryService).createInventory(any(InventoryRequestVO.class));

        Result<Void> result = inventoryController.createInventory(inventoryRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(inventoryService, times(1)).createInventory(inventoryRequestVO);
    }

    @Test
    void updateInventory_ShouldReturnSuccess() {
        doNothing().when(inventoryService).updateInventory(eq(1L), any(InventoryRequestVO.class));

        Result<Void> result = inventoryController.updateInventory(1L, inventoryRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(inventoryService, times(1)).updateInventory(1L, inventoryRequestVO);
    }

    @Test
    void deleteInventory_ShouldReturnSuccess() {
        when(inventoryService.deleteInventory(1L)).thenReturn(true);

        Result<Void> result = inventoryController.deleteInventory(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(inventoryService, times(1)).deleteInventory(1L);
    }

    @Test
    void deleteInventory_ShouldReturnErrorWhenFailed() {
        when(inventoryService.deleteInventory(1L)).thenReturn(false);

        Result<Void> result = inventoryController.deleteInventory(1L);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("删除库存记录失败", result.getMessage());
        verify(inventoryService, times(1)).deleteInventory(1L);
    }

    @Test
    void batchDeleteInventories_ShouldReturnSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(inventoryService.batchDeleteInventories(ids)).thenReturn(true);

        Result<Void> result = inventoryController.batchDeleteInventories(ids);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(inventoryService, times(1)).batchDeleteInventories(ids);
    }

    @Test
    void batchDeleteInventories_ShouldReturnErrorWhenFailed() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(inventoryService.batchDeleteInventories(ids)).thenReturn(false);

        Result<Void> result = inventoryController.batchDeleteInventories(ids);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("批量删除库存记录失败", result.getMessage());
        verify(inventoryService, times(1)).batchDeleteInventories(ids);
    }

    @Test
    void getInventoryStatistics_ShouldReturnStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 1000);
        statistics.put("average", 100);
        when(inventoryService.getInventoryStatistics()).thenReturn(statistics);

        Result<Map<String, Object>> result = inventoryController.getInventoryStatistics();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(statistics, result.getData());
        verify(inventoryService, times(1)).getInventoryStatistics();
    }

    @Test
    void getInventoryByMonth_ShouldReturnInventoryList() {
        List<InventoryVO> expectedList = Arrays.asList(inventoryVO);
        when(inventoryService.getInventoryByMonth("2024-01")).thenReturn(expectedList);

        Result<List<InventoryVO>> result = inventoryController.getInventoryByMonth("2024-01");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(inventoryService, times(1)).getInventoryByMonth("2024-01");
    }

    @Test
    void getInventoryByEnterpriseRanking_ShouldReturnRankingList() {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("enterprise", "企业A");
        ranking.put("quantity", 500);
        List<Map<String, Object>> expectedList = Arrays.asList(ranking);
        when(inventoryService.getInventoryByEnterpriseRanking()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = inventoryController.getInventoryByEnterpriseRanking();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(inventoryService, times(1)).getInventoryByEnterpriseRanking();
    }

    @Test
    void getInventoryTrend_ShouldReturnTrendList() {
        Map<String, Object> trend = new HashMap<>();
        trend.put("month", "2024-01");
        trend.put("quantity", 100);
        List<Map<String, Object>> expectedList = Arrays.asList(trend);
        when(inventoryService.getInventoryTrend()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = inventoryController.getInventoryTrend();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(inventoryService, times(1)).getInventoryTrend();
    }
}
