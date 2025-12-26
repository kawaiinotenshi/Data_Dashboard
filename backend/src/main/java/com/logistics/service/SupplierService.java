package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Supplier;

import java.util.List;

public interface SupplierService extends IService<Supplier> {
    List<Supplier> getAllSuppliers();
}
