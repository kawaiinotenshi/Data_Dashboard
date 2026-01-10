package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVO {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Long supplierId;
    private String unit;
    private Integer safetyStock;
    private Integer status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}