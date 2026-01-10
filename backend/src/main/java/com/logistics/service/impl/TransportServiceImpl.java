package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.Order;
import com.logistics.entity.Transport;
import com.logistics.mapper.TransportMapper;
import com.logistics.service.OrderService;
import com.logistics.service.TransportService;
import com.logistics.util.CoordinateUtils;
import com.logistics.util.WebSocketUtils;
import com.logistics.vo.TransportRequestVO;
import com.logistics.vo.TransportVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TransportServiceImpl extends BaseEntityServiceImpl<TransportMapper, Transport> implements TransportService {
    
    @Autowired
    @Lazy
    private OrderService orderService;
    
    @Autowired
    private WebSocketUtils webSocketUtils;
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
        // 创建运输任务
        Transport transport = new Transport();
        BeanUtils.copyProperties(transportRequestVO, transport);
        
        // 设置坐标信息
        if (transport.getOrigin() != null) {
            transport.setOriginLng(CoordinateUtils.getLongitudeByCity(transport.getOrigin()));
            transport.setOriginLat(CoordinateUtils.getLatitudeByCity(transport.getOrigin()));
        }
        
        if (transport.getDestination() != null) {
            transport.setDestinationLng(CoordinateUtils.getLongitudeByCity(transport.getDestination()));
            transport.setDestinationLat(CoordinateUtils.getLatitudeByCity(transport.getDestination()));
        }
        
        // 保存运输任务
        boolean result = save(transport);
        if (result) {
            // 广播所有数据更新
            webSocketUtils.broadcastAllUpdates();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTransport(Long id, TransportRequestVO transportRequestVO) {
        // 获取原运输任务
        Transport oldTransport = getById(id);
        if (oldTransport == null) {
            return false;
        }
        
        // 更新运输任务
        Transport newTransport = new Transport();
        BeanUtils.copyProperties(transportRequestVO, newTransport);
        newTransport.setId(id);
        
        // 更新坐标信息（与创建时保持一致的逻辑）
        if (newTransport.getOrigin() != null) {
            newTransport.setOriginLng(CoordinateUtils.getLongitudeByCity(newTransport.getOrigin()));
            newTransport.setOriginLat(CoordinateUtils.getLatitudeByCity(newTransport.getOrigin()));
        } else {
            newTransport.setOriginLng(null);
            newTransport.setOriginLat(null);
        }
        
        if (newTransport.getDestination() != null) {
            newTransport.setDestinationLng(CoordinateUtils.getLongitudeByCity(newTransport.getDestination()));
            newTransport.setDestinationLat(CoordinateUtils.getLatitudeByCity(newTransport.getDestination()));
        } else {
            newTransport.setDestinationLng(null);
            newTransport.setDestinationLat(null);
        }
        
        boolean result = updateById(newTransport);
        
        if (result) {
            // 广播所有数据更新
            webSocketUtils.broadcastAllUpdates();
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTransport(Long id) {
        boolean result = deleteEntity(id);
        if (result) {
            // 广播所有数据更新
            webSocketUtils.broadcastAllUpdates();
        }
        return result;
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
