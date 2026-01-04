package com.logistics.config;

import com.logistics.common.Result;
import com.logistics.common.enums.ErrorCode;
import com.logistics.common.exception.BusinessException;
import com.logistics.common.exception.DuplicateResourceException;
import com.logistics.common.exception.ResourceNotFoundException;
import com.logistics.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 业务异常 [{}]: {}", traceId, request.getRequestURI(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<Void> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 资源不存在 [{}]: {}", traceId, request.getRequestURI(), e.getMessage());
        return Result.error(ErrorCode.NOT_FOUND.getCode(), e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public Result<Void> handleValidationException(ValidationException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 参数校验异常 [{}]: {}", traceId, request.getRequestURI(), e.getMessage());
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public Result<Void> handleDuplicateResourceException(DuplicateResourceException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 资源重复异常 [{}]: {}", traceId, request.getRequestURI(), e.getMessage());
        return Result.error(ErrorCode.CONFLICT.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("[TraceId: {}] 参数校验异常 [{}]: {}", traceId, request.getRequestURI(), message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("[TraceId: {}] 参数绑定异常 [{}]: {}", traceId, request.getRequestURI(), message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        String message = String.format("缺少必需参数: %s", e.getParameterName());
        log.warn("[TraceId: {}] 缺少参数异常 [{}]: {}", traceId, request.getRequestURI(), message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        String message = String.format("参数类型错误: %s", e.getName());
        log.warn("[TraceId: {}] 参数类型异常 [{}]: {}", traceId, request.getRequestURI(), message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 请求体读取异常 [{}]: {}", traceId, request.getRequestURI(), e.getMessage());
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), "请求体格式错误");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        String message = String.format("不支持的请求方法: %s", e.getMethod());
        log.warn("[TraceId: {}] 请求方法不支持 [{}]: {}", traceId, request.getRequestURI(), message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 请求路径不存在 [{}]: {}", traceId, request.getRequestURI(), e.getRequestURL());
        return Result.error(ErrorCode.NOT_FOUND.getCode(), "请求路径不存在");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 数据库唯一键冲突 [{}]: {}", traceId, request.getRequestURI(), e.getMessage());
        return Result.error(ErrorCode.CONFLICT.getCode(), "数据重复，请检查唯一字段");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.error("[TraceId: {}] 数据完整性约束异常 [{}]: {}", traceId, request.getRequestURI(), e.getMessage(), e);
        return Result.error(ErrorCode.DATABASE_ERROR.getCode(), "数据完整性约束异常");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.error("[TraceId: {}] 系统异常 [{}]: {}", traceId, request.getRequestURI(), e.getMessage(), e);
        return Result.error(ErrorCode.INTERNAL_ERROR.getCode(), "系统异常：" + e.getMessage());
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
