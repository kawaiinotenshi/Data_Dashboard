package com.logistics.common.exception;

public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String message) {
        super(409, message);
    }

    public DuplicateResourceException(String resourceName, String field, String value) {
        super(409, resourceName + "已存在，" + field + ": " + value);
    }
}
