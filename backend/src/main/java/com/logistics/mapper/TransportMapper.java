package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Transport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface TransportMapper extends BaseMapper<Transport> {
    
    Map<String, Object> selectTransportStatistics();
    
    List<Transport> selectTransportByMonth(@Param("month") String month);
    
    List<Transport> selectTransportByVehicleType(@Param("vehicleType") String vehicleType);
    
    List<Map<String, Object>> selectTransportTrend();
    
    List<Map<String, Object>> selectTransportByVehicleTypeGroup();
}
