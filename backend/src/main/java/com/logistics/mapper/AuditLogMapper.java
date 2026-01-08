package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审计日志Mapper接口
 */
@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {
    // 继承BaseMapper，提供基本的CRUD操作
}