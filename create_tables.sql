-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE logistics_db;

-- 创建部门表
CREATE TABLE IF NOT EXISTS department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    description VARCHAR(500) COMMENT '部门描述',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted INTEGER DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除',
    version INTEGER DEFAULT 1 COMMENT '乐观锁版本号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 创建员工表
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '员工姓名',
    department_id BIGINT COMMENT '部门ID',
    salary DOUBLE COMMENT '薪资',
    hire_date DATE COMMENT '入职日期',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted INTEGER DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除',
    version INTEGER DEFAULT 1 COMMENT '乐观锁版本号',
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 插入测试数据
INSERT INTO department (name, description) VALUES
('技术部', '负责系统开发和维护'),
('财务部', '负责财务核算和管理'),
('人力资源部', '负责人力资源管理'),
('市场部', '负责市场营销'),
('销售部', '负责产品销售');

INSERT INTO employee (name, department_id, salary, hire_date) VALUES
('张三', 1, 15000, '2023-01-15'),
('李四', 1, 18000, '2022-05-20'),
('王五', 2, 12000, '2023-03-10'),
('赵六', 3, 10000, '2023-07-01'),
('孙七', 4, 13000, '2022-11-30'),
('周八', 5, 16000, '2023-02-28'),
('吴九', 1, 20000, '2021-09-15'),
('郑十', 2, 14000, '2023-04-25');
