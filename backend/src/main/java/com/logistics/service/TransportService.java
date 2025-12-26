package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Transport;

import java.util.List;

public interface TransportService extends IService<Transport> {
    List<Transport> getAllTransports();
    List<Transport> getTransportsByStatus(String status);
}
