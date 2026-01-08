package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.AuditLog;
import java.util.Collection;

/**
 * 审计日志Service接口
 */
public interface AuditLogService extends IService<AuditLog> {
    
    /**
     * 保存审计日志
     * @param auditLog 审计日志实体
     */
    void saveAuditLog(AuditLog auditLog);
    
    /**
     * 批量保存审计日志
     * @param auditLogs 审计日志列表
     */
    void batchSaveAuditLogs(Collection<AuditLog> auditLogs);
}