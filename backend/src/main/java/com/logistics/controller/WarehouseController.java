package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.Warehouse;
import com.logistics.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
@CrossOrigin
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/list")
    public Result<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> list = warehouseService.getAllWarehouses();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Warehouse> getWarehouseById(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        return Result.success(warehouse);
    }
}
