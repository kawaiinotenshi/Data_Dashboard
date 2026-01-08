package com.logistics.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.logistics.common.exception.ValidationException;
import com.logistics.service.OrderService;
import com.logistics.vo.OrderRequestVO;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyExcel导入监听器
 */
@Slf4j
public class EasyExcelListener extends AnalysisEventListener<OrderRequestVO> {

    private static final int BATCH_SIZE = 100;
    private List<OrderRequestVO> dataList = new ArrayList<>();
    private OrderService orderService;

    public EasyExcelListener(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 读取每一行数据
     */
    @Override
    public void invoke(OrderRequestVO orderRequestVO, AnalysisContext analysisContext) {
        // 简单验证数据
        if (orderRequestVO.getOrderNo() == null || orderRequestVO.getOrderNo().isEmpty()) {
            throw new ValidationException("订单编号不能为空");
        }
        if (orderRequestVO.getCustomerName() == null || orderRequestVO.getCustomerName().isEmpty()) {
            throw new ValidationException("客户名称不能为空");
        }
        if (orderRequestVO.getOrderAmount() == null || orderRequestVO.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("订单金额必须大于0");
        }
        
        dataList.add(orderRequestVO);
        
        // 批量处理
        if (dataList.size() >= BATCH_SIZE) {
            saveData();
            dataList.clear();
        }
    }

    /**
     * 所有数据读取完成后调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("Excel导入完成，共处理了 {} 条数据", dataList.size());
    }

    /**
     * 保存数据到数据库
     */
    private void saveData() {
        if (!dataList.isEmpty()) {
            orderService.batchCreateOrders(dataList);
            log.info("批量保存了 {} 条订单数据", dataList.size());
        }
    }
}