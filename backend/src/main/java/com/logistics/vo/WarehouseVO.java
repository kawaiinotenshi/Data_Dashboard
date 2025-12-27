package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WarehouseVO {
    private Long id;
    private String name;
    private String location;
    private BigDecimal area;
    private BigDecimal utilizationRate;
    private BigDecimal capacity;
    private BigDecimal throughput;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
