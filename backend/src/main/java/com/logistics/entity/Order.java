package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "orders")
public class Order extends BaseEntity {
    private String orderNo;
    private String customerName;
    private BigDecimal orderAmount;
    private LocalDateTime orderDate;
    private String status;
}
