package com.logistics.service.impl;

import com.logistics.annotation.CacheExpire;
import com.logistics.mapper.OrderMapper;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.mapper.TransportMapper;
import com.logistics.mapper.CustomerMapper;
import com.logistics.mapper.SupplierMapper;
import com.logistics.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final OrderMapper orderMapper;
    private final WarehouseMapper warehouseMapper;
    private final TransportMapper transportMapper;
    private final CustomerMapper customerMapper;
    private final SupplierMapper supplierMapper;

    // 使用固定大小的线程池进行并行处理
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public DashboardServiceImpl(OrderMapper orderMapper, WarehouseMapper warehouseMapper, 
                                TransportMapper transportMapper, CustomerMapper customerMapper,
                                SupplierMapper supplierMapper) {
        this.orderMapper = orderMapper;
        this.warehouseMapper = warehouseMapper;
        this.transportMapper = transportMapper;
        this.customerMapper = customerMapper;
        this.supplierMapper = supplierMapper;
    }

    @Override
    @CacheExpire(value = 5, timeUnit = TimeUnit.MINUTES, keyPrefix = "dashboard")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // 并行执行所有统计查询
        CompletableFuture<Map<String, Object>> orderFuture = CompletableFuture.supplyAsync(
            () -> orderMapper.selectOrderStatistics(), executorService);
            
        CompletableFuture<Map<String, Object>> warehouseFuture = CompletableFuture.supplyAsync(
            () -> warehouseMapper.selectWarehouseStatistics(), executorService);
            
        CompletableFuture<Map<String, Object>> transportFuture = CompletableFuture.supplyAsync(
            () -> transportMapper.selectTransportStatistics(), executorService);
            
        CompletableFuture<Long> customerFuture = CompletableFuture.supplyAsync(
            () -> customerMapper.selectCount(null), executorService);
            
        CompletableFuture<Long> supplierFuture = CompletableFuture.supplyAsync(
            () -> supplierMapper.selectCount(null), executorService);
            
        // 等待所有查询完成并收集结果
        try {
            Map<String, Object> orderStats = orderFuture.get();
            Map<String, Object> warehouseStats = warehouseFuture.get();
            Map<String, Object> transportStats = transportFuture.get();
            Long customerCount = customerFuture.get();
            Long supplierCount = supplierFuture.get();
            
            dashboardData.put("orderCount", orderStats.get("totalCount"));
            dashboardData.put("warehouseCount", warehouseStats.get("totalCount"));
            dashboardData.put("transportCount", transportStats.get("totalCount"));
            dashboardData.put("customerCount", customerCount);
            dashboardData.put("supplierCount", supplierCount);
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常
            e.printStackTrace();
            // 如果发生异常，回退到串行处理
            Map<String, Object> orderStats = orderMapper.selectOrderStatistics();
            dashboardData.put("orderCount", orderStats.get("totalCount"));
            
            Map<String, Object> warehouseStats = warehouseMapper.selectWarehouseStatistics();
            dashboardData.put("warehouseCount", warehouseStats.get("totalCount"));
            
            Map<String, Object> transportStats = transportMapper.selectTransportStatistics();
            dashboardData.put("transportCount", transportStats.get("totalCount"));
            
            dashboardData.put("customerCount", customerMapper.selectCount(null));
            dashboardData.put("supplierCount", supplierMapper.selectCount(null));
        }
        
        return dashboardData;
    }
}
