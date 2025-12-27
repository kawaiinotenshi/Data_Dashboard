USE logistics_db;

-- 为所有表添加BaseEntity所需的字段

-- 1. warehouse表
ALTER TABLE warehouse ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE warehouse ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 2. inventory_ratio表
ALTER TABLE inventory_ratio ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE inventory_ratio ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 3. customs_clearance表
ALTER TABLE customs_clearance ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE customs_clearance ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 4. in_out_record表
ALTER TABLE in_out_record ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE in_out_record ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 5. warehouse_distribution表
ALTER TABLE warehouse_distribution ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE warehouse_distribution ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 6. business_quarterly表
ALTER TABLE business_quarterly ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE business_quarterly ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 7. orders表
ALTER TABLE orders ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE orders ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 8. customer表
ALTER TABLE customer ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE customer ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 9. supplier表
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';

-- 10. transport表
ALTER TABLE transport ADD COLUMN IF NOT EXISTS is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)';
ALTER TABLE transport ADD COLUMN IF NOT EXISTS version INT DEFAULT 0 COMMENT '乐观锁版本号';
