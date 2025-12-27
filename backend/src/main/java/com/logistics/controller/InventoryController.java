package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.InventoryService;
import com.logistics.vo.InventoryVO;
import com.logistics.vo.InventoryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@CrossOrigin
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    public Result<List<InventoryVO>> getAllInventory() {
        List<InventoryVO> list = inventoryService.getAllInventoryVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<InventoryVO> getInventoryById(@PathVariable Long id) {
        InventoryVO inventoryVO = inventoryService.getInventoryById(id);
        return Result.success(inventoryVO);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<InventoryVO>> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        List<InventoryVO> list = inventoryService.getInventoryVOsByWarehouse(warehouseId);
        return Result.success(list);
    }

    @PostMapping
    public Result<Void> createInventory(@RequestBody InventoryRequestVO inventoryRequestVO) {
        inventoryService.createInventory(inventoryRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateInventory(@PathVariable Long id, @RequestBody InventoryRequestVO inventoryRequestVO) {
        inventoryService.updateInventory(id, inventoryRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteInventory(@PathVariable Long id) {
        boolean success = inventoryService.deleteInventory(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除库存记录失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteInventories(@RequestBody List<Long> ids) {
        boolean success = inventoryService.batchDeleteInventories(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除库存记录失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getInventoryStatistics() {
        Map<String, Object> statistics = inventoryService.getInventoryStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/month/{month}")
    public Result<List<InventoryVO>> getInventoryByMonth(@PathVariable String month) {
        List<InventoryVO> list = inventoryService.getInventoryByMonth(month);
        return Result.success(list);
    }

    @GetMapping("/enterprise-ranking")
    public Result<List<Map<String, Object>>> getInventoryByEnterpriseRanking() {
        List<Map<String, Object>> list = inventoryService.getInventoryByEnterpriseRanking();
        return Result.success(list);
    }

    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> getInventoryTrend() {
        List<Map<String, Object>> list = inventoryService.getInventoryTrend();
        return Result.success(list);
    }
}
