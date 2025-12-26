package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transport")
public class Transport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String vehicleType;
    private Integer vehicleCount;
    private BigDecimal totalDistance;
    private String month;
    private LocalDateTime createdTime;
}
