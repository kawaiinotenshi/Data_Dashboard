package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransportVO {
    private Long id;
    private String vehicleType;
    private Integer vehicleCount;
    private BigDecimal totalDistance;
    private String month;
    private String status;
    private String vehicleNo;
    private String driver;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
