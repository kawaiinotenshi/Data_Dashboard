package com.logistics.vo;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeRequestVO {
    private String name;
    private Long departmentId;
    private Double salary;
    private LocalDate hireDate;
}