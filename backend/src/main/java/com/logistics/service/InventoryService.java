package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Inventory;

import java.util.List;

public interface InventoryService extends IService<Inventory> {
    List<Inventory> getAllInventory();
    List<Inventory> getInventoryByWarehouse(Long warehouseId);
}
