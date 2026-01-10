package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    List<Product> selectProductsByName(@Param("name") String name);
    
    List<Product> selectProductsByCategory(@Param("category") String category);
    
    List<Product> selectProductsBySupplier(@Param("supplierId") Long supplierId);
}