package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "product")
public class Product extends BaseEntity {
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Long supplierId;
    private String unit;
    private Integer safetyStock;
    private Integer status;
}