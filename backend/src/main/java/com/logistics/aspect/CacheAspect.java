package com.logistics.aspect;

import com.logistics.annotation.CacheExpire;
import com.logistics.util.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class CacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);

    @Autowired
    private RedisUtil redisUtil;

    @Around("@annotation(com.logistics.annotation.CacheExpire)")
    public Object aroundCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheExpire cacheExpire = method.getAnnotation(CacheExpire.class);

        String key = buildCacheKey(cacheExpire, method, joinPoint.getArgs());

        Object cachedValue = redisUtil.get(key);
        if (cachedValue != null) {
            logger.debug("Cache hit for key: {}", key);
            return cachedValue;
        }

        logger.debug("Cache miss for key: {}", key);
        Object result = joinPoint.proceed();

        if (result != null) {
            redisUtil.set(key, result, cacheExpire.value(), cacheExpire.timeUnit());
            logger.debug("Cached result for key: {} with TTL: {} {}", 
                key, cacheExpire.value(), cacheExpire.timeUnit());
        }

        return result;
    }

    private String buildCacheKey(CacheExpire cacheExpire, Method method, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder();

        if (!cacheExpire.keyPrefix().isEmpty()) {
            keyBuilder.append(cacheExpire.keyPrefix());
        } else {
            keyBuilder.append("cache:");
        }

        if (!cacheExpire.key().isEmpty()) {
            keyBuilder.append(cacheExpire.key());
        } else {
            keyBuilder.append(method.getDeclaringClass().getSimpleName())
                      .append(":")
                      .append(method.getName());
        }

        if (args != null && args.length > 0) {
            keyBuilder.append(":");
            for (Object arg : args) {
                if (arg != null) {
                    keyBuilder.append(arg.toString()).append(",");
                }
            }
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }

        return keyBuilder.toString();
    }
}
