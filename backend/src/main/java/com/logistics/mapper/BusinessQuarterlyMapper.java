package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.BusinessQuarterly;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 季度业务数据Mapper
 */
@Mapper
public interface BusinessQuarterlyMapper extends BaseMapper<BusinessQuarterly> {

    /**
     * 根据年份、季度和业务类型获取记录
     */
    BusinessQuarterly selectByYearQuarterAndType(@Param("year") Integer year, 
                                               @Param("quarter") Integer quarter, 
                                               @Param("businessType") String businessType);

    /**
     * 更新季度业务金额
     */
    int updateAmountByYearQuarterAndType(@Param("year") Integer year, 
                                       @Param("quarter") Integer quarter, 
                                       @Param("businessType") String businessType, 
                                       @Param("amount") java.math.BigDecimal amount);
}