package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.SupplierService;
import com.logistics.vo.SupplierVO;
import com.logistics.vo.SupplierRequestVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/list")
    public Result<List<SupplierVO>> getAllSuppliers() {
        List<SupplierVO> list = supplierService.getAllSupplierVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<SupplierVO> getSupplierById(@PathVariable Long id) {
        SupplierVO supplierVO = supplierService.getSupplierById(id);
        return Result.success(supplierVO);
    }

    @PostMapping
    public Result<Void> createSupplier(@RequestBody SupplierRequestVO supplierRequestVO) {
        supplierService.createSupplier(supplierRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateSupplier(@PathVariable Long id, @RequestBody SupplierRequestVO supplierRequestVO) {
        supplierService.updateSupplier(id, supplierRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteSupplier(@PathVariable Long id) {
        boolean success = supplierService.deleteSupplier(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除供应商失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteSuppliers(@RequestBody List<Long> ids) {
        boolean success = supplierService.batchDeleteSuppliers(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除供应商失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getSupplierStatistics() {
        Map<String, Object> statistics = supplierService.getSupplierStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/name/{name}")
    public Result<List<SupplierVO>> getSuppliersByName(@PathVariable String name) {
        List<SupplierVO> list = supplierService.getSuppliersByName(name);
        return Result.success(list);
    }

    @GetMapping("/address/{address}")
    public Result<List<SupplierVO>> getSuppliersByAddress(@PathVariable String address) {
        List<SupplierVO> list = supplierService.getSuppliersByAddress(address);
        return Result.success(list);
    }

    @GetMapping("/contact-ranking")
    public Result<List<Map<String, Object>>> getSupplierByContactRanking() {
        List<Map<String, Object>> list = supplierService.getSupplierByContactRanking();
        return Result.success(list);
    }
}
