package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.SupplierService;
import com.logistics.vo.SupplierVO;
import com.logistics.vo.SupplierRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
@CrossOrigin
@Api(tags = "供应商管理")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/list")
    @ApiOperation("获取所有供应商")
    public Result<List<SupplierVO>> getAllSuppliers() {
        List<SupplierVO> list = supplierService.getAllSupplierVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取供应商")
    public Result<SupplierVO> getSupplierById(@ApiParam("供应商ID") @PathVariable Long id) {
        SupplierVO supplierVO = supplierService.getSupplierById(id);
        return Result.success(supplierVO);
    }

    @PostMapping
    @ApiOperation("创建供应商")
    public Result<Void> createSupplier(@ApiParam("供应商信息") @RequestBody SupplierRequestVO supplierRequestVO) {
        supplierService.createSupplier(supplierRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新供应商")
    public Result<Void> updateSupplier(@ApiParam("供应商ID") @PathVariable Long id, @ApiParam("供应商信息") @RequestBody SupplierRequestVO supplierRequestVO) {
        supplierService.updateSupplier(id, supplierRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除供应商")
    public Result<Void> deleteSupplier(@ApiParam("供应商ID") @PathVariable Long id) {
        boolean success = supplierService.deleteSupplier(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除供应商失败");
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除供应商")
    public Result<Void> batchDeleteSuppliers(@ApiParam("供应商ID列表") @RequestBody List<Long> ids) {
        boolean success = supplierService.batchDeleteSuppliers(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除供应商失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取供应商统计信息")
    public Result<Map<String, Object>> getSupplierStatistics() {
        Map<String, Object> statistics = supplierService.getSupplierStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/name/{name}")
    @ApiOperation("根据名称搜索供应商")
    public Result<List<SupplierVO>> getSuppliersByName(@ApiParam("供应商名称") @PathVariable String name) {
        List<SupplierVO> list = supplierService.getSuppliersByName(name);
        return Result.success(list);
    }

    @GetMapping("/address/{address}")
    @ApiOperation("根据地址搜索供应商")
    public Result<List<SupplierVO>> getSuppliersByAddress(@ApiParam("供应商地址") @PathVariable String address) {
        List<SupplierVO> list = supplierService.getSuppliersByAddress(address);
        return Result.success(list);
    }

    @GetMapping("/contact-ranking")
    @ApiOperation("获取供应商联系排名")
    public Result<List<Map<String, Object>>> getSupplierByContactRanking() {
        List<Map<String, Object>> list = supplierService.getSupplierByContactRanking();
        return Result.success(list);
    }
}
