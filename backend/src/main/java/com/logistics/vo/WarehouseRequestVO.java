package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WarehouseRequestVO {
    private String name;
    private String location;
    private BigDecimal area;
    private BigDecimal utilizationRate;
    private BigDecimal capacity;
    private BigDecimal throughput;
}
