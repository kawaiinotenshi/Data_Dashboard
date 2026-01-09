package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.WarehouseService;
import com.logistics.util.WebSocketUtils;
import com.logistics.vo.WarehouseVO;
import com.logistics.vo.WarehouseRequestVO;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouse")
@CrossOrigin
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WebSocketUtils webSocketUtils;

    public WarehouseController(WarehouseService warehouseService, WebSocketUtils webSocketUtils) {
        this.warehouseService = warehouseService;
        this.webSocketUtils = webSocketUtils;
    }

    @GetMapping("/list")
    public Result<List<WarehouseVO>> getAllWarehouses() {
        List<WarehouseVO> list = warehouseService.getAllWarehouseVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<WarehouseVO> getWarehouseById(@PathVariable Long id) {
        WarehouseVO warehouseVO = warehouseService.getWarehouseVOById(id);
        return Result.success(warehouseVO);
    }

    @PostMapping
    public Result<Void> createWarehouse(@RequestBody WarehouseRequestVO warehouseRequestVO) {
        warehouseService.createWarehouse(warehouseRequestVO);
        // 广播仓库更新
        webSocketUtils.broadcastWarehouseUpdate();
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateWarehouse(@PathVariable Long id, @RequestBody WarehouseRequestVO warehouseRequestVO) {
        warehouseService.updateWarehouse(id, warehouseRequestVO);
        // 广播仓库更新
        webSocketUtils.broadcastWarehouseUpdate();
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteWarehouse(@PathVariable Long id) {
        boolean success = warehouseService.deleteWarehouse(id);
        if (success) {
            // 广播仓库更新
            webSocketUtils.broadcastWarehouseUpdate();
            return Result.success();
        } else {
            return Result.error("删除仓库失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteWarehouses(@RequestBody List<Long> ids) {
        boolean success = warehouseService.batchDeleteWarehouses(ids);
        if (success) {
            // 广播仓库更新
            webSocketUtils.broadcastWarehouseUpdate();
            return Result.success();
        } else {
            return Result.error("批量删除仓库失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getWarehouseStatistics() {
        Map<String, Object> statistics = warehouseService.getWarehouseStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/location/{location}")
    public Result<List<WarehouseVO>> getWarehousesByLocation(@PathVariable String location) {
        List<WarehouseVO> list = warehouseService.getWarehousesByLocation(location);
        return Result.success(list);
    }

    @GetMapping("/utilization-range")
    public Result<List<WarehouseVO>> getWarehousesByUtilizationRange(
            @RequestParam BigDecimal minRate,
            @RequestParam BigDecimal maxRate) {
        List<WarehouseVO> list = warehouseService.getWarehousesByUtilizationRange(minRate, maxRate);
        return Result.success(list);
    }

    @GetMapping("/capacity-ranking")
    public Result<List<Map<String, Object>>> getWarehouseCapacityRanking() {
        List<Map<String, Object>> list = warehouseService.getWarehouseCapacityRanking();
        return Result.success(list);
    }
}
