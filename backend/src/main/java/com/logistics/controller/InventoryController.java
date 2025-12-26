package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.Inventory;
import com.logistics.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@CrossOrigin
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    public Result<List<Inventory>> getAllInventory() {
        List<Inventory> list = inventoryService.getAllInventory();
        return Result.success(list);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<Inventory>> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        List<Inventory> list = inventoryService.getInventoryByWarehouse(warehouseId);
        return Result.success(list);
    }
}
