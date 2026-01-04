-- 物流供应链可视化面板 - 数据库索引优化脚本
-- 创建日期: 2026-01-04
-- 说明: 为常用查询字段添加索引，提升查询性能

USE logistics_db;

-- 1. 仓库表索引优化
-- 为仓库名称添加索引（常用于搜索）
CREATE INDEX IF NOT EXISTS idx_warehouse_name ON warehouse(name);

-- 为仓库位置添加索引（常用于按位置查询）
CREATE INDEX IF NOT EXISTS idx_warehouse_location ON warehouse(location);

-- 为仓库状态添加索引（常用于筛选）
CREATE INDEX IF NOT EXISTS idx_warehouse_status ON warehouse(status);

-- 复合索引：按状态和创建时间查询
CREATE INDEX IF NOT EXISTS idx_warehouse_status_created ON warehouse(status, created_time);


-- 2. 客户表索引优化
-- 为客户名称添加索引（常用于搜索）
CREATE INDEX IF NOT EXISTS idx_customer_name ON customer(name);

-- 为客户联系方式添加索引（常用于查询）
CREATE INDEX IF NOT EXISTS idx_customer_contact ON customer(contact);

-- 为客户状态添加索引（常用于筛选）
CREATE INDEX IF NOT EXISTS idx_customer_status ON customer(status);

-- 复合索引：按状态和创建时间查询
CREATE INDEX IF NOT EXISTS idx_customer_status_created ON customer(status, created_time);


-- 3. 供应商表索引优化
-- 为供应商名称添加索引（常用于搜索）
CREATE INDEX IF NOT EXISTS idx_supplier_name ON supplier(name);

-- 为供应商联系方式添加索引（常用于查询）
CREATE INDEX IF NOT EXISTS idx_supplier_contact ON supplier(contact);

-- 为供应商评级添加索引（常用于筛选）
CREATE INDEX IF NOT EXISTS idx_supplier_rating ON supplier(rating);

-- 复合索引：按评级和创建时间查询
CREATE INDEX IF NOT EXISTS idx_supplier_rating_created ON supplier(rating, created_time);


-- 4. 订单表索引优化
-- 为订单编号添加索引（常用于查询）
CREATE INDEX IF NOT EXISTS idx_order_number ON order(order_number);

-- 为客户ID添加索引（常用于关联查询）
CREATE INDEX IF NOT EXISTS idx_order_customer_id ON order(customer_id);

-- 为订单状态添加索引（常用于筛选）
CREATE INDEX IF NOT EXISTS idx_order_status ON order(status);

-- 为订单创建时间添加索引（常用于时间范围查询）
CREATE INDEX IF NOT EXISTS idx_order_created_time ON order(created_time);

-- 复合索引：按客户和状态查询
CREATE INDEX IF NOT EXISTS idx_order_customer_status ON order(customer_id, status);

-- 复合索引：按状态和创建时间查询
CREATE INDEX IF NOT EXISTS idx_order_status_created ON order(status, created_time);


-- 5. 库存表索引优化
-- 为企业名称添加索引（常用于搜索）
CREATE INDEX IF NOT EXISTS idx_inventory_enterprise ON inventory_ratio(enterprise_name);

-- 为月份添加索引（常用于按月查询）
CREATE INDEX IF NOT EXISTS idx_inventory_month ON inventory_ratio(month);

-- 为创建时间添加索引（常用于时间范围查询）
CREATE INDEX IF NOT EXISTS idx_inventory_created_time ON inventory_ratio(created_time);

-- 复合索引：按企业和月份查询
CREATE INDEX IF NOT EXISTS idx_inventory_enterprise_month ON inventory_ratio(enterprise_name, month);


-- 6. 运输表索引优化
-- 为运输编号添加索引（常用于查询）
CREATE INDEX IF NOT EXISTS idx_transport_number ON transport(transport_number);

-- 为起点和终点添加索引（常用于路线查询）
CREATE INDEX IF NOT EXISTS idx_transport_origin ON transport(origin);
CREATE INDEX IF NOT EXISTS idx_transport_destination ON transport(destination);

-- 为运输状态添加索引（常用于筛选）
CREATE INDEX IF NOT EXISTS idx_transport_status ON transport(status);

-- 为运输日期添加索引（常用于时间范围查询）
CREATE INDEX IF NOT EXISTS idx_transport_date ON transport(transport_date);

-- 复合索引：按状态和日期查询
CREATE INDEX IF NOT EXISTS idx_transport_status_date ON transport(status, transport_date);

-- 复合索引：按起点和终点查询
CREATE INDEX IF NOT EXISTS idx_transport_route ON transport(origin, destination);


-- 7. 全文索引（可选，用于模糊搜索）
-- 如果需要全文搜索功能，可以创建全文索引
-- CREATE FULLTEXT INDEX IF NOT EXISTS idx_warehouse_name_fulltext ON warehouse(name);
-- CREATE FULLTEXT INDEX IF NOT EXISTS idx_customer_name_fulltext ON customer(name);
-- CREATE FULLTEXT INDEX IF NOT EXISTS idx_supplier_name_fulltext ON supplier(name);


-- 8. 查看索引使用情况
-- 可以使用以下命令查看索引是否被有效使用
-- SELECT 
--     TABLE_NAME,
--     INDEX_NAME,
--     COLUMN_NAME,
--     SEQ_IN_INDEX,
--     CARDINALITY
-- FROM 
--     information_schema.STATISTICS
-- WHERE 
--     TABLE_SCHEMA = 'logistics_db'
-- ORDER BY 
--     TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;


-- 9. 分析表以更新索引统计信息
ANALYZE TABLE warehouse;
ANALYZE TABLE customer;
ANALYZE TABLE supplier;
ANALYZE TABLE `order`;
ANALYZE TABLE inventory_ratio;
ANALYZE TABLE transport;


-- 10. 查看表大小和索引大小
-- SELECT 
--     TABLE_NAME,
--     ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS 'Size (MB)',
--     ROUND((INDEX_LENGTH / 1024 / 1024), 2) AS 'Index Size (MB)',
--     ROUND((INDEX_LENGTH / (DATA_LENGTH + INDEX_LENGTH)) * 100, 2) AS 'Index Ratio %'
-- FROM 
--     information_schema.TABLES
-- WHERE 
--     TABLE_SCHEMA = 'logistics_db'
-- ORDER BY 
--     (DATA_LENGTH + INDEX_LENGTH) DESC;


-- 11. 性能优化建议
-- 定期执行以下命令来优化表
-- OPTIMIZE TABLE warehouse;
-- OPTIMIZE TABLE customer;
-- OPTIMIZE TABLE supplier;
-- OPTIMIZE TABLE `order`;
-- OPTIMIZE TABLE inventory_ratio;
-- OPTIMIZE TABLE transport;


-- 12. 查看慢查询日志配置
-- SHOW VARIABLES LIKE 'slow_query%';
-- SHOW VARIABLES LIKE 'long_query_time';


-- 13. 启用慢查询日志（如果需要）
-- SET GLOBAL slow_query_log = 'ON';
-- SET GLOBAL long_query_time = 2;
-- SET GLOBAL slow_query_log_file = '/var/log/mysql/mysql-slow.log';


-- 14. 查看当前执行的查询
-- SHOW PROCESSLIST;


-- 15. 查看索引使用效率
-- 可以使用EXPLAIN分析查询语句的执行计划
-- EXPLAIN SELECT * FROM order WHERE customer_id = 1 AND status = 'pending';
-- EXPLAIN SELECT * FROM warehouse WHERE status = 'active' ORDER BY created_time DESC;


-- 完成提示
SELECT '数据库索引优化完成！' AS message;
