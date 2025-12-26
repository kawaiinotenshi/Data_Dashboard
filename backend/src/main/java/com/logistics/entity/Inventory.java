package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("inventory_ratio")
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String enterpriseName;
    private BigDecimal ratio;
    private String month;
    private LocalDateTime createdTime;
}
