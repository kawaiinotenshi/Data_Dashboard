package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements TransportService {
    @Override
    public List<Transport> getAllTransports() {
        return list();
    }

    @Override
    public List<TransportVO> getAllTransportVOs() {
        List<Transport> transports = list();
        return transports.stream().map(transport -> {
            TransportVO transportVO = new TransportVO();
            BeanUtils.copyProperties(transport, transportVO);
            return transportVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TransportVO> getTransportsByStatus(String status) {
        QueryWrapper<Transport> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        List<Transport> transports = list(wrapper);
        return transports.stream().map(transport -> {
            TransportVO transportVO = new TransportVO();
            BeanUtils.copyProperties(transport, transportVO);
            return transportVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public TransportVO getTransportById(Long id) {
        Transport transport = getById(id);
        if (transport == null) {
            return null;
        }
        TransportVO transportVO = new TransportVO();
        BeanUtils.copyProperties(transport, transportVO);
        return transportVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTransport(TransportRequestVO transportRequestVO) {
        Transport transport = new Transport();
        BeanUtils.copyProperties(transportRequestVO, transport);
        return save(transport);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTransport(Long id, TransportRequestVO transportRequestVO) {
        Transport transport = getById(id);
        if (transport == null) {
            throw new ResourceNotFoundException("运输记录", id);
        }
        BeanUtils.copyProperties(transportRequestVO, transport);
        return updateById(transport);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTransport(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTransports(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Map<String, Object> getTransportStatistics() {
        return baseMapper.selectTransportStatistics();
    }

    @Override
    public List<TransportVO> getTransportByMonth(String month) {
        List<Transport> transports = baseMapper.selectTransportByMonth(month);
        return transports.stream().map(transport -> {
            TransportVO transportVO = new TransportVO();
            BeanUtils.copyProperties(transport, transportVO);
            return transportVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TransportVO> getTransportByVehicleType(String vehicleType) {
        List<Transport> transports = baseMapper.selectTransportByVehicleType(vehicleType);
        return transports.stream().map(transport -> {
            TransportVO transportVO = new TransportVO();
            BeanUtils.copyProperties(transport, transportVO);
            return transportVO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTransportTrend() {
        return baseMapper.selectTransportTrend();
    }

    @Override
    public List<Map<String, Object>> getTransportByVehicleTypeGroup() {
        return baseMapper.selectTransportByVehicleTypeGroup();
    }
}
