package com.logistics.service;

import com.logistics.vo.InventoryAlertVO;

import java.util.List;
import java.util.Map;

/**
 * 库存监控服务接口
 */
public interface InventoryMonitorService {
    
    /**
     * 检查所有仓库的库存水位
     * @return 库存预警列表
     */
    List<InventoryAlertVO> checkInventoryLevels();
    
    /**
     * 检查指定仓库的库存水位
     * @param warehouseId 仓库ID
     * @return 库存预警信息
     */
    InventoryAlertVO checkInventoryLevelByWarehouse(Long warehouseId);
    
    /**
     * 获取库存预警配置
     * @return 预警配置（包括低库存阈值、高库存阈值等）
     */
    Map<String, Object> getAlertConfig();
    
    /**
     * 更新库存预警配置
     * @param config 预警配置
     */
    void updateAlertConfig(Map<String, Object> config);
    
    /**
     * 生成库存水位报告
     * @return 库存水位报告
     */
    Map<String, Object> generateInventoryReport();
}