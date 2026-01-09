package com.logistics.vo;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class TransportRequestVO {
    @NotBlank(message = "车辆类型不能为空")
    private String vehicleType;
    
    private Integer vehicleCount;
    
    @NotNull(message = "总距离不能为空")
    @DecimalMin(value = "0.0", message = "总距离必须大于等于0")
    private BigDecimal totalDistance;
    
    @NotBlank(message = "状态不能为空")
    private String status;
    
    @NotBlank(message = "月份不能为空")
    private String month;
}
