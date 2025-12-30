package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    
    Map<String, Object> selectInventoryStatistics();
    
    List<Inventory> selectInventoryByMonth(@Param("month") String month);
    
    List<Map<String, Object>> selectInventoryByEnterpriseRanking();
    
    List<Map<String, Object>> selectInventoryByEnterpriseRatio();
    
    List<Map<String, Object>> selectInventoryTrend();
}
