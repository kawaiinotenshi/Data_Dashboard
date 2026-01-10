package com.logistics.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.logistics.vo.*;
import com.logistics.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket工具类，用于集中管理WebSocket消息的发送
 */
@Component
public class WebSocketUtils {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderService orderService;
    private final WarehouseService warehouseService;
    private final TransportService transportService;

    @Autowired
    public WebSocketUtils(SimpMessagingTemplate messagingTemplate, 
                        @Lazy OrderService orderService, 
                        @Lazy WarehouseService warehouseService, 
                        @Lazy TransportService transportService) {
        this.messagingTemplate = messagingTemplate;
        this.orderService = orderService;
        this.warehouseService = warehouseService;
        this.transportService = transportService;
    }

    /**
     * 广播订单更新
     */
    public void broadcastOrderUpdate() {
        List<OrderVO> orders = orderService.getAllOrderVOs();
        messagingTemplate.convertAndSend("/topic/orders", orders);
    }

    /**
     * 广播仓库更新
     */
    public void broadcastWarehouseUpdate() {
        List<WarehouseVO> warehouses = warehouseService.getAllWarehouseVOs();
        messagingTemplate.convertAndSend("/topic/warehouses", warehouses);
    }

    /**
     * 广播物流任务更新
     */
    public void broadcastTransportUpdate() {
        List<TransportVO> transports = transportService.getAllTransportVOs();
        messagingTemplate.convertAndSend("/topic/transports", transports);
    }
    
    /**
     * 广播运输路线更新
     */
    public void broadcastTransportRoutes() {
        // 获取所有运输中或待处理的任务
        List<TransportVO> activeTransports = transportService.getTransportsByStatus("运输中");
        activeTransports.addAll(transportService.getTransportsByStatus("pending"));
        
        // 构建路线数据
        List<Map<String, Object>> routes = new ArrayList<>();
        for (TransportVO transport : activeTransports) {
            // 确保有完整的坐标信息
            if (transport.getOriginLng() != null && transport.getOriginLat() != null &&
                transport.getDestinationLng() != null && transport.getDestinationLat() != null) {
                
                Map<String, Object> route = new HashMap<>();
                route.put("id", transport.getId());
                route.put("origin", new double[]{transport.getOriginLng(), transport.getOriginLat()});
                route.put("destination", new double[]{transport.getDestinationLng(), transport.getDestinationLat()});
                route.put("status", transport.getStatus());
                route.put("vehicleType", transport.getVehicleType());
                
                routes.add(route);
            }
        }
        
        messagingTemplate.convertAndSend("/topic/transportRoutes", routes);
    }

    /**
     * 广播系统通知
     */
    public void broadcastSystemNotice(String message) {
        messagingTemplate.convertAndSend("/topic/notices", message);
    }

    /**
     * 广播库存预警信息
     */
    public void broadcastInventoryAlert(InventoryAlertVO alert) {
        messagingTemplate.convertAndSend("/topic/inventoryAlerts", alert);
    }

    /**
     * 广播所有数据更新
     */
    public void broadcastAllUpdates() {
        broadcastOrderUpdate();
        broadcastWarehouseUpdate();
        broadcastTransportUpdate();
        broadcastTransportRoutes();
    }
}
