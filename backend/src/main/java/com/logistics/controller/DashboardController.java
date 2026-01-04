package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/data")
    public Result<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = dashboardService.getDashboardData();
        return Result.success(data);
    }
}
