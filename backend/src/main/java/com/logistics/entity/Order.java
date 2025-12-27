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
@TableName(value = "orders", indexes = {
    @TableIndex(fieldList = "orderNo", name = "idx_order_no", isUnique = true),
    @TableIndex(fieldList = "customerName", name = "idx_order_customer"),
    @TableIndex(fieldList = "status", name = "idx_order_status"),
    @TableIndex(fieldList = "orderDate", name = "idx_order_date")
})
public class Order extends BaseEntity {
    private String orderNo;
    private String customerName;
    private BigDecimal orderAmount;
    private LocalDateTime orderDate;
    private String status;
}
