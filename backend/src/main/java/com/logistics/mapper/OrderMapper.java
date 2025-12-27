package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    Map<String, Object> selectOrderStatistics();
    
    List<Map<String, Object>> selectOrdersByStatusGroup();
    
    List<Map<String, Object>> selectOrdersByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    List<Order> selectOrdersByCustomerName(@Param("customerName") String customerName);
}
