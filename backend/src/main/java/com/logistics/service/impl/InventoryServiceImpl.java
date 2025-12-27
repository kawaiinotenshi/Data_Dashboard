package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Inventory;
import com.logistics.mapper.InventoryMapper;
import com.logistics.service.InventoryService;
import com.logistics.vo.InventoryRequestVO;
import com.logistics.vo.InventoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {
    @Override
    public List<Inventory> getAllInventory() {
        return list();
    }

    @Override
    public List<InventoryVO> getAllInventoryVOs() {
        List<Inventory> inventories = list();
        return inventories.stream().map(inventory -> {
            InventoryVO inventoryVO = new InventoryVO();
            BeanUtils.copyProperties(inventory, inventoryVO);
            return inventoryVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("warehouse_id", warehouseId);
        return list(wrapper);
    }

    @Override
    public List<InventoryVO> getInventoryVOsByWarehouse(Long warehouseId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("warehouse_id", warehouseId);
        List<Inventory> inventories = list(wrapper);
        return inventories.stream().map(inventory -> {
            InventoryVO inventoryVO = new InventoryVO();
            BeanUtils.copyProperties(inventory, inventoryVO);
            return inventoryVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public InventoryVO getInventoryById(Long id) {
        Inventory inventory = getById(id);
        if (inventory == null) {
            return null;
        }
        InventoryVO inventoryVO = new InventoryVO();
        BeanUtils.copyProperties(inventory, inventoryVO);
        return inventoryVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInventory(InventoryRequestVO inventoryRequestVO) {
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryRequestVO, inventory);
        return save(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInventory(Long id, InventoryRequestVO inventoryRequestVO) {
        Inventory inventory = getById(id);
        if (inventory == null) {
            throw new ResourceNotFoundException("库存记录", id);
        }
        BeanUtils.copyProperties(inventoryRequestVO, inventory);
        return updateById(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventory(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInventories(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateInventories(List<InventoryRequestVO> inventoryRequestVOs) {
        if (inventoryRequestVOs == null || inventoryRequestVOs.isEmpty()) {
            return false;
        }
        List<Inventory> inventories = inventoryRequestVOs.stream().map(vo -> {
            Inventory inventory = new Inventory();
            BeanUtils.copyProperties(vo, inventory);
            return inventory;
        }).collect(java.util.stream.Collectors.toList());
        return saveBatch(inventories, 500);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateInventories(List<Inventory> inventories) {
        if (inventories == null || inventories.isEmpty()) {
            return false;
        }
        return updateBatchById(inventories, 500);
    }

    @Override
    public Map<String, Object> getInventoryStatistics() {
        return baseMapper.selectInventoryStatistics();
    }

    @Override
    public List<InventoryVO> getInventoryByMonth(String month) {
        List<Inventory> inventories = baseMapper.selectInventoryByMonth(month);
        return inventories.stream().map(inventory -> {
            InventoryVO inventoryVO = new InventoryVO();
            BeanUtils.copyProperties(inventory, inventoryVO);
            return inventoryVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getInventoryByEnterpriseRanking() {
        return baseMapper.selectInventoryByEnterpriseRanking();
    }

    @Override
    public List<Map<String, Object>> getInventoryTrend() {
        return baseMapper.selectInventoryTrend();
    }
}
