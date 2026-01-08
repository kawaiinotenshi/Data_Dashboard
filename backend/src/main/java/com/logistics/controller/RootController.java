package com.logistics.controller;

import com.logistics.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RootController {

    @GetMapping
    public Result<Map<String, Object>> getRootInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "物流仓储大数据展示系统 API");
        info.put("version", "1.0.0");
        info.put("status", "running");
        info.put("message", "欢迎使用物流仓储大数据展示系统 API");
        info.put("availableEndpoints", new String[] {
            "/order/**",
            "/warehouse/**",
            "/inventory/**",
            "/customer/**",
            "/supplier/**",
            "/transport/**",
            "/department/**",
            "/employee/**",
            "/ai/**",
            "/actuator/**"
        });
        return Result.success(info);
    }
}