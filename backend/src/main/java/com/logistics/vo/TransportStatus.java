package com.logistics.vo;

/**
 * 物流任务状态常量
 */
public class TransportStatus {
    
    /**
     * 待处理
     */
    public static final String PENDING = "待处理";
    
    /**
     * 运输中
     */
    public static final String IN_TRANSIT = "运输中";
    
    /**
     * 已送达
     */
    public static final String DELIVERED = "已送达";
    
    /**
     * 已完成
     */
    public static final String COMPLETED = "已完成";
    
    /**
     * 已取消
     */
    public static final String CANCELLED = "已取消";
    
    /**
     * 获取所有状态列表
     */
    public static String[] getAllStatuses() {
        return new String[] { PENDING, IN_TRANSIT, DELIVERED, COMPLETED, CANCELLED };
    }
    
    /**
     * 检查状态是否有效
     */
    public static boolean isValidStatus(String status) {
        for (String validStatus : getAllStatuses()) {
            if (validStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }
}