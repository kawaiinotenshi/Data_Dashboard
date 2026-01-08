CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '角色 (admin/user)',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态 (active/inactive)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 (0:未删除, 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入默认管理员用户
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `role`, `status`) 
VALUES ('admin', '$2a$10$oFVbf5nFWFyFT7js7R3iAe5MnR37HLgiFLIFDaC6nRin28vsSvgy2', 'admin@example.com', '13800138000', 'admin', 'active');

-- 设置默认管理员密码为root
