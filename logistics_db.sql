/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : logistics_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 04/01/2026 10:13:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_quarterly
-- ----------------------------
DROP TABLE IF EXISTS `business_quarterly`;
CREATE TABLE `business_quarterly`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `year` int NULL DEFAULT NULL,
  `quarter` tinyint NULL DEFAULT NULL,
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `growth_rate` decimal(5, 2) NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business_quarterly
-- ----------------------------
INSERT INTO `business_quarterly` VALUES (1, 2025, 1, '仓储业务', 1250.00, 8.50, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (2, 2025, 1, '运输业务', 980.00, 12.30, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (3, 2025, 1, '清关业务', 650.00, 15.80, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (4, 2025, 2, '仓储业务', 1380.00, 10.40, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (5, 2025, 2, '运输业务', 1120.00, 14.20, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (6, 2025, 2, '清关业务', 720.00, 10.70, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (7, 2025, 3, '仓储业务', 1450.00, 5.10, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (8, 2025, 3, '运输业务', 1250.00, 11.60, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (9, 2025, 3, '清关业务', 780.00, 8.30, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (10, 2025, 4, '仓储业务', 1580.00, 8.90, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (11, 2025, 4, '运输业务', 1380.00, 10.40, '2025-12-26 18:21:13');
INSERT INTO `business_quarterly` VALUES (12, 2025, 4, '清关业务', 850.00, 9.00, '2025-12-26 18:21:13');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  `version` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, '顺丰物流股份有限公司', '陈总', '0755-29901111', '广东省深圳市宝安区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `customer` VALUES (2, '京东物流有限公司', '杨总', '010-89191111', '北京市大兴区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `customer` VALUES (3, '德邦物流股份有限公司', '黄总', '021-60371111', '上海市青浦区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `customer` VALUES (4, '中通快递股份有限公司', '周总', '021-69781111', '上海市青浦区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `customer` VALUES (5, '圆通速递股份有限公司', '吴总', '021-39281111', '上海市青浦区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);

-- ----------------------------
-- Table structure for customs_clearance
-- ----------------------------
DROP TABLE IF EXISTS `customs_clearance`;
CREATE TABLE `customs_clearance`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `product_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `quantity` decimal(10, 2) NULL DEFAULT NULL,
  `month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `growth_rate` decimal(5, 2) NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customs_clearance
-- ----------------------------
INSERT INTO `customs_clearance` VALUES (1, '英国', '电子产品', 4500.00, '2025-12', 12.50, '2025-12-26 18:20:59');
INSERT INTO `customs_clearance` VALUES (2, '英国', '服装', 3200.00, '2025-12', 8.30, '2025-12-26 18:20:59');
INSERT INTO `customs_clearance` VALUES (3, '英国', '食品', 2800.00, '2025-12', 15.20, '2025-12-26 18:20:59');
INSERT INTO `customs_clearance` VALUES (4, '美国', '电子产品', 5200.00, '2025-12', 10.80, '2025-12-26 18:20:59');
INSERT INTO `customs_clearance` VALUES (5, '美国', '服装', 4100.00, '2025-12', 6.70, '2025-12-26 18:20:59');
INSERT INTO `customs_clearance` VALUES (6, '德国', '机械设备', 3800.00, '2025-12', 9.40, '2025-12-26 18:20:59');
INSERT INTO `customs_clearance` VALUES (7, '日本', '汽车配件', 3500.00, '2025-12', 7.80, '2025-12-26 18:20:59');

-- ----------------------------
-- Table structure for in_out_record
-- ----------------------------
DROP TABLE IF EXISTS `in_out_record`;
CREATE TABLE `in_out_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_date` date NOT NULL,
  `type` tinyint NOT NULL,
  `quantity` decimal(10, 2) NOT NULL,
  `warehouse_id` bigint NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of in_out_record
-- ----------------------------
INSERT INTO `in_out_record` VALUES (1, '2025-12-01', 1, 5000.00, 1, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (2, '2025-12-02', 2, 3000.00, 1, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (3, '2025-12-03', 1, 4500.00, 2, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (4, '2025-12-04', 2, 2800.00, 2, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (5, '2025-12-05', 1, 3800.00, 3, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (6, '2025-12-06', 2, 3200.00, 3, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (7, '2025-12-07', 1, 6000.00, 4, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (8, '2025-12-08', 2, 4500.00, 4, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (9, '2025-12-09', 1, 4200.00, 5, '2025-12-26 18:21:09');
INSERT INTO `in_out_record` VALUES (10, '2025-12-10', 2, 3500.00, 5, '2025-12-26 18:21:09');

-- ----------------------------
-- Table structure for inventory_ratio
-- ----------------------------
DROP TABLE IF EXISTS `inventory_ratio`;
CREATE TABLE `inventory_ratio`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enterprise_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ratio` decimal(5, 2) NOT NULL,
  `month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  `version` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory_ratio
-- ----------------------------
INSERT INTO `inventory_ratio` VALUES (1, '顺丰物流', 25.50, '2025-12', '2025-12-26 18:18:02', '2025-12-26 18:18:02', 0, 0);
INSERT INTO `inventory_ratio` VALUES (2, '京东物流', 22.30, '2025-12', '2025-12-26 18:18:02', '2025-12-26 18:18:02', 0, 0);
INSERT INTO `inventory_ratio` VALUES (3, '德邦物流', 18.70, '2025-12', '2025-12-26 18:18:02', '2025-12-26 18:18:02', 0, 0);
INSERT INTO `inventory_ratio` VALUES (4, '中通快递', 15.20, '2025-12', '2025-12-26 18:18:02', '2025-12-26 18:18:02', 0, 0);
INSERT INTO `inventory_ratio` VALUES (5, '圆通速递', 10.80, '2025-12', '2025-12-26 18:18:02', '2025-12-26 18:18:02', 0, 0);
INSERT INTO `inventory_ratio` VALUES (6, '其他', 7.50, '2025-12', '2025-12-26 18:18:02', '2025-12-26 18:18:02', 0, 0);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `customer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_amount` decimal(10, 2) NULL DEFAULT NULL,
  `order_date` date NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  `version` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'ORD202512001', '顺丰物流', 125000.00, '2025-12-01', '已完成', '2025-12-26 18:21:07', '2025-12-26 18:21:07', 0, 0);
INSERT INTO `orders` VALUES (2, 'ORD202512002', '京东物流', 98000.00, '2025-12-02', '已完成', '2025-12-26 18:21:07', '2025-12-26 18:21:07', 0, 0);
INSERT INTO `orders` VALUES (3, 'ORD202512003', '德邦物流', 86000.00, '2025-12-03', '已完成', '2025-12-26 18:21:07', '2025-12-26 18:21:07', 0, 0);
INSERT INTO `orders` VALUES (4, 'ORD202512004', '中通快递', 75000.00, '2025-12-04', '已完成', '2025-12-26 18:21:07', '2025-12-26 18:21:07', 0, 0);
INSERT INTO `orders` VALUES (5, 'ORD202512005', '圆通速递', 62000.00, '2025-12-05', '已完成', '2025-12-26 18:21:07', '2025-12-26 18:21:07', 0, 0);

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  `version` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (1, '大连港务集团', '张经理', '0411-88881234', '辽宁省大连市中山区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `supplier` VALUES (2, '青岛港集团', '李经理', '0532-88882345', '山东省青岛市市南区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `supplier` VALUES (3, '宁波舟山港', '王经理', '0574-88883456', '浙江省宁波市北仑区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `supplier` VALUES (4, '上海港集团', '赵经理', '021-88884567', '上海市浦东新区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);
INSERT INTO `supplier` VALUES (5, '广州港集团', '刘经理', '020-88885678', '广东省广州市黄埔区', NULL, '2025-12-26 18:21:08', '2025-12-26 18:21:08', 0, 0);

-- ----------------------------
-- Table structure for transport
-- ----------------------------
DROP TABLE IF EXISTS `transport`;
CREATE TABLE `transport`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `vehicle_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `vehicle_count` int NULL DEFAULT NULL,
  `total_distance` decimal(10, 2) NULL DEFAULT NULL,
  `month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  `version` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transport
-- ----------------------------
INSERT INTO `transport` VALUES (1, '货车', 120, 45000.00, '2025-12', '运输中', '2025-12-26 18:21:06', '2025-12-26 18:21:06', 0, 0);
INSERT INTO `transport` VALUES (2, '卡车', 85, 38000.00, '2025-12', '运输中', '2025-12-26 18:21:06', '2025-12-26 18:21:06', 0, 0);
INSERT INTO `transport` VALUES (3, '厢式车', 65, 28000.00, '2025-12', '运输中', '2025-12-26 18:21:06', '2025-12-26 18:21:06', 0, 0);
INSERT INTO `transport` VALUES (4, '冷藏车', 35, 15000.00, '2025-12', '已完成', '2025-12-26 18:21:06', '2025-12-26 18:21:06', 0, 0);
INSERT INTO `transport` VALUES (5, '特种车', 25, 12000.00, '2025-12', '已完成', '2025-12-26 18:21:06', '2025-12-26 18:21:06', 0, 0);

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `area` decimal(10, 2) NULL DEFAULT NULL,
  `utilization_rate` decimal(5, 2) NULL DEFAULT NULL,
  `capacity` decimal(10, 2) NULL DEFAULT NULL,
  `throughput` decimal(10, 2) NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  `version` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES (1, '大连仓', '辽宁省大连市', 50000.00, 75.00, 150000.00, 1500000.00, '2025-12-26 18:17:54', '2025-12-26 18:17:54', 0, 0);
INSERT INTO `warehouse` VALUES (2, '青岛仓', '山东省青岛市', 30000.00, 82.00, 80000.00, 500000.00, '2025-12-26 18:17:54', '2025-12-26 18:17:54', 0, 0);
INSERT INTO `warehouse` VALUES (3, '宁波仓', '浙江省宁波市', 28000.00, 68.00, 70000.00, 600000.00, '2025-12-26 18:17:54', '2025-12-26 18:17:54', 0, 0);
INSERT INTO `warehouse` VALUES (4, '上海仓', '上海市', 45000.00, 88.00, 120000.00, 1800000.00, '2025-12-26 18:17:54', '2025-12-26 18:17:54', 0, 0);
INSERT INTO `warehouse` VALUES (5, '广州仓', '广东省广州市', 38000.00, 72.00, 95000.00, 850000.00, '2025-12-26 18:17:54', '2025-12-26 18:17:54', 0, 0);

-- ----------------------------
-- Table structure for warehouse_distribution
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_distribution`;
CREATE TABLE `warehouse_distribution`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `warehouse_count` int NULL DEFAULT NULL,
  `total_capacity` decimal(10, 2) NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warehouse_distribution
-- ----------------------------
INSERT INTO `warehouse_distribution` VALUES (1, '辽宁省', '大连', 45, 150000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (2, '山东省', '青岛', 38, 80000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (3, '浙江省', '宁波', 52, 70000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (4, '上海市', '上海', 68, 120000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (5, '广东省', '广州', 62, 95000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (6, '江苏省', '苏州', 35, 65000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (7, '天津市', '天津', 28, 55000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (8, '福建省', '厦门', 25, 45000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (9, '四川省', '成都', 18, 35000.00, '2025-12-26 18:21:11');
INSERT INTO `warehouse_distribution` VALUES (10, '湖北省', '武汉', 22, 40000.00, '2025-12-26 18:21:11');

SET FOREIGN_KEY_CHECKS = 1;
