package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Supplier;
import com.logistics.vo.SupplierRequestVO;
import com.logistics.vo.SupplierVO;

import java.util.List;
import java.util.Map;

public interface SupplierService extends IService<Supplier> {
    List<Supplier> getAllSuppliers();
    List<SupplierVO> getAllSupplierVOs();
    SupplierVO getSupplierById(Long id);
    boolean createSupplier(SupplierRequestVO supplierRequestVO);
    boolean updateSupplier(Long id, SupplierRequestVO supplierRequestVO);
    boolean deleteSupplier(Long id);
    boolean batchDeleteSuppliers(List<Long> ids);
    
    boolean batchCreateSuppliers(List<SupplierRequestVO> supplierRequestVOs);
    boolean batchUpdateSuppliers(List<Supplier> suppliers);
    
    Map<String, Object> getSupplierStatistics();
    List<SupplierVO> getSuppliersByName(String name);
    List<SupplierVO> getSuppliersByAddress(String address);
    List<Map<String, Object>> getSupplierByContactRanking();
}
