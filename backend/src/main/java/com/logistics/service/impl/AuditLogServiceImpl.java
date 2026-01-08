package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.AuditLog;
import com.logistics.mapper.AuditLogMapper;
import com.logistics.service.AuditLogService;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * 审计日志Service实现类
 */
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {
    
    @Override
    public void saveAuditLog(AuditLog auditLog) {
        this.save(auditLog);
    }
    
    @Override
    public void batchSaveAuditLogs(Collection<AuditLog> auditLogs) {
        this.saveBatch(auditLogs);
    }
}