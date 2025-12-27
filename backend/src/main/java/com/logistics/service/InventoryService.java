package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Inventory;
import com.logistics.vo.InventoryRequestVO;
import com.logistics.vo.InventoryVO;

import java.util.List;
import java.util.Map;

public interface InventoryService extends IService<Inventory> {
    List<Inventory> getAllInventory();
    List<InventoryVO> getAllInventoryVOs();
    List<Inventory> getInventoryByWarehouse(Long warehouseId);
    List<InventoryVO> getInventoryVOsByWarehouse(Long warehouseId);
    InventoryVO getInventoryById(Long id);
    boolean createInventory(InventoryRequestVO inventoryRequestVO);
    boolean updateInventory(Long id, InventoryRequestVO inventoryRequestVO);
    boolean deleteInventory(Long id);
    boolean batchDeleteInventories(List<Long> ids);
    
    boolean batchCreateInventories(List<InventoryRequestVO> inventoryRequestVOs);
    boolean batchUpdateInventories(List<Inventory> inventories);
    
    Map<String, Object> getInventoryStatistics();
    List<InventoryVO> getInventoryByMonth(String month);
    List<Map<String, Object>> getInventoryByEnterpriseRanking();
    List<Map<String, Object>> getInventoryTrend();
}
