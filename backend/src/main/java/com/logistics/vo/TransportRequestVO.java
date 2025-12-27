package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransportRequestVO {
    private String vehicleType;
    private Integer vehicleCount;
    private BigDecimal totalDistance;
    private String month;
}
