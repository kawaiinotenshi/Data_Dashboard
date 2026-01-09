package com.logistics.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbMigration {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {

            // 执行数据库迁移语句
            String[] sqls = {
                "ALTER TABLE orders ADD COLUMN warehouse_id BIGINT COMMENT '关联的仓库ID'",
                "ALTER TABLE orders ADD COLUMN transport_id BIGINT COMMENT '关联的运输任务ID'",
                "ALTER TABLE orders ADD COLUMN required_capacity DECIMAL(10,2) COMMENT '订单所需容量'",
                "ALTER TABLE transport ADD COLUMN order_id BIGINT COMMENT '关联的订单ID'"
            };

            for (String sql : sqls) {
                try {
                    stmt.executeUpdate(sql);
                    System.out.println("成功执行: " + sql);
                } catch (Exception e) {
                    System.out.println("执行失败(可能是列已存在): " + sql);
                    System.out.println("错误信息: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}