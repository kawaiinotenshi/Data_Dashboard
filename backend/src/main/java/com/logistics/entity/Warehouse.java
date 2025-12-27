package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.IndexName;
import com.baomidou.mybatisplus.annotation.TableIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "warehouse", indexes = {
    @TableIndex(fieldList = "name", name = "idx_warehouse_name"),
    @TableIndex(fieldList = "location", name = "idx_warehouse_location")
})
public class Warehouse extends BaseEntity {
    private String name;
    private String location;
    private BigDecimal area;
    private BigDecimal utilizationRate;
    private BigDecimal capacity;
    private BigDecimal throughput;
}
