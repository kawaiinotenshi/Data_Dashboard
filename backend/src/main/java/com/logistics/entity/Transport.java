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
@TableName(value = "transport", indexes = {
    @TableIndex(fieldList = "vehicleType", name = "idx_transport_vehicle_type"),
    @TableIndex(fieldList = "month", name = "idx_transport_month")
})
public class Transport extends BaseEntity {
    private String vehicleType;
    private Integer vehicleCount;
    private BigDecimal totalDistance;
    private String month;
}
