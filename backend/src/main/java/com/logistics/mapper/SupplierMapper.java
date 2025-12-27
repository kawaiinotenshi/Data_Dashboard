package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    
    Map<String, Object> selectSupplierStatistics();
    
    List<Supplier> selectSuppliersByName(@Param("name") String name);
    
    List<Supplier> selectSuppliersByAddress(@Param("address") String address);
    
    List<Map<String, Object>> selectSupplierByContactRanking();
}
