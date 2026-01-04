package com.logistics.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerVO {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String address;
    private String email;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
