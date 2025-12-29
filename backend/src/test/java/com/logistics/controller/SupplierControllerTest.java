package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.SupplierService;
import com.logistics.vo.SupplierVO;
import com.logistics.vo.SupplierRequestVO;
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
class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierVO supplierVO;
    private SupplierRequestVO supplierRequestVO;

    @BeforeEach
    void setUp() {
        supplierVO = new SupplierVO();
        supplierVO.setId(1L);
        supplierVO.setName("供应商A");
        supplierVO.setAddress("上海市浦东新区");
        supplierVO.setPhone("13900139000");

        supplierRequestVO = new SupplierRequestVO();
        supplierRequestVO.setName("供应商A");
        supplierRequestVO.setAddress("上海市浦东新区");
        supplierRequestVO.setPhone("13900139000");
    }

    @Test
    void getAllSuppliers_ShouldReturnSupplierList() {
        List<SupplierVO> expectedList = Arrays.asList(supplierVO);
        when(supplierService.getAllSupplierVOs()).thenReturn(expectedList);

        Result<List<SupplierVO>> result = supplierController.getAllSuppliers();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(supplierService, times(1)).getAllSupplierVOs();
    }

    @Test
    void getSupplierById_ShouldReturnSupplier() {
        when(supplierService.getSupplierById(1L)).thenReturn(supplierVO);

        Result<SupplierVO> result = supplierController.getSupplierById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(supplierVO, result.getData());
        verify(supplierService, times(1)).getSupplierById(1L);
    }

    @Test
    void createSupplier_ShouldReturnSuccess() {
        doNothing().when(supplierService).createSupplier(any(SupplierRequestVO.class));

        Result<Void> result = supplierController.createSupplier(supplierRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(supplierService, times(1)).createSupplier(supplierRequestVO);
    }

    @Test
    void updateSupplier_ShouldReturnSuccess() {
        doNothing().when(supplierService).updateSupplier(eq(1L), any(SupplierRequestVO.class));

        Result<Void> result = supplierController.updateSupplier(1L, supplierRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(supplierService, times(1)).updateSupplier(1L, supplierRequestVO);
    }

    @Test
    void deleteSupplier_ShouldReturnSuccess() {
        when(supplierService.deleteSupplier(1L)).thenReturn(true);

        Result<Void> result = supplierController.deleteSupplier(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(supplierService, times(1)).deleteSupplier(1L);
    }

    @Test
    void deleteSupplier_ShouldReturnErrorWhenFailed() {
        when(supplierService.deleteSupplier(1L)).thenReturn(false);

        Result<Void> result = supplierController.deleteSupplier(1L);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("删除供应商失败", result.getMessage());
        verify(supplierService, times(1)).deleteSupplier(1L);
    }

    @Test
    void batchDeleteSuppliers_ShouldReturnSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(supplierService.batchDeleteSuppliers(ids)).thenReturn(true);

        Result<Void> result = supplierController.batchDeleteSuppliers(ids);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(supplierService, times(1)).batchDeleteSuppliers(ids);
    }

    @Test
    void batchDeleteSuppliers_ShouldReturnErrorWhenFailed() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(supplierService.batchDeleteSuppliers(ids)).thenReturn(false);

        Result<Void> result = supplierController.batchDeleteSuppliers(ids);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("批量删除供应商失败", result.getMessage());
        verify(supplierService, times(1)).batchDeleteSuppliers(ids);
    }

    @Test
    void getSupplierStatistics_ShouldReturnStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 50);
        statistics.put("active", 40);
        when(supplierService.getSupplierStatistics()).thenReturn(statistics);

        Result<Map<String, Object>> result = supplierController.getSupplierStatistics();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(statistics, result.getData());
        verify(supplierService, times(1)).getSupplierStatistics();
    }

    @Test
    void getSuppliersByName_ShouldReturnSupplierList() {
        List<SupplierVO> expectedList = Arrays.asList(supplierVO);
        when(supplierService.getSuppliersByName("供应商A")).thenReturn(expectedList);

        Result<List<SupplierVO>> result = supplierController.getSuppliersByName("供应商A");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(supplierService, times(1)).getSuppliersByName("供应商A");
    }

    @Test
    void getSuppliersByAddress_ShouldReturnSupplierList() {
        List<SupplierVO> expectedList = Arrays.asList(supplierVO);
        when(supplierService.getSuppliersByAddress("上海市浦东新区")).thenReturn(expectedList);

        Result<List<SupplierVO>> result = supplierController.getSuppliersByAddress("上海市浦东新区");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(supplierService, times(1)).getSuppliersByAddress("上海市浦东新区");
    }

    @Test
    void getSupplierByContactRanking_ShouldReturnRankingList() {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("supplierId", 1L);
        ranking.put("supplierName", "供应商A");
        ranking.put("contactCount", 20);
        List<Map<String, Object>> expectedList = Arrays.asList(ranking);
        when(supplierService.getSupplierByContactRanking()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = supplierController.getSupplierByContactRanking();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(supplierService, times(1)).getSupplierByContactRanking();
    }
}
