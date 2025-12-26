package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Warehouse;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {
    @Override
    public List<Warehouse> getAllWarehouses() {
        return list();
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return getById(id);
    }
}
