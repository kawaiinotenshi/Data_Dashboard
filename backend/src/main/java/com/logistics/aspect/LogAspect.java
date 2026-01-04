package com.logistics.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final long SLOW_REQUEST_THRESHOLD = 3000;
    private static final int MAX_PARAM_LENGTH = 1000;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(public * com.logistics.controller..*.*(..))")
    public void controllerPointcut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void mappingPointcut() {
    }

    @Around("controllerPointcut() && mappingPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String requestURI = request != null ? request.getRequestURI() : "unknown";
        String method = request != null ? request.getMethod() : "unknown";
        String clientIp = getClientIp(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : "unknown";

        log.info("========== 请求开始 ==========");
        log.info("请求地址: {} {}", method, requestURI);
        log.info("类名: {}.{}", className, methodName);
        log.info("客户端IP: {}", clientIp);
        log.info("User-Agent: {}", userAgent);

        if (request != null) {
            logRequestHeaders(request);
            logRequestParams(request);
        }

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            log.info("请求参数: {}", formatArgs(args));
        }

        Object result = null;
        Throwable exception = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("========== 请求结束 ==========");
            log.info("执行时间: {} ms", executionTime);

            if (executionTime > SLOW_REQUEST_THRESHOLD) {
                log.warn("⚠️ 慢请求警告: 请求执行时间超过 {} ms", SLOW_REQUEST_THRESHOLD);
            }

            if (exception != null) {
                log.error("请求异常: {}", exception.getMessage(), exception);
            } else {
                log.info("响应结果: {}", formatResult(result));
            }

            log.info("========================================");
        }
    }

    private void logRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (!"authorization".equalsIgnoreCase(headerName)) {
                headers.put(headerName, headerValue);
            }
        }
        if (!headers.isEmpty()) {
            log.info("请求头: {}", headers);
        }
    }

    private void logRequestParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            Map<String, String> formattedParams = new HashMap<>();
            params.forEach((key, values) -> {
                String value = values.length > 0 ? values[0] : "";
                if (value.length() > MAX_PARAM_LENGTH) {
                    value = value.substring(0, MAX_PARAM_LENGTH) + "...";
                }
                formattedParams.put(key, value);
            });
            log.info("查询参数: {}", formattedParams);
        }
    }

    private String formatArgs(Object[] args) {
        try {
            String json = objectMapper.writeValueAsString(args);
            if (json.length() > MAX_PARAM_LENGTH) {
                return json.substring(0, MAX_PARAM_LENGTH) + "...";
            }
            return json;
        } catch (Exception e) {
            return Arrays.toString(args);
        }
    }

    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }
        try {
            String json = objectMapper.writeValueAsString(result);
            if (json.length() > MAX_PARAM_LENGTH) {
                return json.substring(0, MAX_PARAM_LENGTH) + "...";
            }
            return json;
        } catch (Exception e) {
            return result.toString();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip != null ? ip : "unknown";
    }
}
