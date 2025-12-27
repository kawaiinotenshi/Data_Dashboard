package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private String customerName;
    private BigDecimal orderAmount;
    private LocalDateTime orderDate;
    private String status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
