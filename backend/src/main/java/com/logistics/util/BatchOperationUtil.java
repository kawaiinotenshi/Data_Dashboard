package com.logistics.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class BatchOperationUtil {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    public static <T> void batchInsert(BaseMapper<T> mapper, List<T> entities) {
        batchInsert(mapper, entities, DEFAULT_BATCH_SIZE);
    }

    @Transactional(rollbackFor = Exception.class)
    public static <T> void batchInsert(BaseMapper<T> mapper, List<T> entities, int batchSize) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        int total = entities.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<T> batch = entities.subList(i, end);
            
            for (T entity : batch) {
                mapper.insert(entity);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public static <T> void batchUpdate(BaseMapper<T> mapper, List<T> entities) {
        batchUpdate(mapper, entities, DEFAULT_BATCH_SIZE);
    }

    @Transactional(rollbackFor = Exception.class)
    public static <T> void batchUpdate(BaseMapper<T> mapper, List<T> entities, int batchSize) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        int total = entities.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<T> batch = entities.subList(i, end);
            
            for (T entity : batch) {
                mapper.updateById(entity);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public static <T> void batchDelete(BaseMapper<T> mapper, List<Long> ids) {
        batchDelete(mapper, ids, DEFAULT_BATCH_SIZE);
    }

    @Transactional(rollbackFor = Exception.class)
    public static <T> void batchDelete(BaseMapper<T> mapper, List<Long> ids, int batchSize) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        int total = ids.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<Long> batch = ids.subList(i, end);
            
            for (Long id : batch) {
                mapper.deleteById(id);
            }
        }
    }

    public static <T> List<T> queryInBatches(BaseMapper<T> mapper, LambdaQueryWrapper<T> wrapper, int batchSize) {
        List<T> result = new ArrayList<>();
        int offset = 0;
        
        while (true) {
            wrapper.last("LIMIT " + batchSize + " OFFSET " + offset);
            List<T> batch = mapper.selectList(wrapper);
            
            if (batch.isEmpty()) {
                break;
            }
            
            result.addAll(batch);
            
            if (batch.size() < batchSize) {
                break;
            }
            
            offset += batchSize;
        }
        
        return result;
    }
}
