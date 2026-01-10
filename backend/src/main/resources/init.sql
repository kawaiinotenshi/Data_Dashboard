-- 创建产品表
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `name` varchar(255) NOT NULL COMMENT '产品名称',
  `category` varchar(100) DEFAULT NULL COMMENT '产品分类',
  `price` decimal(10,2) DEFAULT NULL COMMENT '产品价格',
  `stock` int DEFAULT NULL COMMENT '库存数量',
  `description` varchar(255) DEFAULT NULL COMMENT '产品描述',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `safety_stock` int DEFAULT NULL COMMENT '安全库存',
  `status` int DEFAULT NULL COMMENT '状态',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品表';

-- 创建部门表
CREATE TABLE IF NOT EXISTS `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(50) NOT NULL COMMENT '部门名称',
  `description` varchar(200) DEFAULT NULL COMMENT '部门描述',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除(0:未删除,1:已删除)',
  `version` int(11) DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_department_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 创建员工表
CREATE TABLE IF NOT EXISTS `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `name` varchar(50) NOT NULL COMMENT '员工姓名',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `salary` decimal(10,2) DEFAULT NULL COMMENT '工资',
  `hire_date` date DEFAULT NULL COMMENT '入职日期',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除(0:未删除,1:已删除)',
  `version` int(11) DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_employee_department_id` (`department_id`),
  KEY `idx_employee_hire_date` (`hire_date`),
  KEY `idx_employee_salary` (`salary`),
  CONSTRAINT `fk_employee_department_id` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- 插入部门数据
INSERT INTO `department` (`name`, `description`) VALUES
('技术部', '负责系统开发和维护'),
('市场部', '负责产品推广和销售'),
('财务部', '负责财务核算和管理'),
('人力资源部', '负责人力资源管理'),
('运营部', '负责日常运营和管理');

-- 插入员工数据
INSERT INTO `employee` (`name`, `department_id`, `salary`, `hire_date`) VALUES
('张三', 1, 8000.00, '2020-01-15'),
('李四', 1, 9500.00, '2019-03-22'),
('王五', 2, 7500.00, '2021-05-10'),
('赵六', 2, 8200.00, '2020-07-18'),
('孙七', 3, 10000.00, '2018-10-05'),
('周八', 3, 8800.00, '2020-12-30'),
('吴九', 4, 9000.00, '2019-08-15'),
('郑十', 5, 7200.00, '2021-02-14');
