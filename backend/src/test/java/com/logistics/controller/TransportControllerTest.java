package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.TransportService;
import com.logistics.vo.TransportVO;
import com.logistics.vo.TransportRequestVO;
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
class TransportControllerTest {

    @Mock
    private TransportService transportService;

    @InjectMocks
    private TransportController transportController;

    private TransportVO transportVO;
    private TransportRequestVO transportRequestVO;

    @BeforeEach
    void setUp() {
        transportVO = new TransportVO();
        transportVO.setId(1L);
        transportVO.setVehicleType("卡车");
        transportVO.setVehicleCount(10);
        transportVO.setTotalDistance(new java.math.BigDecimal("1000"));
        transportVO.setMonth("2024-01");

        transportRequestVO = new TransportRequestVO();
        transportRequestVO.setVehicleType("卡车");
        transportRequestVO.setVehicleCount(10);
        transportRequestVO.setTotalDistance(new java.math.BigDecimal("1000"));
        transportRequestVO.setMonth("2024-01");
    }

    @Test
    void getAllTransports_ShouldReturnTransportList() {
        List<TransportVO> expectedList = Arrays.asList(transportVO);
        when(transportService.getAllTransportVOs()).thenReturn(expectedList);

        Result<List<TransportVO>> result = transportController.getAllTransports();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(transportService, times(1)).getAllTransportVOs();
    }

    @Test
    void getTransportById_ShouldReturnTransport() {
        when(transportService.getTransportById(1L)).thenReturn(transportVO);

        Result<TransportVO> result = transportController.getTransportById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(transportVO, result.getData());
        verify(transportService, times(1)).getTransportById(1L);
    }

    @Test
    void getTransportsByStatus_ShouldReturnTransportList() {
        List<TransportVO> expectedList = Arrays.asList(transportVO);
        when(transportService.getTransportsByStatus("运输中")).thenReturn(expectedList);

        Result<List<TransportVO>> result = transportController.getTransportsByStatus("运输中");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(transportService, times(1)).getTransportsByStatus("运输中");
    }

    @Test
    void createTransport_ShouldReturnSuccess() {
        doNothing().when(transportService).createTransport(any(TransportRequestVO.class));

        Result<Void> result = transportController.createTransport(transportRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(transportService, times(1)).createTransport(transportRequestVO);
    }

    @Test
    void updateTransport_ShouldReturnSuccess() {
        doNothing().when(transportService).updateTransport(eq(1L), any(TransportRequestVO.class));

        Result<Void> result = transportController.updateTransport(1L, transportRequestVO);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(transportService, times(1)).updateTransport(1L, transportRequestVO);
    }

    @Test
    void deleteTransport_ShouldReturnSuccess() {
        when(transportService.deleteTransport(1L)).thenReturn(true);

        Result<Void> result = transportController.deleteTransport(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(transportService, times(1)).deleteTransport(1L);
    }

    @Test
    void deleteTransport_ShouldReturnErrorWhenFailed() {
        when(transportService.deleteTransport(1L)).thenReturn(false);

        Result<Void> result = transportController.deleteTransport(1L);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("删除运输记录失败", result.getMessage());
        verify(transportService, times(1)).deleteTransport(1L);
    }

    @Test
    void batchDeleteTransports_ShouldReturnSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(transportService.batchDeleteTransports(ids)).thenReturn(true);

        Result<Void> result = transportController.batchDeleteTransports(ids);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(transportService, times(1)).batchDeleteTransports(ids);
    }

    @Test
    void batchDeleteTransports_ShouldReturnErrorWhenFailed() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(transportService.batchDeleteTransports(ids)).thenReturn(false);

        Result<Void> result = transportController.batchDeleteTransports(ids);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("批量删除运输记录失败", result.getMessage());
        verify(transportService, times(1)).batchDeleteTransports(ids);
    }

    @Test
    void getTransportStatistics_ShouldReturnStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 500);
        statistics.put("average", 50);
        when(transportService.getTransportStatistics()).thenReturn(statistics);

        Result<Map<String, Object>> result = transportController.getTransportStatistics();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(statistics, result.getData());
        verify(transportService, times(1)).getTransportStatistics();
    }

    @Test
    void getTransportByMonth_ShouldReturnTransportList() {
        List<TransportVO> expectedList = Arrays.asList(transportVO);
        when(transportService.getTransportByMonth("2024-01")).thenReturn(expectedList);

        Result<List<TransportVO>> result = transportController.getTransportByMonth("2024-01");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(transportService, times(1)).getTransportByMonth("2024-01");
    }

    @Test
    void getTransportByVehicleType_ShouldReturnTransportList() {
        List<TransportVO> expectedList = Arrays.asList(transportVO);
        when(transportService.getTransportByVehicleType("卡车")).thenReturn(expectedList);

        Result<List<TransportVO>> result = transportController.getTransportByVehicleType("卡车");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(transportService, times(1)).getTransportByVehicleType("卡车");
    }

    @Test
    void getTransportTrend_ShouldReturnTrendList() {
        Map<String, Object> trend = new HashMap<>();
        trend.put("month", "2024-01");
        trend.put("count", 50);
        List<Map<String, Object>> expectedList = Arrays.asList(trend);
        when(transportService.getTransportTrend()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = transportController.getTransportTrend();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(transportService, times(1)).getTransportTrend();
    }

    @Test
    void getTransportByVehicleTypeGroup_ShouldReturnGroupedList() {
        Map<String, Object> group = new HashMap<>();
        group.put("vehicleType", "卡车");
        group.put("count", 100);
        List<Map<String, Object>> expectedList = Arrays.asList(group);
        when(transportService.getTransportByVehicleTypeGroup()).thenReturn(expectedList);

        Result<List<Map<String, Object>>> result = transportController.getTransportByVehicleTypeGroup();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(expectedList, result.getData());
        verify(transportService, times(1)).getTransportByVehicleTypeGroup();
    }
}
