package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {
    
    Map<String, Object> selectWarehouseStatistics();
    
    List<Warehouse> selectWarehousesByLocation(@Param("location") String location);
    
    List<Warehouse> selectWarehousesByUtilizationRange(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);
    
    List<Map<String, Object>> selectWarehouseCapacityRanking();
}
