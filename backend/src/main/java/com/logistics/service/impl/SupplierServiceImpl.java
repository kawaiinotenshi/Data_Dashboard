package com.logistics.service.impl;

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
public class SupplierServiceImpl extends BaseEntityServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Override
    public String getEntityName() {
        return "供应商";
    }

    @Override
    public Class<?> getVOClass() {
        return SupplierVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return SupplierRequestVO.class;
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return getAllEntities();
    }

    @Override
    public List<SupplierVO> getAllSupplierVOs() {
        return getAllEntityVOs(this::convertToSupplierVO);
    }

    @Override
    public SupplierVO getSupplierById(Long id) {
        return getEntityById(id, this::convertToSupplierVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSupplier(SupplierRequestVO supplierRequestVO) {
        return createEntity(supplierRequestVO, Supplier.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSupplier(Long id, SupplierRequestVO supplierRequestVO) {
        return updateEntity(id, supplierRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSupplier(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSuppliers(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    public Map<String, Object> getSupplierStatistics() {
        return baseMapper.selectSupplierStatistics();
    }

    @Override
    public List<SupplierVO> getSuppliersByName(String name) {
        List<Supplier> suppliers = baseMapper.selectSuppliersByName(name);
        return convertToVOList(suppliers, this::convertToSupplierVO);
    }

    @Override
    public List<SupplierVO> getSuppliersByAddress(String address) {
        List<Supplier> suppliers = baseMapper.selectSuppliersByAddress(address);
        return convertToVOList(suppliers, this::convertToSupplierVO);
    }

    @Override
    public List<Map<String, Object>> getSupplierByContactRanking() {
        return baseMapper.selectSupplierByContactRanking();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateSuppliers(List<Supplier> suppliers) {
        return batchUpdateEntities(suppliers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateSuppliers(List<SupplierRequestVO> supplierRequestVOs) {
        return batchCreateEntities(supplierRequestVOs, Supplier.class);
    }

    private SupplierVO convertToSupplierVO(Supplier supplier) {
        SupplierVO supplierVO = new SupplierVO();
        BeanUtils.copyProperties(supplier, supplierVO);
        return supplierVO;
    }
}
