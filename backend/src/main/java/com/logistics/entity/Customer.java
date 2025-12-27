package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "customer", indexes = {
    @TableIndex(fieldList = "name", name = "idx_customer_name"),
    @TableIndex(fieldList = "phone", name = "idx_customer_phone")
})
public class Customer extends BaseEntity {
    private String name;
    private String contactPerson;
    private String phone;
    private String address;
}
