package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.common.exception.ResourceNotFoundException;
import com.logistics.entity.Warehouse;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.service.WarehouseService;
import com.logistics.vo.WarehouseRequestVO;
import com.logistics.vo.WarehouseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {
    @Override
    public List<Warehouse> getAllWarehouses() {
        return list();
    }

    @Override
    public List<WarehouseVO> getAllWarehouseVOs() {
        List<Warehouse> warehouses = list();
        return warehouses.stream().map(warehouse -> {
            WarehouseVO warehouseVO = new WarehouseVO();
            BeanUtils.copyProperties(warehouse, warehouseVO);
            return warehouseVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return getById(id);
    }

    @Override
    public WarehouseVO getWarehouseVOById(Long id) {
        Warehouse warehouse = getById(id);
        if (warehouse == null) {
            return null;
        }
        WarehouseVO warehouseVO = new WarehouseVO();
        BeanUtils.copyProperties(warehouse, warehouseVO);
        return warehouseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createWarehouse(WarehouseRequestVO warehouseRequestVO) {
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(warehouseRequestVO, warehouse);
        return save(warehouse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWarehouse(Long id, WarehouseRequestVO warehouseRequestVO) {
        Warehouse warehouse = getById(id);
        if (warehouse == null) {
            throw new ResourceNotFoundException("仓库", id);
        }
        BeanUtils.copyProperties(warehouseRequestVO, warehouse);
        return updateById(warehouse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWarehouse(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteWarehouses(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateWarehouses(List<WarehouseRequestVO> warehouseRequestVOs) {
        if (warehouseRequestVOs == null || warehouseRequestVOs.isEmpty()) {
            return false;
        }
        List<Warehouse> warehouses = warehouseRequestVOs.stream().map(vo -> {
            Warehouse warehouse = new Warehouse();
            BeanUtils.copyProperties(vo, warehouse);
            return warehouse;
        }).collect(java.util.stream.Collectors.toList());
        return saveBatch(warehouses, 500);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateWarehouses(List<Warehouse> warehouses) {
        if (warehouses == null || warehouses.isEmpty()) {
            return false;
        }
        return updateBatchById(warehouses, 500);
    }

    @Override
    public Map<String, Object> getWarehouseStatistics() {
        return baseMapper.selectWarehouseStatistics();
    }

    @Override
    public List<WarehouseVO> getWarehousesByLocation(String location) {
        List<Warehouse> warehouses = baseMapper.selectWarehousesByLocation(location);
        return warehouses.stream().map(warehouse -> {
            WarehouseVO warehouseVO = new WarehouseVO();
            BeanUtils.copyProperties(warehouse, warehouseVO);
            return warehouseVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<WarehouseVO> getWarehousesByUtilizationRange(BigDecimal minRate, BigDecimal maxRate) {
        List<Warehouse> warehouses = baseMapper.selectWarehousesByUtilizationRange(minRate, maxRate);
        return warehouses.stream().map(warehouse -> {
            WarehouseVO warehouseVO = new WarehouseVO();
            BeanUtils.copyProperties(warehouse, warehouseVO);
            return warehouseVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getWarehouseCapacityRanking() {
        return baseMapper.selectWarehouseCapacityRanking();
    }
}
