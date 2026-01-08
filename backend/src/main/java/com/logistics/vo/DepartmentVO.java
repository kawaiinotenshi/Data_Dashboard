package com.logistics.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DepartmentVO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}