package com.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.logistics.entity.base.BaseEntity;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User extends BaseEntity {
    private String username;        // 用户名
    private String password;        // 密码
    private String email;           // 邮箱
    private String phone;           // 手机号
    private String role;            // 角色 (admin/user)
    private String status;          // 状态 (active/inactive)
}