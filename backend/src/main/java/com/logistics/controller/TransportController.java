package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.TransportService;
import com.logistics.vo.TransportVO;
import com.logistics.vo.TransportRequestVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transport")
@CrossOrigin
public class TransportController {
    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("/list")
    public Result<List<TransportVO>> getAllTransports() {
        List<TransportVO> list = transportService.getAllTransportVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<TransportVO> getTransportById(@PathVariable Long id) {
        TransportVO transportVO = transportService.getTransportById(id);
        return Result.success(transportVO);
    }

    @GetMapping("/status/{status}")
    public Result<List<TransportVO>> getTransportsByStatus(@PathVariable String status) {
        List<TransportVO> list = transportService.getTransportsByStatus(status);
        return Result.success(list);
    }

    @PostMapping
    public Result<Void> createTransport(@RequestBody TransportRequestVO transportRequestVO) {
        transportService.createTransport(transportRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateTransport(@PathVariable Long id, @RequestBody TransportRequestVO transportRequestVO) {
        transportService.updateTransport(id, transportRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTransport(@PathVariable Long id) {
        boolean success = transportService.deleteTransport(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除运输记录失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteTransports(@RequestBody List<Long> ids) {
        boolean success = transportService.batchDeleteTransports(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除运输记录失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getTransportStatistics() {
        Map<String, Object> statistics = transportService.getTransportStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/month/{month}")
    public Result<List<TransportVO>> getTransportByMonth(@PathVariable String month) {
        List<TransportVO> list = transportService.getTransportByMonth(month);
        return Result.success(list);
    }

    @GetMapping("/vehicle-type/{vehicleType}")
    public Result<List<TransportVO>> getTransportByVehicleType(@PathVariable String vehicleType) {
        List<TransportVO> list = transportService.getTransportByVehicleType(vehicleType);
        return Result.success(list);
    }

    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> getTransportTrend() {
        List<Map<String, Object>> list = transportService.getTransportTrend();
        return Result.success(list);
    }

    @GetMapping("/vehicle-type-group")
    public Result<List<Map<String, Object>>> getTransportByVehicleTypeGroup() {
        List<Map<String, Object>> list = transportService.getTransportByVehicleTypeGroup();
        return Result.success(list);
    }
}
