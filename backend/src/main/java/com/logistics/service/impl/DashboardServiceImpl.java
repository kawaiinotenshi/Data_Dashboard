package com.logistics.service.impl;

import com.logistics.annotation.CacheExpire;
import com.logistics.mapper.OrderMapper;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.mapper.TransportMapper;
import com.logistics.mapper.CustomerMapper;
import com.logistics.mapper.SupplierMapper;
import com.logistics.service.DashboardService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final OrderMapper orderMapper;
    private final WarehouseMapper warehouseMapper;
    private final TransportMapper transportMapper;
    private final CustomerMapper customerMapper;
    private final SupplierMapper supplierMapper;

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
        
        Map<String, Object> orderStats = orderMapper.selectOrderStatistics();
        dashboardData.put("orderCount", orderStats.get("totalCount"));
        
        Map<String, Object> warehouseStats = warehouseMapper.selectWarehouseStatistics();
        dashboardData.put("warehouseCount", warehouseStats.get("totalCount"));
        
        Map<String, Object> transportStats = transportMapper.selectTransportStatistics();
        dashboardData.put("transportCount", transportStats.get("totalCount"));
        
        dashboardData.put("customerCount", customerMapper.selectCount(null));
        dashboardData.put("supplierCount", supplierMapper.selectCount(null));
        
        return dashboardData;
    }
}
