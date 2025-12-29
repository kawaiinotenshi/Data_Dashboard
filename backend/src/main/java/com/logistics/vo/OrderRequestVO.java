package com.logistics.vo;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class OrderRequestVO {
    
    @NotBlank(message = "订单号不能为空")
    @Size(max = 50, message = "订单号长度不能超过50个字符")
    private String orderNo;
    
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 100, message = "客户名称长度不能超过100个字符")
    private String customerName;
    
    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.0", message = "订单金额必须大于等于0")
    @DecimalMax(value = "999999999.99", message = "订单金额不能超过999999999.99")
    private BigDecimal orderAmount;
    
    @NotBlank(message = "订单状态不能为空")
    @Size(max = 20, message = "订单状态长度不能超过20个字符")
    @Pattern(regexp = "^(pending|processing|shipped|delivered|cancelled)$", message = "订单状态必须是pending、processing、shipped、delivered或cancelled之一")
    private String status;
}
