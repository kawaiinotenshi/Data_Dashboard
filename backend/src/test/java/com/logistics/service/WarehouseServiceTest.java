package com.logistics.service;

import com.logistics.dto.WarehouseDTO;
import com.logistics.entity.Warehouse;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.service.impl.WarehouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("大连仓");
        warehouse.setLocation("大连市");
        warehouse.setCapacity(10000);
        warehouse.setOccupied(5000);
    }

    @Test
    void testGetAllWarehouses() {
        List<Warehouse> warehouses = Arrays.asList(warehouse);
        when(warehouseMapper.selectList(null)).thenReturn(warehouses);

        List<WarehouseDTO> result = warehouseService.getAllWarehouses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("大连仓", result.get(0).getName());
        verify(warehouseMapper, times(1)).selectList(null);
    }

    @Test
    void testGetWarehouseById() {
        when(warehouseMapper.selectById(1L)).thenReturn(warehouse);

        WarehouseDTO result = warehouseService.getWarehouseById(1L);

        assertNotNull(result);
        assertEquals("大连仓", result.getName());
        verify(warehouseMapper, times(1)).selectById(1L);
    }

    @Test
    void testCreateWarehouse() {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setName("大连仓");
        dto.setLocation("大连市");
        dto.setCapacity(10000);
        dto.setOccupied(5000);

        when(warehouseMapper.insert(any(Warehouse.class))).thenReturn(1);

        WarehouseDTO result = warehouseService.createWarehouse(dto);

        assertNotNull(result);
        assertEquals("大连仓", result.getName());
        verify(warehouseMapper, times(1)).insert(any(Warehouse.class));
    }

    @Test
    void testUpdateWarehouse() {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(1L);
        dto.setName("大连仓");
        dto.setLocation("大连市");
        dto.setCapacity(10000);
        dto.setOccupied(5000);

        when(warehouseMapper.selectById(1L)).thenReturn(warehouse);
        when(warehouseMapper.updateById(any(Warehouse.class))).thenReturn(1);

        WarehouseDTO result = warehouseService.updateWarehouse(1L, dto);

        assertNotNull(result);
        assertEquals("大连仓", result.getName());
        verify(warehouseMapper, times(1)).selectById(1L);
        verify(warehouseMapper, times(1)).updateById(any(Warehouse.class));
    }

    @Test
    void testDeleteWarehouse() {
        when(warehouseMapper.deleteById(1L)).thenReturn(1);

        warehouseService.deleteWarehouse(1L);

        verify(warehouseMapper, times(1)).deleteById(1L);
    }
}
