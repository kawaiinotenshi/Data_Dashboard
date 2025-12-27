package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryRequestVO {
    private String enterpriseName;
    private BigDecimal ratio;
    private String month;
}
