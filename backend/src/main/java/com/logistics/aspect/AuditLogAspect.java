package com.logistics.aspect;

import com.alibaba.fastjson.JSON;
import com.logistics.entity.AuditLog;
import com.logistics.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 审计日志切面类
 */
@Aspect
@Component
@Order(1)
public class AuditLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    /**
     * 定义切点：匹配所有Controller层的public方法
     */
    @Pointcut("execution(public * com.logistics.controller.*.*(..))")
    public void auditLogPointCut() {
    }

    /**
     * 环绕通知：记录完整的操作信息
     */
    @Around("auditLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        String exceptionMessage = null;
        String operationResult = "SUCCESS";

        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
            operationResult = "FAILED";
            throw e;
        } finally {
            long executeTime = System.currentTimeMillis() - startTime;
            final String finalExceptionMessage = exceptionMessage;
            final String finalOperationResult = operationResult;
            final ProceedingJoinPoint finalJoinPoint = joinPoint;
            // 异步保存审计日志
            CompletableFuture.runAsync(() -> {
                saveAuditLog(finalJoinPoint, finalExceptionMessage, finalOperationResult, executeTime);
            });
        }

        return result;
    }

    /**
     * 保存审计日志
     */
    private void saveAuditLog(JoinPoint joinPoint, String exceptionMessage, String result, long executeTime) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        AuditLog auditLog = new AuditLog();

        // 获取请求信息
        auditLog.setRequestUrl(request.getRequestURI());
        auditLog.setRequestMethod(request.getMethod());
        auditLog.setRequestIp(getIpAddress(request));

        // 获取用户信息（此处需要根据实际情况获取，暂时模拟）
        // 实际项目中可以从Token、Session或ThreadLocal中获取
        auditLog.setUserId(1L);
        auditLog.setUsername("admin");

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取模块名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String module = className.replace("Controller", "");
        auditLog.setModule(module);

        // 获取操作类型
        String operationType = getOperationType(method.getName());
        auditLog.setOperationType(operationType);

        // 获取操作内容
        String operationContent = String.format("%s %s: %s", 
                module, operationType, JSON.toJSONString(joinPoint.getArgs()));
        auditLog.setOperationContent(operationContent);

        // 设置操作时间和结果
        auditLog.setOperationTime(LocalDateTime.now());
        auditLog.setResult(result);
        auditLog.setErrorMessage(exceptionMessage);
        auditLog.setExecuteTime(executeTime);

        // 保存审计日志
        auditLogService.saveAuditLog(auditLog);
    }

    /**
     * 根据方法名获取操作类型
     */
    private String getOperationType(String methodName) {
        if (methodName.startsWith("add") || methodName.startsWith("create") || methodName.startsWith("insert")) {
            return "新增";
        } else if (methodName.startsWith("update") || methodName.startsWith("edit")) {
            return "修改";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "删除";
        } else if (methodName.startsWith("get") || methodName.startsWith("find") || methodName.startsWith("select") || methodName.startsWith("query")) {
            return "查询";
        } else if (methodName.startsWith("list")) {
            return "列表查询";
        } else if (methodName.startsWith("batch")) {
            return "批量操作";
        } else {
            return "其他操作";
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个非unknown的IP
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!"unknown".equalsIgnoreCase(s)) {
                    ip = s.trim();
                    break;
                }
            }
        }
        return ip;
    }
}