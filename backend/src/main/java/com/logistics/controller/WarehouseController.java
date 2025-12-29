package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.WarehouseService;
import com.logistics.vo.WarehouseVO;
import com.logistics.vo.WarehouseRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse")
@CrossOrigin
@Api(tags = "仓库管理")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/list")
    @ApiOperation("获取所有仓库")
    public Result<List<WarehouseVO>> getAllWarehouses() {
        List<WarehouseVO> list = warehouseService.getAllWarehouseVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取仓库")
    public Result<WarehouseVO> getWarehouseById(@ApiParam("仓库ID") @PathVariable Long id) {
        WarehouseVO warehouseVO = warehouseService.getWarehouseVOById(id);
        return Result.success(warehouseVO);
    }

    @PostMapping
    @ApiOperation("创建仓库")
    public Result<Void> createWarehouse(@RequestBody WarehouseRequestVO warehouseRequestVO) {
        warehouseService.createWarehouse(warehouseRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新仓库")
    public Result<Void> updateWarehouse(@ApiParam("仓库ID") @PathVariable Long id, @RequestBody WarehouseRequestVO warehouseRequestVO) {
        warehouseService.updateWarehouse(id, warehouseRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除仓库")
    public Result<Void> deleteWarehouse(@ApiParam("仓库ID") @PathVariable Long id) {
        boolean success = warehouseService.deleteWarehouse(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除仓库失败");
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除仓库")
    public Result<Void> batchDeleteWarehouses(@RequestBody List<Long> ids) {
        boolean success = warehouseService.batchDeleteWarehouses(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除仓库失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取仓库统计")
    public Result<Map<String, Object>> getWarehouseStatistics() {
        Map<String, Object> statistics = warehouseService.getWarehouseStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/location/{location}")
    @ApiOperation("按位置获取仓库")
    public Result<List<WarehouseVO>> getWarehousesByLocation(@ApiParam("仓库位置") @PathVariable String location) {
        List<WarehouseVO> list = warehouseService.getWarehousesByLocation(location);
        return Result.success(list);
    }

    @GetMapping("/utilization-range")
    @ApiOperation("按利用率范围获取仓库")
    public Result<List<WarehouseVO>> getWarehousesByUtilizationRange(
            @ApiParam("最小利用率") @RequestParam BigDecimal minRate,
            @ApiParam("最大利用率") @RequestParam BigDecimal maxRate) {
        List<WarehouseVO> list = warehouseService.getWarehousesByUtilizationRange(minRate, maxRate);
        return Result.success(list);
    }

    @GetMapping("/capacity-ranking")
    @ApiOperation("获取仓库容量排名")
    public Result<List<Map<String, Object>>> getWarehouseCapacityRanking() {
        List<Map<String, Object>> list = warehouseService.getWarehouseCapacityRanking();
        return Result.success(list);
    }
}
