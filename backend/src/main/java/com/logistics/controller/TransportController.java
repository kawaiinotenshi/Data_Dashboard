package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.Transport;
import com.logistics.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transport")
@CrossOrigin
public class TransportController {
    @Autowired
    private TransportService transportService;

    @GetMapping("/list")
    public Result<List<Transport>> getAllTransports() {
        List<Transport> list = transportService.getAllTransports();
        return Result.success(list);
    }

    @GetMapping("/status/{status}")
    public Result<List<Transport>> getTransportsByStatus(@PathVariable String status) {
        List<Transport> list = transportService.getTransportsByStatus(status);
        return Result.success(list);
    }
}
