package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "transport")
public class Transport extends BaseEntity {
    private String vehicleType;
    private Integer vehicleCount;
    private BigDecimal totalDistance;
    private String month;
    private String status;
    
    // 新增起始地和目的地字段
    private String origin;
    private String destination;
    
    // 坐标字段
    @TableField(exist = false)
    private Double originLng;
    @TableField(exist = false)
    private Double originLat;
    @TableField(exist = false)
    private Double destinationLng;
    @TableField(exist = false)
    private Double destinationLat;
    
    @TableField(exist = false)
    private Long orderId;
    
    @TableField(exist = false)
    private LocalDateTime scheduledTime;
    
    @TableField(exist = false)
    private LocalDateTime actualStartTime;
    
    @TableField(exist = false)
    private LocalDateTime actualEndTime;
}
