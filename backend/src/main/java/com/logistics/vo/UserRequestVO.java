package com.logistics.vo;

import lombok.Data;

@Data
public class UserRequestVO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String status;
}