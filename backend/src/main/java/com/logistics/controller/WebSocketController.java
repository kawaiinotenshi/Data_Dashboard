package com.logistics.controller;

import com.logistics.entity.Order;
import com.logistics.entity.Warehouse;
import com.logistics.entity.Transport;
import com.logistics.service.OrderService;
import com.logistics.service.WarehouseService;
import com.logistics.service.TransportService;
import com.logistics.vo.OrderVO;
import com.logistics.vo.WarehouseVO;
import com.logistics.vo.TransportVO;
import com.logistics.vo.InventoryAlertVO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * WebSocket控制器
 */
@Controller
@CrossOrigin
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderService orderService;
    private final WarehouseService warehouseService;
    private final TransportService transportService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate,
                              OrderService orderService,
                              WarehouseService warehouseService,
                              TransportService transportService) {
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
     * 广播数据更新（所有数据）
     */
    public void broadcastAllUpdates() {
        broadcastOrderUpdate();
        broadcastWarehouseUpdate();
        broadcastTransportUpdate();
    }
    
    /**
     * 广播库存预警信息
     */
    public void broadcastInventoryAlert(InventoryAlertVO alert) {
        messagingTemplate.convertAndSend("/topic/inventoryAlerts", alert);
    }

    /**
     * 处理客户端连接
     */
    @MessageMapping("/connect")
    @SendTo("/topic/connection")
    public String handleConnect() {
        return "WebSocket连接成功";
    }
}
