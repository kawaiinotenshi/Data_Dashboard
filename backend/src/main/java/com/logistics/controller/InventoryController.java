package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.InventoryService;
import com.logistics.vo.InventoryVO;
import com.logistics.vo.InventoryRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@CrossOrigin
@Api(tags = "库存管理")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/list")
    @ApiOperation("获取所有库存")
    public Result<List<InventoryVO>> getAllInventory() {
        List<InventoryVO> list = inventoryService.getAllInventoryVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取库存")
    public Result<InventoryVO> getInventoryById(@ApiParam("库存ID") @PathVariable Long id) {
        InventoryVO inventoryVO = inventoryService.getInventoryById(id);
        return Result.success(inventoryVO);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @ApiOperation("按仓库获取库存")
    public Result<List<InventoryVO>> getInventoryByWarehouse(@ApiParam("仓库ID") @PathVariable Long warehouseId) {
        List<InventoryVO> list = inventoryService.getInventoryVOsByWarehouse(warehouseId);
        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("创建库存")
    public Result<Void> createInventory(@RequestBody InventoryRequestVO inventoryRequestVO) {
        inventoryService.createInventory(inventoryRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新库存")
    public Result<Void> updateInventory(@ApiParam("库存ID") @PathVariable Long id, @RequestBody InventoryRequestVO inventoryRequestVO) {
        inventoryService.updateInventory(id, inventoryRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除库存")
    public Result<Void> deleteInventory(@ApiParam("库存ID") @PathVariable Long id) {
        boolean success = inventoryService.deleteInventory(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除库存记录失败");
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除库存")
    public Result<Void> batchDeleteInventories(@RequestBody List<Long> ids) {
        boolean success = inventoryService.batchDeleteInventories(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除库存记录失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取库存统计")
    public Result<Map<String, Object>> getInventoryStatistics() {
        Map<String, Object> statistics = inventoryService.getInventoryStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/month/{month}")
    @ApiOperation("按月份获取库存")
    public Result<List<InventoryVO>> getInventoryByMonth(@ApiParam("月份") @PathVariable String month) {
        List<InventoryVO> list = inventoryService.getInventoryByMonth(month);
        return Result.success(list);
    }

    @GetMapping("/enterprise-ranking")
    @ApiOperation("获取企业库存排名")
    public Result<List<Map<String, Object>>> getInventoryByEnterpriseRanking() {
        List<Map<String, Object>> list = inventoryService.getInventoryByEnterpriseRanking();
        return Result.success(list);
    }

    @GetMapping("/trend")
    @ApiOperation("获取库存趋势")
    public Result<List<Map<String, Object>>> getInventoryTrend() {
        List<Map<String, Object>> list = inventoryService.getInventoryTrend();
        return Result.success(list);
    }
}
