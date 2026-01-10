package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.BusinessQuarterly;
import com.logistics.mapper.BusinessQuarterlyMapper;
import com.logistics.service.BusinessQuarterlyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BusinessQuarterlyServiceImpl extends BaseEntityServiceImpl<BusinessQuarterlyMapper, BusinessQuarterly> implements BusinessQuarterlyService {

    @Override
    public String getEntityName() {
        return "季度业务数据";
    }

    @Override
    public Class<?> getVOClass() {
        return BusinessQuarterly.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return BusinessQuarterly.class;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuarterlyAmount(Integer year, Integer quarter, String businessType, BigDecimal amount) {
        // 查找是否存在该季度业务数据
        BusinessQuarterly existing = baseMapper.selectByYearQuarterAndType(year, quarter, businessType);
        
        if (existing != null) {
            // 更新现有记录
            return baseMapper.updateAmountByYearQuarterAndType(year, quarter, businessType, amount) > 0;
        } else {
            // 创建新记录
            BusinessQuarterly newQuarterly = new BusinessQuarterly();
            newQuarterly.setYear(year);
            newQuarterly.setQuarter(quarter);
            newQuarterly.setBusinessType(businessType);
            newQuarterly.setAmount(amount);
            newQuarterly.setGrowthRate(new BigDecimal(0)); // 默认增长率为0
            
            return save(newQuarterly);
        }
    }

    @Override
    public List<BusinessQuarterly> getBusinessQuarterlyByYearAndQuarter(Integer year, Integer quarter) {
        QueryWrapper<BusinessQuarterly> wrapper = new QueryWrapper<>();
        wrapper.eq("year", year);
        wrapper.eq("quarter", quarter);
        wrapper.eq("is_deleted", 0);
        
        return list(wrapper);
    }
}