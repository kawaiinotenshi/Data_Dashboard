package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    
    Map<String, Object> selectCustomerStatistics();
    
    List<Customer> selectCustomersByName(@Param("name") String name);
    
    List<Customer> selectCustomersByAddress(@Param("address") String address);
    
    List<Map<String, Object>> selectCustomerOrderCount();
}
