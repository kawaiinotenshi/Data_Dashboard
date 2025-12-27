package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Transport;
import com.logistics.vo.TransportRequestVO;
import com.logistics.vo.TransportVO;

import java.util.List;
import java.util.Map;

public interface TransportService extends IService<Transport> {
    List<Transport> getAllTransports();
    List<TransportVO> getAllTransportVOs();
    List<TransportVO> getTransportsByStatus(String status);
    TransportVO getTransportById(Long id);
    boolean createTransport(TransportRequestVO transportRequestVO);
    boolean updateTransport(Long id, TransportRequestVO transportRequestVO);
    boolean deleteTransport(Long id);
    boolean batchDeleteTransports(List<Long> ids);
    
    boolean batchCreateTransports(List<TransportRequestVO> transportRequestVOs);
    boolean batchUpdateTransports(List<Transport> transports);
    
    Map<String, Object> getTransportStatistics();
    List<TransportVO> getTransportByMonth(String month);
    List<TransportVO> getTransportByVehicleType(String vehicleType);
    List<Map<String, Object>> getTransportTrend();
    List<Map<String, Object>> getTransportByVehicleTypeGroup();
}
