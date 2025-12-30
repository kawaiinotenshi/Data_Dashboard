package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class InventoryServiceImpl extends BaseEntityServiceImpl<InventoryMapper, Inventory> implements InventoryService {
    @Override
    public String getEntityName() {
        return "库存记录";
    }

    @Override
    public Class<?> getVOClass() {
        return InventoryVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return InventoryRequestVO.class;
    }

    @Override
    public List<Inventory> getAllInventory() {
        return getAllEntities();
    }

    @Override
    public List<InventoryVO> getAllInventoryVOs() {
        return getAllEntityVOs(this::convertToInventoryVO);
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
        return convertToVOList(inventories, this::convertToInventoryVO);
    }

    @Override
    public InventoryVO getInventoryById(Long id) {
        return getEntityById(id, this::convertToInventoryVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInventory(InventoryRequestVO inventoryRequestVO) {
        return createEntity(inventoryRequestVO, Inventory.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInventory(Long id, InventoryRequestVO inventoryRequestVO) {
        return updateEntity(id, inventoryRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventory(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInventories(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateInventories(List<InventoryRequestVO> inventoryRequestVOs) {
        return batchCreateEntities(inventoryRequestVOs, Inventory.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateInventories(List<Inventory> inventories) {
        return batchUpdateEntities(inventories);
    }

    @Override
    public Map<String, Object> getInventoryStatistics() {
        return baseMapper.selectInventoryStatistics();
    }

    @Override
    public List<InventoryVO> getInventoryByMonth(String month) {
        List<Inventory> inventories = baseMapper.selectInventoryByMonth(month);
        return convertToVOList(inventories, this::convertToInventoryVO);
    }

    @Override
    public List<Map<String, Object>> getInventoryByEnterpriseRanking() {
        return baseMapper.selectInventoryByEnterpriseRanking();
    }

    @Override
    public List<Map<String, Object>> getInventoryByEnterpriseRatio() {
        return baseMapper.selectInventoryByEnterpriseRatio();
    }

    @Override
    public List<Map<String, Object>> getInventoryTrend() {
        return baseMapper.selectInventoryTrend();
    }

    private InventoryVO convertToInventoryVO(Inventory inventory) {
        InventoryVO inventoryVO = new InventoryVO();
        BeanUtils.copyProperties(inventory, inventoryVO);
        return inventoryVO;
    }
}
