package com.logistics.service.impl;

import com.logistics.controller.WebSocketController;
import com.logistics.entity.Warehouse;
import com.logistics.service.InventoryMonitorService;
import com.logistics.service.WarehouseService;
import com.logistics.vo.InventoryAlertVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存监控服务实现类
 */
@Service
public class InventoryMonitorServiceImpl implements InventoryMonitorService {

    private final WarehouseService warehouseService;
    private final WebSocketController webSocketController;
    
    // 库存预警配置
    private Map<String, Object> alertConfig = new HashMap<>();
    
    public InventoryMonitorServiceImpl(WarehouseService warehouseService, WebSocketController webSocketController) {
        this.warehouseService = warehouseService;
        this.webSocketController = webSocketController;
        
        // 初始化预警配置（默认值）
        alertConfig.put("lowThreshold", new BigDecimal("20"));  // 低库存阈值：20%
        alertConfig.put("highThreshold", new BigDecimal("80"));  // 高库存阈值：80%
        alertConfig.put("checkInterval", 5);  // 检查间隔：5分钟
        alertConfig.put("alertEnabled", true);  // 启用预警
    }

    @Override
    public List<InventoryAlertVO> checkInventoryLevels() {
        List<InventoryAlertVO> alerts = new ArrayList<>();
        
        // 获取所有仓库信息
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        
        BigDecimal lowThreshold = (BigDecimal) alertConfig.get("lowThreshold");
        BigDecimal highThreshold = (BigDecimal) alertConfig.get("highThreshold");
        
        // 检查每个仓库的库存水位
        for (Warehouse warehouse : warehouses) {
            InventoryAlertVO alert = checkSingleWarehouseInventory(warehouse, lowThreshold, highThreshold);
            if (alert != null) {
                alerts.add(alert);
                
                // 如果是警告或错误级别的预警，广播通知
                if ("WARN".equals(alert.getAlertLevel()) || "ERROR".equals(alert.getAlertLevel())) {
                    webSocketController.broadcastSystemNotice(alert.getAlertMessage());
                    // 专门广播库存预警信息
                    webSocketController.broadcastInventoryAlert(alert);
                }
            }
        }
        
        return alerts;
    }

    @Override
    public InventoryAlertVO checkInventoryLevelByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        if (warehouse == null) {
            return null;
        }
        
        BigDecimal lowThreshold = (BigDecimal) alertConfig.get("lowThreshold");
        BigDecimal highThreshold = (BigDecimal) alertConfig.get("highThreshold");
        
        return checkSingleWarehouseInventory(warehouse, lowThreshold, highThreshold);
    }

    @Override
    public Map<String, Object> getAlertConfig() {
        return Collections.unmodifiableMap(alertConfig);
    }

    @Override
    public void updateAlertConfig(Map<String, Object> config) {
        alertConfig.putAll(config);
    }

    @Override
    public Map<String, Object> generateInventoryReport() {
        Map<String, Object> report = new HashMap<>();
        
        List<InventoryAlertVO> alerts = checkInventoryLevels();
        
        // 统计不同类型的预警数量
        Map<String, Long> alertTypeStats = alerts.stream()
                .collect(Collectors.groupingBy(InventoryAlertVO::getAlertType, Collectors.counting()));
        
        Map<String, Long> alertLevelStats = alerts.stream()
                .collect(Collectors.groupingBy(InventoryAlertVO::getAlertLevel, Collectors.counting()));
        
        // 计算平均利用率
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        BigDecimal totalUtilization = warehouses.stream()
                .map(Warehouse::getUtilizationRate)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal avgUtilization = warehouses.isEmpty() ? BigDecimal.ZERO 
                : totalUtilization.divide(new BigDecimal(warehouses.size()), 2, BigDecimal.ROUND_HALF_UP);
        
        report.put("totalWarehouses", warehouses.size());
        report.put("totalAlerts", alerts.size());
        report.put("alertTypeStats", alertTypeStats);
        report.put("alertLevelStats", alertLevelStats);
        report.put("averageUtilization", avgUtilization);
        report.put("alerts", alerts);
        report.put("reportTime", LocalDateTime.now());
        
        return report;
    }
    
    /**
     * 检查单个仓库的库存水位
     */
    private InventoryAlertVO checkSingleWarehouseInventory(Warehouse warehouse, 
                                                           BigDecimal lowThreshold, 
                                                           BigDecimal highThreshold) {
        
        if (warehouse == null || warehouse.getUtilizationRate() == null) {
            return null;
        }
        
        InventoryAlertVO alert = new InventoryAlertVO();
        alert.setWarehouseId(warehouse.getId());
        alert.setWarehouseName(warehouse.getName());
        alert.setCurrentUtilizationRate(warehouse.getUtilizationRate());
        alert.setCurrentCapacity(warehouse.getCapacity());
        alert.setLowThreshold(lowThreshold);
        alert.setHighThreshold(highThreshold);
        alert.setAlertTime(LocalDateTime.now());
        
        BigDecimal utilizationRate = warehouse.getUtilizationRate();
        
        // 确定预警类型和级别
        if (utilizationRate.compareTo(lowThreshold) < 0) {
            // 低库存预警
            alert.setAlertType("LOW");
            alert.setAlertLevel("WARN");
            alert.setAlertMessage(String.format("仓库%s库存利用率过低，当前利用率为%s%%，低于阈值%s%%",
                    warehouse.getName(), utilizationRate, lowThreshold));
        } else if (utilizationRate.compareTo(highThreshold) > 0) {
            // 高库存预警
            alert.setAlertType("HIGH");
            alert.setAlertLevel("ERROR");
            alert.setAlertMessage(String.format("仓库%s库存利用率过高，当前利用率为%s%%，高于阈值%s%%",
                    warehouse.getName(), utilizationRate, highThreshold));
        } else {
            // 正常库存水平
            alert.setAlertType("NORM");
            alert.setAlertLevel("INFO");
            alert.setAlertMessage(String.format("仓库%s库存利用率正常，当前利用率为%s%%",
                    warehouse.getName(), utilizationRate));
        }
        
        return alert;
    }
}