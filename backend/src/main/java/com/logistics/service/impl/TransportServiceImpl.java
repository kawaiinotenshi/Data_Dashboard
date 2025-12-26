package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logistics.entity.Transport;
import com.logistics.mapper.TransportMapper;
import com.logistics.service.TransportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements TransportService {
    @Override
    public List<Transport> getAllTransports() {
        return list();
    }

    @Override
    public List<Transport> getTransportsByStatus(String status) {
        QueryWrapper<Transport> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return list(wrapper);
    }
}
