package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.Transport;
import com.logistics.mapper.TransportMapper;
import com.logistics.service.TransportService;
import com.logistics.vo.TransportRequestVO;
import com.logistics.vo.TransportVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TransportServiceImpl extends BaseEntityServiceImpl<TransportMapper, Transport> implements TransportService {
    @Override
    public String getEntityName() {
        return "运输记录";
    }

    @Override
    public Class<?> getVOClass() {
        return TransportVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return TransportRequestVO.class;
    }

    @Override
    public List<Transport> getAllTransports() {
        return getAllEntities();
    }

    @Override
    public List<TransportVO> getAllTransportVOs() {
        return getAllEntityVOs(this::convertToTransportVO);
    }

    @Override
    public List<TransportVO> getTransportsByStatus(String status) {
        QueryWrapper<Transport> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        List<Transport> transports = list(wrapper);
        return convertToVOList(transports, this::convertToTransportVO);
    }

    @Override
    public TransportVO getTransportById(Long id) {
        return getEntityById(id, this::convertToTransportVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTransport(TransportRequestVO transportRequestVO) {
        return createEntity(transportRequestVO, Transport.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTransport(Long id, TransportRequestVO transportRequestVO) {
        return updateEntity(id, transportRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTransport(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTransports(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    public Map<String, Object> getTransportStatistics() {
        return baseMapper.selectTransportStatistics();
    }

    @Override
    public List<TransportVO> getTransportByMonth(String month) {
        List<Transport> transports = baseMapper.selectTransportByMonth(month);
        return convertToVOList(transports, this::convertToTransportVO);
    }

    @Override
    public List<TransportVO> getTransportByVehicleType(String vehicleType) {
        List<Transport> transports = baseMapper.selectTransportByVehicleType(vehicleType);
        return convertToVOList(transports, this::convertToTransportVO);
    }

    @Override
    public List<Map<String, Object>> getTransportTrend() {
        return baseMapper.selectTransportTrend();
    }

    @Override
    public List<Map<String, Object>> getTransportByVehicleTypeGroup() {
        return baseMapper.selectTransportByVehicleTypeGroup();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTransports(List<Transport> transports) {
        return batchUpdateEntities(transports);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateTransports(List<TransportRequestVO> transportRequestVOs) {
        return batchCreateEntities(transportRequestVOs, Transport.class);
    }

    private TransportVO convertToTransportVO(Transport transport) {
        TransportVO transportVO = new TransportVO();
        BeanUtils.copyProperties(transport, transportVO);
        return transportVO;
    }
}
