package com.logistics.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}