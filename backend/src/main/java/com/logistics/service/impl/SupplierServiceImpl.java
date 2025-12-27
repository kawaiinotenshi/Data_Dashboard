package com.logistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Supplier;
import com.logistics.mapper.SupplierMapper;
import com.logistics.service.SupplierService;
import com.logistics.vo.SupplierRequestVO;
import com.logistics.vo.SupplierVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Override
    public List<Supplier> getAllSuppliers() {
        return list();
    }

    @Override
    public List<SupplierVO> getAllSupplierVOs() {
        List<Supplier> suppliers = list();
        return suppliers.stream().map(supplier -> {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(supplier, supplierVO);
            return supplierVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public SupplierVO getSupplierById(Long id) {
        Supplier supplier = getById(id);
        if (supplier == null) {
            return null;
        }
        SupplierVO supplierVO = new SupplierVO();
        BeanUtils.copyProperties(supplier, supplierVO);
        return supplierVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSupplier(SupplierRequestVO supplierRequestVO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierRequestVO, supplier);
        return save(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSupplier(Long id, SupplierRequestVO supplierRequestVO) {
        Supplier supplier = getById(id);
        if (supplier == null) {
            throw new ResourceNotFoundException("供应商", id);
        }
        BeanUtils.copyProperties(supplierRequestVO, supplier);
        return updateById(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSupplier(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSuppliers(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Map<String, Object> getSupplierStatistics() {
        return baseMapper.selectSupplierStatistics();
    }

    @Override
    public List<SupplierVO> getSuppliersByName(String name) {
        List<Supplier> suppliers = baseMapper.selectSuppliersByName(name);
        return suppliers.stream().map(supplier -> {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(supplier, supplierVO);
            return supplierVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<SupplierVO> getSuppliersByAddress(String address) {
        List<Supplier> suppliers = baseMapper.selectSuppliersByAddress(address);
        return suppliers.stream().map(supplier -> {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(supplier, supplierVO);
            return supplierVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSupplierByContactRanking() {
        return baseMapper.selectSupplierByContactRanking();
    }
}
