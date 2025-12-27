package com.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderRequestVO {
    private String orderNo;
    private String customerName;
    private BigDecimal orderAmount;
    private String status;
}
