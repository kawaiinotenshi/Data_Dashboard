package com.logistics.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SqlPerformanceInterceptor implements Interceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlPerformanceInterceptor.class);
    
    private static final long SLOW_SQL_THRESHOLD = 1000;
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql();
        String sqlId = mappedStatement.getId();
        
        long startTime = System.currentTimeMillis();
        try {
            Object result = invocation.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            if (executionTime > SLOW_SQL_THRESHOLD) {
                logger.warn("慢SQL检测 - ID: {}, 执行时间: {}ms, SQL: {}", sqlId, executionTime, formatSql(sql));
            } else {
                logger.debug("SQL执行 - ID: {}, 执行时间: {}ms", sqlId, executionTime);
            }
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.error("SQL执行异常 - ID: {}, 执行时间: {}ms, SQL: {}, 异常: {}", 
                sqlId, executionTime, formatSql(sql), e.getMessage());
            throw e;
        }
    }
    
    private String formatSql(String sql) {
        if (sql == null) {
            return "";
        }
        return sql.replaceAll("\\s+", " ").trim();
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
    }
}
