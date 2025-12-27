package com.logistics.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),
    
    INTERNAL_ERROR(500, "系统内部错误"),
    DATABASE_ERROR(5001, "数据库操作失败"),
    VALIDATION_ERROR(5002, "数据校验失败"),
    DUPLICATE_ERROR(5003, "数据重复"),
    
    WAREHOUSE_NOT_FOUND(1001, "仓库不存在"),
    INVENTORY_NOT_FOUND(1002, "库存记录不存在"),
    ORDER_NOT_FOUND(1003, "订单不存在"),
    CUSTOMER_NOT_FOUND(1004, "客户不存在"),
    SUPPLIER_NOT_FOUND(1005, "供应商不存在"),
    TRANSPORT_NOT_FOUND(1006, "运输记录不存在"),
    
    WAREHOUSE_NAME_DUPLICATE(2001, "仓库名称已存在"),
    CUSTOMER_EMAIL_DUPLICATE(2002, "客户邮箱已存在"),
    SUPPLIER_EMAIL_DUPLICATE(2003, "供应商邮箱已存在"),
    
    INSUFFICIENT_INVENTORY(3001, "库存不足"),
    WAREHOUSE_CAPACITY_EXCEEDED(3002, "仓库容量不足");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
