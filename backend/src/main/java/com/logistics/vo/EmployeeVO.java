package com.logistics.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeVO {
    private Long id;
    private String name;
    private Long departmentId;
    private String departmentName;
    private Double salary;
    private LocalDate hireDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}