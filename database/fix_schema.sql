-- 修复 warehouse 表结构，添加缺失的字段
USE logistics_db;

-- 添加 is_deleted 字段（逻辑删除）
ALTER TABLE warehouse ADD COLUMN is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)';

-- 添加 version 字段（乐观锁）
ALTER TABLE warehouse ADD COLUMN version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 为其他表也添加这些字段（如果需要）
-- 库存占比表
ALTER TABLE inventory_ratio ADD COLUMN is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)';
ALTER TABLE inventory_ratio ADD COLUMN version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 清关数据表
ALTER TABLE customs_clearance ADD COLUMN is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)';
ALTER TABLE customs_clearance ADD COLUMN version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 出入库记录表
ALTER TABLE in_out_record ADD COLUMN is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)';
ALTER TABLE in_out_record ADD COLUMN version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 仓库分布表
ALTER TABLE warehouse_distribution ADD COLUMN is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)';
ALTER TABLE warehouse_distribution ADD COLUMN version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 季度业务数据表
ALTER TABLE business_quarterly ADD COLUMN is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除, 1:已删除)';
ALTER TABLE business_quarterly ADD COLUMN version INT DEFAULT 0 COMMENT '乐观锁版本号';
