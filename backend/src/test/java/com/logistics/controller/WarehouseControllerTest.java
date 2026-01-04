package com.logistics.controller;

import com.logistics.dto.WarehouseDTO;
import com.logistics.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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

    private WarehouseDTO warehouseDTO;

    @BeforeEach
    void setUp() {
        warehouseDTO = new WarehouseDTO();
        warehouseDTO.setId(1L);
        warehouseDTO.setName("大连仓");
        warehouseDTO.setLocation("大连市");
        warehouseDTO.setCapacity(10000);
        warehouseDTO.setOccupied(5000);
    }

    @Test
    void testGetAllWarehouses() {
        List<WarehouseDTO> warehouses = Arrays.asList(warehouseDTO);
        when(warehouseService.getAllWarehouses()).thenReturn(warehouses);

        ResponseEntity<List<WarehouseDTO>> response = warehouseController.getAllWarehouses();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("大连仓", response.getBody().get(0).getName());
        verify(warehouseService, times(1)).getAllWarehouses();
    }

    @Test
    void testGetWarehouseById() {
        when(warehouseService.getWarehouseById(1L)).thenReturn(warehouseDTO);

        ResponseEntity<WarehouseDTO> response = warehouseController.getWarehouseById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("大连仓", response.getBody().getName());
        verify(warehouseService, times(1)).getWarehouseById(1L);
    }

    @Test
    void testCreateWarehouse() {
        when(warehouseService.createWarehouse(warehouseDTO)).thenReturn(warehouseDTO);

        ResponseEntity<WarehouseDTO> response = warehouseController.createWarehouse(warehouseDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("大连仓", response.getBody().getName());
        verify(warehouseService, times(1)).createWarehouse(warehouseDTO);
    }

    @Test
    void testUpdateWarehouse() {
        when(warehouseService.updateWarehouse(1L, warehouseDTO)).thenReturn(warehouseDTO);

        ResponseEntity<WarehouseDTO> response = warehouseController.updateWarehouse(1L, warehouseDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("大连仓", response.getBody().getName());
        verify(warehouseService, times(1)).updateWarehouse(1L, warehouseDTO);
    }

    @Test
    void testDeleteWarehouse() {
        doNothing().when(warehouseService).deleteWarehouse(1L);

        ResponseEntity<Void> response = warehouseController.deleteWarehouse(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(warehouseService, times(1)).deleteWarehouse(1L);
    }
}
