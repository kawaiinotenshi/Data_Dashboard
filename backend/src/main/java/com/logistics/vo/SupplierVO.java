package com.logistics.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierVO {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String address;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
