package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.BusinessQuarterly;

import java.math.BigDecimal;

/**
 * 季度业务数据服务接口
 */
public interface BusinessQuarterlyService extends IService<BusinessQuarterly> {

    /**
     * 更新季度业务金额
     * @param year 年份
     * @param quarter 季度
     * @param businessType 业务类型
     * @param amount 新增金额
     * @return 是否更新成功
     */
    boolean updateQuarterlyAmount(Integer year, Integer quarter, String businessType, BigDecimal amount);

    /**
     * 根据年份和季度获取业务数据
     * @param year 年份
     * @param quarter 季度
     * @return 季度业务数据列表
     */
    java.util.List<BusinessQuarterly> getBusinessQuarterlyByYearAndQuarter(Integer year, Integer quarter);
}