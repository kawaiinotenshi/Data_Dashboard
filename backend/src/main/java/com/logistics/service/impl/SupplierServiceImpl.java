package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Supplier;
import com.logistics.mapper.SupplierMapper;
import com.logistics.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Override
    public List<Supplier> getAllSuppliers() {
        return list();
    }
}
