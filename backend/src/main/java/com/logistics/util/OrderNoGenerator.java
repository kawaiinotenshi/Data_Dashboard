package com.logistics.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 订单号生成器
 * 采用 ORD + yyyyMMdd + 4位流水号格式
 */
@Component
public class OrderNoGenerator {

    private final OrderMapper orderMapper;
    private final Lock lock = new ReentrantLock();

    @Autowired
    public OrderNoGenerator(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 生成唯一订单号
     * @return 格式为 ORD + yyyyMMdd + 4位流水号
     */
    public String generateOrderNo() {
        lock.lock();
        try {
            // 获取当前日期
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateStr = today.format(formatter);
            String prefix = "ORD" + dateStr;

            // 查询当天最大订单号
            QueryWrapper<com.logistics.entity.Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("order_no", prefix)
                        .orderByDesc("order_no")
                        .last("LIMIT 1");

            com.logistics.entity.Order lastOrder = orderMapper.selectOne(queryWrapper);
            int sequence = 1;

            if (lastOrder != null) {
                String lastOrderNo = lastOrder.getOrderNo();
                String sequenceStr = lastOrderNo.substring(11, 15); // 截取4位流水号
                sequence = Integer.parseInt(sequenceStr) + 1;
            }

            // 格式化为4位，不足前面补0
            String sequenceStr = String.format("%04d", sequence);
            return prefix + sequenceStr;
        } finally {
            lock.unlock();
        }
    }
}