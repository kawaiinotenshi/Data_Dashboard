package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.TransportService;
import com.logistics.vo.TransportVO;
import com.logistics.vo.TransportRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transport")
@CrossOrigin
@Api(tags = "运输管理")
public class TransportController {
    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("/list")
    @ApiOperation("获取所有运输记录")
    public Result<List<TransportVO>> getAllTransports() {
        List<TransportVO> list = transportService.getAllTransportVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取运输记录")
    public Result<TransportVO> getTransportById(@ApiParam("运输记录ID") @PathVariable Long id) {
        TransportVO transportVO = transportService.getTransportById(id);
        return Result.success(transportVO);
    }

    @GetMapping("/status/{status}")
    @ApiOperation("根据状态获取运输记录")
    public Result<List<TransportVO>> getTransportsByStatus(@ApiParam("运输状态") @PathVariable String status) {
        List<TransportVO> list = transportService.getTransportsByStatus(status);
        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("创建运输记录")
    public Result<Void> createTransport(@ApiParam("运输记录信息") @RequestBody TransportRequestVO transportRequestVO) {
        transportService.createTransport(transportRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新运输记录")
    public Result<Void> updateTransport(@ApiParam("运输记录ID") @PathVariable Long id, @ApiParam("运输记录信息") @RequestBody TransportRequestVO transportRequestVO) {
        transportService.updateTransport(id, transportRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除运输记录")
    public Result<Void> deleteTransport(@ApiParam("运输记录ID") @PathVariable Long id) {
        boolean success = transportService.deleteTransport(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除运输记录失败");
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除运输记录")
    public Result<Void> batchDeleteTransports(@ApiParam("运输记录ID列表") @RequestBody List<Long> ids) {
        boolean success = transportService.batchDeleteTransports(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除运输记录失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取运输统计信息")
    public Result<Map<String, Object>> getTransportStatistics() {
        Map<String, Object> statistics = transportService.getTransportStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/month/{month}")
    @ApiOperation("根据月份获取运输记录")
    public Result<List<TransportVO>> getTransportByMonth(@ApiParam("月份，格式：yyyy-MM") @PathVariable String month) {
        List<TransportVO> list = transportService.getTransportByMonth(month);
        return Result.success(list);
    }

    @GetMapping("/vehicle-type/{vehicleType}")
    @ApiOperation("根据车辆类型获取运输记录")
    public Result<List<TransportVO>> getTransportByVehicleType(@ApiParam("车辆类型") @PathVariable String vehicleType) {
        List<TransportVO> list = transportService.getTransportByVehicleType(vehicleType);
        return Result.success(list);
    }

    @GetMapping("/trend")
    @ApiOperation("获取运输趋势")
    public Result<List<Map<String, Object>>> getTransportTrend() {
        List<Map<String, Object>> list = transportService.getTransportTrend();
        return Result.success(list);
    }

    @GetMapping("/vehicle-type-group")
    @ApiOperation("按车辆类型分组统计")
    public Result<List<Map<String, Object>>> getTransportByVehicleTypeGroup() {
        List<Map<String, Object>> list = transportService.getTransportByVehicleTypeGroup();
        return Result.success(list);
    }
}
