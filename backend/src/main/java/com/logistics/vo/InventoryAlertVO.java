package com.logistics.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存预警信息VO
 */
@Data
public class InventoryAlertVO {
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 仓库名称
     */
    private String warehouseName;
    
    /**
     * 当前利用率
     */
    private BigDecimal currentUtilizationRate;
    
    /**
     * 当前容量
     */
    private BigDecimal currentCapacity;
    
    /**
     * 预警类型：LOW - 低库存，HIGH - 高库存，NORM - 正常
     */
    private String alertType;
    
    /**
     * 预警级别：INFO - 信息，WARN - 警告，ERROR - 错误
     */
    private String alertLevel;
    
    /**
     * 预警消息
     */
    private String alertMessage;
    
    /**
     * 预警时间戳
     */
    private Long timestamp;
    
    /**
     * 低库存阈值
     */
    private BigDecimal lowThreshold;
    
    /**
     * 高库存阈值
     */
    private BigDecimal highThreshold;
    
    /**
     * 预警时间
     */
    private LocalDateTime alertTime;
}