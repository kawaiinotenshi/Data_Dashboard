package com.logistics.service.impl;

import com.logistics.annotation.CacheExpire;
import com.logistics.entity.Warehouse;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.service.WarehouseService;
import com.logistics.vo.WarehouseRequestVO;
import com.logistics.vo.WarehouseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WarehouseServiceImpl extends BaseEntityServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {
    @Override
    public String getEntityName() {
        return "仓库";
    }

    @Override
    public Class<?> getVOClass() {
        return WarehouseVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return WarehouseRequestVO.class;
    }

    @Override
    @CacheExpire(value = 10, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse")
    public List<Warehouse> getAllWarehouses() {
        return getAllEntities();
    }

    @Override
    @CacheExpire(value = 10, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse")
    public List<WarehouseVO> getAllWarehouseVOs() {
        return getAllEntityVOs(this::convertToWarehouseVO);
    }

    @Override
    @CacheExpire(value = 10, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse")
    public Warehouse getWarehouseById(Long id) {
        return getById(id);
    }

    @Override
    @CacheExpire(value = 10, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse")
    public WarehouseVO getWarehouseVOById(Long id) {
        return getEntityById(id, this::convertToWarehouseVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "warehouse", allEntries = true)
    public boolean createWarehouse(WarehouseRequestVO warehouseRequestVO) {
        return createEntity(warehouseRequestVO, Warehouse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "warehouse", allEntries = true)
    public boolean updateWarehouse(Long id, WarehouseRequestVO warehouseRequestVO) {
        return updateEntity(id, warehouseRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "warehouse", allEntries = true)
    public boolean deleteWarehouse(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "warehouse", allEntries = true)
    public boolean batchDeleteWarehouses(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "warehouse", allEntries = true)
    public boolean batchCreateWarehouses(List<WarehouseRequestVO> warehouseRequestVOs) {
        return batchCreateEntities(warehouseRequestVOs, Warehouse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "warehouse", allEntries = true)
    public boolean batchUpdateWarehouses(List<Warehouse> warehouses) {
        return batchUpdateEntities(warehouses);
    }

    @Override
    @CacheExpire(value = 5, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse:stats")
    public Map<String, Object> getWarehouseStatistics() {
        return baseMapper.selectWarehouseStatistics();
    }

    @Override
    @CacheExpire(value = 10, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse:location")
    public List<WarehouseVO> getWarehousesByLocation(String location) {
        List<Warehouse> warehouses = baseMapper.selectWarehousesByLocation(location);
        return convertToVOList(warehouses, this::convertToWarehouseVO);
    }

    @Override
    @CacheExpire(value = 10, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse:utilization")
    public List<WarehouseVO> getWarehousesByUtilizationRange(BigDecimal minRate, BigDecimal maxRate) {
        List<Warehouse> warehouses = baseMapper.selectWarehousesByUtilizationRange(minRate, maxRate);
        return convertToVOList(warehouses, this::convertToWarehouseVO);
    }

    @Override
    @CacheExpire(value = 5, timeUnit = TimeUnit.MINUTES, keyPrefix = "warehouse:ranking")
    public List<Map<String, Object>> getWarehouseCapacityRanking() {
        return baseMapper.selectWarehouseCapacityRanking();
    }

    private WarehouseVO convertToWarehouseVO(Warehouse warehouse) {
        WarehouseVO warehouseVO = new WarehouseVO();
        BeanUtils.copyProperties(warehouse, warehouseVO);
        return warehouseVO;
    }
}
