package com.logistics.service;

import com.logistics.entity.Warehouse;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.service.impl.WarehouseServiceImpl;
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
class WarehouseServiceTest {

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;
    private WarehouseVO warehouseVO;
    private WarehouseRequestVO warehouseRequestVO;

    @BeforeEach
    void setUp() {
        // 创建 Warehouse 实体
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("大连仓");
        warehouse.setLocation("大连市");
        warehouse.setArea(new BigDecimal(10000));
        warehouse.setCapacity(new BigDecimal(5000));
        warehouse.setThroughput(new BigDecimal(0));
        
        // 创建 WarehouseRequestVO
        warehouseRequestVO = new WarehouseRequestVO();
        warehouseRequestVO.setName("大连仓");
        warehouseRequestVO.setLocation("大连市");
        warehouseRequestVO.setArea(new BigDecimal(10000));
        warehouseRequestVO.setCapacity(new BigDecimal(5000));
    }

    @Test
    void testGetAllWarehouses() {
        List<Warehouse> warehouses = Arrays.asList(warehouse);
        when(warehouseMapper.selectList(null)).thenReturn(warehouses);

        List<Warehouse> result = warehouseService.getAllWarehouses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("大连仓", result.get(0).getName());
        verify(warehouseMapper, times(1)).selectList(null);
    }

    @Test
    void testGetWarehouseById() {
        when(warehouseMapper.selectById(1L)).thenReturn(warehouse);

        Warehouse result = warehouseService.getWarehouseById(1L);

        assertNotNull(result);
        assertEquals("大连仓", result.getName());
        verify(warehouseMapper, times(1)).selectById(1L);
    }

    @Test
    void testCreateWarehouse() {
        when(warehouseMapper.insert(any(Warehouse.class))).thenReturn(1);

        boolean result = warehouseService.createWarehouse(warehouseRequestVO);

        assertTrue(result);
        verify(warehouseMapper, times(1)).insert(any(Warehouse.class));
    }

    @Test
    void testUpdateWarehouse() {
        when(warehouseMapper.selectById(1L)).thenReturn(warehouse);
        when(warehouseMapper.updateById(any(Warehouse.class))).thenReturn(1);

        boolean result = warehouseService.updateWarehouse(1L, warehouseRequestVO);

        assertTrue(result);
        verify(warehouseMapper, times(1)).selectById(1L);
        verify(warehouseMapper, times(1)).updateById(any(Warehouse.class));
    }

    @Test
    void testDeleteWarehouse() {
        when(warehouseMapper.deleteById(1L)).thenReturn(1);

        boolean result = warehouseService.deleteWarehouse(1L);

        assertTrue(result);
        verify(warehouseMapper, times(1)).deleteById(1L);
    }
}
