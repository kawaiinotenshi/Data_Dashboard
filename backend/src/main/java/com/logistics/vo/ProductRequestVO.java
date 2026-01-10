package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestVO {
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Long supplierId;
    private String unit;
    private Integer safetyStock;
    private Integer status;
}