package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Warehouse;

import java.util.List;

public interface WarehouseService extends IService<Warehouse> {
    List<Warehouse> getAllWarehouses();
    Warehouse getWarehouseById(Long id);
}
