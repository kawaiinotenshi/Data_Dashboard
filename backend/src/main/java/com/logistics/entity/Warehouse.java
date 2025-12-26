package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("warehouse")
public class Warehouse {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String location;
    private BigDecimal area;
    private BigDecimal utilizationRate;
    private BigDecimal capacity;
    private BigDecimal throughput;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
