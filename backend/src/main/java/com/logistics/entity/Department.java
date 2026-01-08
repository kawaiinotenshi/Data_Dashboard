package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;

/**
 * 部门实体类
 */
@Data
@TableName("department")
public class Department extends BaseEntity {
    private String name;         // 部门名称
    private String description;  // 部门描述
}