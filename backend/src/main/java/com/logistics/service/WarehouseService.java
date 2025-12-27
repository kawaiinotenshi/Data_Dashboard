package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Warehouse;
import com.logistics.vo.WarehouseRequestVO;
import com.logistics.vo.WarehouseVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WarehouseService extends IService<Warehouse> {
    List<Warehouse> getAllWarehouses();
    List<WarehouseVO> getAllWarehouseVOs();
    Warehouse getWarehouseById(Long id);
    WarehouseVO getWarehouseVOById(Long id);
    boolean createWarehouse(WarehouseRequestVO warehouseRequestVO);
    boolean updateWarehouse(Long id, WarehouseRequestVO warehouseRequestVO);
    boolean deleteWarehouse(Long id);
    boolean batchDeleteWarehouses(List<Long> ids);
    
    boolean batchCreateWarehouses(List<WarehouseRequestVO> warehouseRequestVOs);
    boolean batchUpdateWarehouses(List<Warehouse> warehouses);
    
    Map<String, Object> getWarehouseStatistics();
    List<WarehouseVO> getWarehousesByLocation(String location);
    List<WarehouseVO> getWarehousesByUtilizationRange(BigDecimal minRate, BigDecimal maxRate);
    List<Map<String, Object>> getWarehouseCapacityRanking();
}
