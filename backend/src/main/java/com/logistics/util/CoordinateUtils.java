package com.logistics.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 坐标工具类，用于管理城市与经纬度的映射关系
 */
public class CoordinateUtils {
    
    // 城市坐标映射（模拟数据）
    private static final Map<String, double[]> CITY_COORDINATES = new HashMap<>();
    
    static {
        // 初始化主要城市的坐标
        CITY_COORDINATES.put("北京", new double[]{116.4074, 39.9042});
        CITY_COORDINATES.put("上海", new double[]{121.4737, 31.2304});
        CITY_COORDINATES.put("广州", new double[]{113.2644, 23.1291});
        CITY_COORDINATES.put("深圳", new double[]{114.0579, 22.5431});
        CITY_COORDINATES.put("成都", new double[]{104.0668, 30.5728});
        CITY_COORDINATES.put("杭州", new double[]{120.1551, 30.2741});
        CITY_COORDINATES.put("武汉", new double[]{114.3055, 30.5928});
        CITY_COORDINATES.put("西安", new double[]{108.9398, 34.3416});
        CITY_COORDINATES.put("重庆", new double[]{106.5501, 29.5630});
        CITY_COORDINATES.put("南京", new double[]{118.7969, 32.0603});
        CITY_COORDINATES.put("天津", new double[]{117.2008, 39.0842});
        CITY_COORDINATES.put("苏州", new double[]{120.5853, 31.2989});
        CITY_COORDINATES.put("郑州", new double[]{113.6254, 34.7466});
        CITY_COORDINATES.put("长沙", new double[]{112.9388, 28.2282});
        CITY_COORDINATES.put("沈阳", new double[]{123.4328, 41.8045});
        CITY_COORDINATES.put("青岛", new double[]{120.3826, 36.0671});
        CITY_COORDINATES.put("济南", new double[]{117.1200, 36.6513});
        CITY_COORDINATES.put("大连", new double[]{121.6147, 38.9140});
        CITY_COORDINATES.put("宁波", new double[]{121.5498, 29.8683});
        CITY_COORDINATES.put("厦门", new double[]{118.0894, 24.4798});
    }
    
    /**
     * 根据城市名称获取经纬度坐标
     * @param city 城市名称
     * @return 经纬度数组 [经度, 纬度]，如果城市不存在则返回null
     */
    public static double[] getCoordinatesByCity(String city) {
        if (city == null || city.isEmpty()) {
            return null;
        }
        
        // 尝试直接匹配城市名称
        double[] coordinates = CITY_COORDINATES.get(city);
        if (coordinates != null) {
            return coordinates;
        }
        
        // 尝试提取城市名称（如"上海仓库" -> "上海"）
        for (String key : CITY_COORDINATES.keySet()) {
            if (city.contains(key)) {
                return CITY_COORDINATES.get(key);
            }
        }
        
        return null;
    }
    
    /**
     * 根据城市名称获取经度
     * @param city 城市名称
     * @return 经度，如果城市不存在则返回null
     */
    public static Double getLongitudeByCity(String city) {
        double[] coordinates = getCoordinatesByCity(city);
        return coordinates != null ? coordinates[0] : null;
    }
    
    /**
     * 根据城市名称获取纬度
     * @param city 城市名称
     * @return 纬度，如果城市不存在则返回null
     */
    public static Double getLatitudeByCity(String city) {
        double[] coordinates = getCoordinatesByCity(city);
        return coordinates != null ? coordinates[1] : null;
    }
}