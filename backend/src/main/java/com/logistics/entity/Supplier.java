package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "supplier", indexes = {
    @TableIndex(fieldList = "name", name = "idx_supplier_name"),
    @TableIndex(fieldList = "phone", name = "idx_supplier_phone")
})
public class Supplier extends BaseEntity {
    private String name;
    private String contactPerson;
    private String phone;
    private String address;
}
