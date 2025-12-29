package com.logistics.vo;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class WarehouseRequestVO {
    
    @NotBlank(message = "仓库名称不能为空")
    @Size(max = 100, message = "仓库名称长度不能超过100个字符")
    private String name;
    
    @NotBlank(message = "仓库位置不能为空")
    @Size(max = 200, message = "仓库位置长度不能超过200个字符")
    private String location;
    
    @NotNull(message = "仓库面积不能为空")
    @DecimalMin(value = "0.0", message = "仓库面积必须大于等于0")
    @DecimalMax(value = "999999.99", message = "仓库面积不能超过999999.99")
    private BigDecimal area;
    
    @DecimalMin(value = "0.0", message = "利用率必须大于等于0")
    @DecimalMax(value = "100.0", message = "利用率不能超过100")
    private BigDecimal utilizationRate;
    
    @DecimalMin(value = "0.0", message = "容量必须大于等于0")
    @DecimalMax(value = "999999999.99", message = "容量不能超过999999999.99")
    private BigDecimal capacity;
    
    @DecimalMin(value = "0.0", message = "吞吐量必须大于等于0")
    @DecimalMax(value = "999999999.99", message = "吞吐量不能超过999999999.99")
    private BigDecimal throughput;
}
