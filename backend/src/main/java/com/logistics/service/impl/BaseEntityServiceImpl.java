package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.common.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseEntityServiceImpl<M, T> extends ServiceImpl<M, T> {

    protected abstract String getEntityName();

    protected abstract Class<?> getVOClass();

    protected abstract Class<?> getRequestVOClass();

    public List<T> getAllEntities() {
        return list();
    }

    public <VO> List<VO> getAllEntityVOs(Function<T, VO> voMapper) {
        List<T> entities = list();
        return entities.stream().map(voMapper).collect(Collectors.toList());
    }

    public <VO> VO getEntityById(Long id, Function<T, VO> voMapper) {
        T entity = getById(id);
        if (entity == null) {
            return null;
        }
        return voMapper.apply(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public <RequestVO> boolean createEntity(RequestVO requestVO, Class<T> entityClass) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(requestVO, entity);
            return save(entity);
        } catch (Exception e) {
            throw new RuntimeException("创建" + getEntityName() + "失败", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <RequestVO> boolean updateEntity(Long id, RequestVO requestVO) {
        T entity = getById(id);
        if (entity == null) {
            throw new ResourceNotFoundException(getEntityName(), id);
        }
        BeanUtils.copyProperties(requestVO, entity);
        return updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEntity(Long id) {
        return removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteEntities(List<Long> ids) {
        return removeByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public <RequestVO> boolean batchCreateEntities(List<RequestVO> requestVOs, Class<T> entityClass) {
        if (requestVOs == null || requestVOs.isEmpty()) {
            return false;
        }
        try {
            List<T> entities = requestVOs.stream().map(vo -> {
                try {
                    T entity = entityClass.getDeclaredConstructor().newInstance();
                    BeanUtils.copyProperties(vo, entity);
                    return entity;
                } catch (Exception e) {
                    throw new RuntimeException("创建" + getEntityName() + "失败", e);
                }
            }).collect(Collectors.toList());
            return saveBatch(entities, 500);
        } catch (Exception e) {
            throw new RuntimeException("批量创建" + getEntityName() + "失败", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateEntities(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        return updateBatchById(entities, 500);
    }

    public <VO> List<VO> convertToVOList(List<T> entities, Function<T, VO> voMapper) {
        return entities.stream().map(voMapper).collect(Collectors.toList());
    }

    public <VO> VO convertToVO(T entity, Function<T, VO> voMapper) {
        if (entity == null) {
            return null;
        }
        return voMapper.apply(entity);
    }
}
