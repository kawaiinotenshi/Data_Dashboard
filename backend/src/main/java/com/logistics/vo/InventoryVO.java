package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryVO {
    private Long id;
    private String enterpriseName;
    private BigDecimal ratio;
    private String month;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
