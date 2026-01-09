-- Add missing columns to orders table
ALTER TABLE orders ADD COLUMN warehouse_id BIGINT COMMENT '关联的仓库ID';
ALTER TABLE orders ADD COLUMN transport_id BIGINT COMMENT '关联的运输任务ID';
ALTER TABLE orders ADD COLUMN required_capacity DECIMAL(10,2) COMMENT '订单所需容量';

-- Add missing column to transport table
ALTER TABLE transport ADD COLUMN order_id BIGINT COMMENT '关联的订单ID';
ALTER TABLE transport ADD COLUMN origin VARCHAR(255) COMMENT '起始地';
ALTER TABLE transport ADD COLUMN destination VARCHAR(255) COMMENT '目的地';
ALTER TABLE transport ADD COLUMN scheduled_time DATETIME COMMENT '计划时间';
ALTER TABLE transport ADD COLUMN actual_start_time DATETIME COMMENT '实际开始时间';
ALTER TABLE transport ADD COLUMN actual_end_time DATETIME COMMENT '实际结束时间';