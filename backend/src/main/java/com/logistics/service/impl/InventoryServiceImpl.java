package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Inventory;
import com.logistics.mapper.InventoryMapper;
import com.logistics.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {
    @Override
    public List<Inventory> getAllInventory() {
        return list();
    }

    @Override
    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("warehouse_id", warehouseId);
        return list(wrapper);
    }
}
