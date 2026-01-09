package com.logistics.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * WebSocket控制器 - 仅作为WebSocket端点处理器
 */
@Controller
@CrossOrigin
public class WebSocketController {

    /**
     * 处理客户端连接
     */
    @MessageMapping("/connect")
    @SendTo("/topic/connection")
    public String handleConnect() {
        return "WebSocket连接成功";
    }
}
