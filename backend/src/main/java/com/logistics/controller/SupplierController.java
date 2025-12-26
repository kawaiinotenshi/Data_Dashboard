package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.Supplier;
import com.logistics.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@CrossOrigin
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/list")
    public Result<List<Supplier>> getAllSuppliers() {
        List<Supplier> list = supplierService.getAllSuppliers();
        return Result.success(list);
    }
}
