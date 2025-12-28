-- 创建数据库
CREATE DATABASE IF NOT EXISTS logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE logistics_db;

-- 1. 仓库信息表
DROP TABLE IF EXISTS warehouse;
CREATE TABLE warehouse (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '仓库名称',
    location VARCHAR(200) COMMENT '仓库位置',
    area DECIMAL(10,2) COMMENT '面积(平方米)',
    utilization_rate DECIMAL(5,2) COMMENT '利用率(%)',
    capacity DECIMAL(10,2) COMMENT '容量(吨)',
    throughput DECIMAL(10,2) COMMENT '流转量(吨)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)',
    version INT DEFAULT 0 COMMENT '乐观锁版本号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库信息表';

-- 2. 库存占比表
DROP TABLE IF EXISTS inventory_ratio;
CREATE TABLE inventory_ratio (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    enterprise_name VARCHAR(100) NOT NULL COMMENT '企业名称',
    ratio DECIMAL(5,2) NOT NULL COMMENT '占比(%)',
    month VARCHAR(7) COMMENT '月份(yyyy-MM)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存占比表';

-- 3. 清关数据表
DROP TABLE IF EXISTS customs_clearance;
CREATE TABLE customs_clearance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    country VARCHAR(50) NOT NULL COMMENT '国家',
    product_type VARCHAR(100) COMMENT '产品类型',
    quantity DECIMAL(10,2) COMMENT '数量(吨)',
    `year_month` VARCHAR(7) COMMENT '年月(yyyy-MM)',
    growth_rate DECIMAL(5,2) COMMENT '同比增长率(%)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='清关数据表';

-- 4. 出入库记录表
DROP TABLE IF EXISTS in_out_record;
CREATE TABLE in_out_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    record_date DATE NOT NULL COMMENT '记录日期',
    type TINYINT NOT NULL COMMENT '类型(1:入库, 2:出库)',
    quantity DECIMAL(10,2) NOT NULL COMMENT '数量(吨)',
    warehouse_id BIGINT COMMENT '仓库ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出入库记录表';

-- 5. 仓库分布表
DROP TABLE IF EXISTS warehouse_distribution;
CREATE TABLE warehouse_distribution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    province VARCHAR(50) NOT NULL COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    warehouse_count INT COMMENT '仓库数量',
    total_capacity DECIMAL(10,2) COMMENT '总容量(吨)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库分布表';

-- 6. 季度业务数据表
DROP TABLE IF EXISTS business_quarterly;
CREATE TABLE business_quarterly (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    year INT COMMENT '年份',
    quarter TINYINT COMMENT '季度(1-4)',
    business_type VARCHAR(50) COMMENT '业务类型',
    amount DECIMAL(10,2) COMMENT '金额(万元)',
    growth_rate DECIMAL(5,2) COMMENT '增长率(%)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='季度业务数据表';

-- 插入示例数据

-- 仓库信息
INSERT INTO warehouse (name, location, area, utilization_rate, capacity, throughput) VALUES 
('大连仓', '辽宁省大连市', 50000.00, 75.00, 150000.00, 1500000.00),
('青岛仓', '山东省青岛市', 30000.00, 82.00, 80000.00, 500000.00),
('宁波仓', '浙江省宁波市', 28000.00, 68.00, 70000.00, 600000.00),
('上海仓', '上海市', 45000.00, 88.00, 120000.00, 1800000.00),
('广州仓', '广东省广州市', 38000.00, 72.00, 95000.00, 850000.00);

-- 库存占比
INSERT INTO inventory_ratio (enterprise_name, ratio, month) VALUES 
('顺丰物流', 25.50, '2025-12'),
('京东物流', 22.30, '2025-12'),
('德邦物流', 18.70, '2025-12'),
('中通快递', 15.20, '2025-12'),
('圆通速递', 10.80, '2025-12'),
('其他', 7.50, '2025-12');

-- 清关数据
INSERT INTO customs_clearance (country, product_type, quantity, `year_month`, growth_rate) VALUES 
('英国', '电子产品', 4500.00, '2025-12', 12.50),
('英国', '服装', 3200.00, '2025-12', 8.30),
('英国', '食品', 2800.00, '2025-12', 15.20),
('美国', '电子产品', 5200.00, '2025-12', 10.80),
('美国', '服装', 4100.00, '2025-12', 6.70),
('德国', '机械设备', 3800.00, '2025-12', 9.40),
('日本', '汽车配件', 3500.00, '2025-12', 7.80);

-- 出入库记录
INSERT INTO in_out_record (record_date, type, quantity, warehouse_id) VALUES 
('2025-12-01', 1, 5000.00, 1),
('2025-12-02', 2, 3000.00, 1),
('2025-12-03', 1, 4500.00, 2),
('2025-12-04', 2, 2800.00, 2),
('2025-12-05', 1, 3800.00, 3),
('2025-12-06', 2, 3200.00, 3),
('2025-12-07', 1, 6000.00, 4),
('2025-12-08', 2, 4500.00, 4),
('2025-12-09', 1, 4200.00, 5),
('2025-12-10', 2, 3500.00, 5);

-- 仓库分布
INSERT INTO warehouse_distribution (province, city, warehouse_count, total_capacity) VALUES 
('辽宁省', '大连', 45, 150000.00),
('山东省', '青岛', 38, 80000.00),
('浙江省', '宁波', 52, 70000.00),
('上海市', '上海', 68, 120000.00),
('广东省', '广州', 62, 95000.00),
('江苏省', '苏州', 35, 65000.00),
('天津市', '天津', 28, 55000.00),
('福建省', '厦门', 25, 45000.00),
('四川省', '成都', 18, 35000.00),
('湖北省', '武汉', 22, 40000.00);

-- 季度业务数据
INSERT INTO business_quarterly (year, quarter, business_type, amount, growth_rate) VALUES 
(2025, 1, '仓储业务', 1250.00, 8.50),
(2025, 1, '运输业务', 980.00, 12.30),
(2025, 1, '清关业务', 650.00, 15.80),
(2025, 2, '仓储业务', 1380.00, 10.40),
(2025, 2, '运输业务', 1120.00, 14.20),
(2025, 2, '清关业务', 720.00, 10.70),
(2025, 3, '仓储业务', 1450.00, 5.10),
(2025, 3, '运输业务', 1250.00, 11.60),
(2025, 3, '清关业务', 780.00, 8.30),
(2025, 4, '仓储业务', 1580.00, 8.90),
(2025, 4, '运输业务', 1380.00, 10.40),
(2025, 4, '清关业务', 850.00, 9.00);
