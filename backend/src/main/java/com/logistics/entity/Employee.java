package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

/**
 * 员工实体类
 */
@Data
@TableName("employee")
public class Employee extends BaseEntity {
    private String name;            // 员工姓名
    private Long departmentId;      // 部门ID
    private Double salary;          // 工资
    private LocalDate hireDate;     // 入职日期

    // 关联的部门对象（用于关联查询，非数据库字段）
    @TableField(exist = false)
    private Department department;
}