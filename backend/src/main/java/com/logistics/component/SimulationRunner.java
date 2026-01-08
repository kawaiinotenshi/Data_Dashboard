package com.logistics.component;

import com.logistics.controller.WebSocketController;
import com.logistics.entity.Transport;
import com.logistics.entity.Warehouse;
import com.logistics.service.InventoryMonitorService;
import com.logistics.service.TransportService;
import com.logistics.service.WarehouseService;
import com.logistics.vo.TransportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * 模拟运行器，用于定期更新数据以模拟实时场景
 */
@Component
public class SimulationRunner {

    @Autowired
    private TransportService transportService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InventoryMonitorService inventoryMonitorService;

    @Autowired
    private WebSocketController webSocketController;

    private final Random random = new Random();

    /**
     * 每10秒更新一次物流任务状态
     */
    @Scheduled(fixedRate = 10000)
    public void updateTransportStatus() {
        try {
            // 获取所有运输任务
            List<Transport> transports = transportService.getAllTransports();
            if (transports.isEmpty()) {
                return;
            }

            // 随机选择一些任务更新状态
            int updateCount = Math.max(1, transports.size() / 5); // 更新1/5的任务
            for (int i = 0; i < updateCount; i++) {
                int index = random.nextInt(transports.size());
                Transport transport = transports.get(index);
                
                // 根据当前状态更新为下一个状态
                String newStatus = getNextStatus(transport.getStatus());
                if (!newStatus.equals(transport.getStatus())) {
                    transport.setStatus(newStatus);
                    transport.setUpdatedAt(LocalDateTime.now());
                    
                    // 更新任务
                    transportService.updateById(transport);
                }
            }

            // 广播更新
            webSocketController.broadcastTransportUpdate();
            webSocketController.broadcastSystemNotice("物流任务状态更新完成");
        } catch (Exception e) {
            e.printStackTrace();
            webSocketController.broadcastSystemNotice("物流任务状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 每15秒更新一次仓库库存
     */
    @Scheduled(fixedRate = 15000)
    public void updateWarehouseInventory() {
        try {
            // 获取所有仓库
            List<Warehouse> warehouses = warehouseService.getAllWarehouses();
            if (warehouses.isEmpty()) {
                return;
            }

            // 随机选择一些仓库更新库存
            int updateCount = Math.max(1, warehouses.size() / 3); // 更新1/3的仓库
            for (int i = 0; i < updateCount; i++) {
                int index = random.nextInt(warehouses.size());
                Warehouse warehouse = warehouses.get(index);
                
                // 随机调整利用率（-5% 到 +5%）
                double changeRate = (random.nextDouble() - 0.5) * 0.1; // -0.05 到 +0.05
                BigDecimal currentUtilization = warehouse.getUtilizationRate();
                if (currentUtilization == null) {
                    currentUtilization = BigDecimal.ZERO;
                }
                double newUtilization = Math.max(0, Math.min(100, currentUtilization.doubleValue() + (currentUtilization.doubleValue() * changeRate)));
                
                if (Math.abs(newUtilization - currentUtilization.doubleValue()) > 0.001) {
                    warehouse.setUtilizationRate(BigDecimal.valueOf(newUtilization));
                    warehouse.setUpdatedAt(LocalDateTime.now());
                    
                    // 更新仓库
                    warehouseService.updateById(warehouse);
                }
            }

            // 广播更新
            webSocketController.broadcastWarehouseUpdate();
            webSocketController.broadcastSystemNotice("仓库库存更新完成");
        } catch (Exception e) {
            e.printStackTrace();
            webSocketController.broadcastSystemNotice("仓库库存更新失败: " + e.getMessage());
        }
    }

    /**
     * 每30秒生成一个新的模拟订单
     */
    @Scheduled(fixedRate = 30000)
    public void generateSimulationOrder() {
        try {
            // 生成模拟订单（这里需要调用订单服务的创建方法）
            // 由于没有完整的订单创建逻辑，这里暂时注释
            // OrderRequestVO orderRequestVO = generateRandomOrder();
            // orderService.createOrder(orderRequestVO);
            
            // 广播更新
            webSocketController.broadcastOrderUpdate();
            webSocketController.broadcastSystemNotice("新订单生成完成");
        } catch (Exception e) {
            e.printStackTrace();
            webSocketController.broadcastSystemNotice("新订单生成失败: " + e.getMessage());
        }
    }

    /**
     * 每20秒执行一次全量数据广播
     */
    @Scheduled(fixedRate = 20000)
    public void broadcastAllData() {
        webSocketController.broadcastAllUpdates();
    }

    /**
     * 获取下一个状态
     */
    private String getNextStatus(String currentStatus) {
        switch (currentStatus) {
            case TransportStatus.PENDING:
                return TransportStatus.IN_TRANSIT;
            case TransportStatus.IN_TRANSIT:
                return random.nextBoolean() ? TransportStatus.DELIVERED : TransportStatus.IN_TRANSIT;
            case TransportStatus.DELIVERED:
                return TransportStatus.COMPLETED;
            case TransportStatus.COMPLETED:
            case TransportStatus.CANCELLED:
                return currentStatus; // 已完成或取消的任务不再更新
            default:
                return TransportStatus.PENDING;
        }
    }
    
    /**
     * 每10秒检查一次库存水平
     */
    @Scheduled(fixedRate = 10000)
    public void checkInventory() {
        try {
            // 检查库存水平
            inventoryMonitorService.checkInventoryLevels();
        } catch (Exception e) {
            e.printStackTrace();
            webSocketController.broadcastSystemNotice("库存检查失败: " + e.getMessage());
        }
    }
}