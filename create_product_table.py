import mysql.connector

# 连接到MySQL数据库
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="logistics_db"
)

cursor = db.cursor()

# 创建产品表的SQL语句
sql = """CREATE TABLE IF NOT EXISTS `product` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品表';"""

try:
    cursor.execute(sql)
    db.commit()
    print("产品表创建成功！")
except Exception as e:
    print(f"创建产品表失败: {e}")
    db.rollback()
finally:
    cursor.close()
    db.close()