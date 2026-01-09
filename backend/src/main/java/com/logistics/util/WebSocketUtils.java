package com.logistics.util;

import java.util.List;
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
    }
}
