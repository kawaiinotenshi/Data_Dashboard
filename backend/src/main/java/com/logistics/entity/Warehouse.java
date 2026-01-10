package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "warehouse")
public class Warehouse extends BaseEntity {
    private String name;
    private String location;
    private BigDecimal area;
    private BigDecimal utilizationRate;
    private BigDecimal capacity;
    private BigDecimal throughput;
    private Integer status; // 0: 正常, 1: 低库存警告, 2: 高库存警告
}
