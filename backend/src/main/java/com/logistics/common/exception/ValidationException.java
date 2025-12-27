package com.logistics.common.exception;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(400, message);
    }

    public ValidationException(String field, String message) {
        super(400, field + ": " + message);
    }
}
