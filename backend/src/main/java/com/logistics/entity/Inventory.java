package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "inventory_ratio", indexes = {
    @TableIndex(fieldList = "enterpriseName", name = "idx_inventory_enterprise"),
    @TableIndex(fieldList = "month", name = "idx_inventory_month")
})
public class Inventory extends BaseEntity {
    private String enterpriseName;
    private BigDecimal ratio;
    private String month;
}
