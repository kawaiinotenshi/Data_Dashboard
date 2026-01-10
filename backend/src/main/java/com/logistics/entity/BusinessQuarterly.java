package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "business_quarterly")
public class BusinessQuarterly extends BaseEntity {
    private Integer year;
    private Integer quarter;
    private String businessType;
    private BigDecimal amount;
    private BigDecimal growthRate;
}