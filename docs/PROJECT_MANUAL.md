# 物流仓储大数据展示系统 - 项目框架与使用手册

## 项目概述

物流仓储大数据展示系统是一个基于现代Web技术栈构建的物流仓储数据可视化平台，提供实时数据监控、数据分析和可视化展示功能。

### 核心特性

- 实时数据展示：仓库信息、库存占比、订单数据等
- 可视化图表：使用ECharts实现多种图表展示
- 响应式设计：支持多设备访问
- 容器化部署：支持Docker和Docker Compose部署
- 自动化测试：集成Playwright端到端测试
- 可视化启动器：简化环境配置和服务管理

## 技术架构

### 技术栈

| 层次 | 技术 | 版本 |
|------|------|------|
| 前端框架 | Vue.js | 3.x |
| 前端构建 | Vite | 最新 |
| 前端图表 | ECharts | 5.x |
| 后端框架 | Spring Boot | 2.7.x |
| 后端ORM | MyBatis-Plus | 最新 |
| 数据库 | MySQL | 8.0 |
| 容器化 | Docker & Docker Compose | 最新 |
| 测试框架 | Playwright | 1.57.0 |
| 测试运行 | Pytest | 最新 |

### 系统架构

```
┌─────────────────────────────────────────────────────────┐
│                    用户浏览器                              │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP
                     ↓
┌─────────────────────────────────────────────────────────┐
│              前端 (Vue 3 + ECharts)                      │
│              Port: 3000 (开发) / 80 (生产)               │
├─────────────────────────────────────────────────────────┤
│  - 左侧面板：基本信息、库存占比、清关数据                 │
│  - 中间面板：地图展示、入库出库趋势、季度统计             │
│  - 右侧面板：仓库信息、清关列表、利用比                   │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP API
                     ↓
┌─────────────────────────────────────────────────────────┐
│            后端 (Spring Boot + MyBatis-Plus)            │
│                    Port: 8080                           │
├─────────────────────────────────────────────────────────┤
│  Controller Layer: RESTful API接口                       │
│  Service Layer: 业务逻辑处理                             │
│  Mapper Layer: 数据访问层                                │
│  GlobalExceptionHandler: 统一异常处理                    │
└────────────────────┬────────────────────────────────────┘
                     │ JDBC
                     ↓
┌─────────────────────────────────────────────────────────┐
│                  MySQL 8.0                              │
│                  Port: 3306/3307                         │
├─────────────────────────────────────────────────────────┤
│  - warehouse: 仓库信息表                                 │
│  - inventory_ratio: 库存占比表                           │
│  - orders: 订单表                                        │
│  - transport: 运输表                                     │
│  - supplier: 供应商表                                    │
│  - customer: 客户表                                      │
│  - customs_clearance: 清关表                            │
│  - in_out_record: 出入库记录表                          │
│  - business_quarterly: 季度业务表                       │
│  - warehouse_distribution: 仓库分布表                    │
└─────────────────────────────────────────────────────────┘
```

## 目录结构

```
大数据面板/
├── FrontEnd/                      # 前端项目
│   ├── public/                    # 静态资源
│   │   ├── a.png
│   │   ├── b.png
│   │   ├── bj.png
│   │   └── ksh33.png
│   ├── src/                       # 源代码
│   │   ├── api/                   # API接口
│   │   │   ├── customer.js
│   │   │   ├── inventory.js
│   │   │   ├── order.js
│   │   │   ├── supplier.js
│   │   │   ├── transport.js
│   │   │   └── warehouse.js
│   │   ├── components/            # Vue组件
│   │   │   ├── CenterPanel.vue
│   │   │   ├── LeftPanel.vue
│   │   │   ├── LoadingSpinner.vue
│   │   │   └── RightPanel.vue
│   │   ├── utils/                 # 工具函数
│   │   │   └── request.js
│   │   ├── App.vue                # 主应用组件
│   │   ├── main.js                # 入口文件
│   │   └── style.css              # 全局样式
│   ├── .dockerignore              # Docker忽略文件
│   ├── .gitignore                 # Git忽略文件
│   ├── .prettierrc                # Prettier配置
│   ├── Dockerfile                 # Docker镜像构建文件
│   ├── README.md                  # 前端文档
│   ├── eslint.config.js           # ESLint配置
│   ├── index.html                 # HTML入口
│   ├── nginx.conf                 # Nginx配置
│   ├── package.json               # 项目配置
│   ├── package-lock.json          # 依赖锁定
│   ├── project.log                # 项目日志
│   └── vite.config.js             # Vite配置
├── backend/                       # 后端项目
│   ├── src/                       # 源代码
│   │   └── main/
│   │       ├── java/com/logistics/
│   │       │   ├── common/       # 公共模块
│   │       │   │   ├── enums/     # 枚举类
│   │       │   │   │   └── ErrorCode.java
│   │       │   │   ├── exception/ # 异常类
│   │       │   │   │   ├── BusinessException.java
│   │       │   │   │   ├── DuplicateResourceException.java
│   │       │   │   │   ├── ResourceNotFoundException.java
│   │       │   │   │   └── ValidationException.java
│   │       │   │   ├── PageResult.java
│   │       │   │   └── Result.java
│   │       │   ├── config/       # 配置类
│   │       │   │   ├── GlobalExceptionHandler.java
│   │       │   │   ├── MyBatisPlusConfig.java
│   │       │   │   └── P6SpyLogger.java
│   │       │   ├── controller/   # 控制器
│   │       │   │   ├── CustomerController.java
│   │       │   │   ├── InventoryController.java
│   │       │   │   ├── OrderController.java
│   │       │   │   ├── SupplierController.java
│   │       │   │   ├── TransportController.java
│   │       │   │   └── WarehouseController.java
│   │       │   ├── entity/       # 实体类
│   │       │   │   ├── base/
│   │       │   │   │   └── BaseEntity.java
│   │       │   │   ├── Customer.java
│   │       │   │   ├── Inventory.java
│   │       │   │   ├── Order.java
│   │       │   │   ├── Supplier.java
│   │       │   │   ├── Transport.java
│   │       │   │   └── Warehouse.java
│   │       │   ├── generator/    # 代码生成器
│   │       │   │   ├── CodeGenerator.java
│   │       │   │   └── EnhancedCodeGenerator.java
│   │       │   ├── handler/      # 处理器
│   │       │   │   └── MyMetaObjectHandler.java
│   │       │   ├── interceptor/   # 拦截器
│   │       │   │   └── SqlPerformanceInterceptor.java
│   │       │   ├── mapper/       # Mapper接口
│   │       │   │   ├── CustomerMapper.java
│   │       │   │   ├── InventoryMapper.java
│   │       │   │   ├── OrderMapper.java
│   │       │   │   ├── SupplierMapper.java
│   │       │   │   ├── TransportMapper.java
│   │       │   │   └── WarehouseMapper.java
│   │       │   ├── service/      # 服务层
│   │       │   │   ├── impl/
│   │       │   │   │   ├── CustomerServiceImpl.java
│   │       │   │   │   ├── InventoryServiceImpl.java
│   │       │   │   │   ├── OrderServiceImpl.java
│   │       │   │   │   ├── SupplierServiceImpl.java
│   │       │   │   │   ├── TransportServiceImpl.java
│   │       │   │   │   └── WarehouseServiceImpl.java
│   │       │   │   ├── CustomerService.java
│   │       │   │   ├── InventoryService.java
│   │       │   │   ├── OrderService.java
│   │       │   │   ├── SupplierService.java
│   │       │   │   ├── TransportService.java
│   │       │   │   └── WarehouseService.java
│   │       │   ├── vo/           # 视图对象
│   │       │   │   ├── CustomerRequestVO.java
│   │       │   │   ├── CustomerVO.java
│   │       │   │   ├── InventoryRequestVO.java
│   │       │   │   ├── InventoryVO.java
│   │       │   │   ├── OrderRequestVO.java
│   │       │   │   ├── OrderVO.java
│   │       │   │   ├── SupplierRequestVO.java
│   │       │   │   ├── SupplierVO.java
│   │       │   │   ├── TransportRequestVO.java
│   │       │   │   ├── TransportVO.java
│   │       │   │   ├── WarehouseRequestVO.java
│   │       │   │   └── WarehouseVO.java
│   │       │   └── LogisticsApplication.java
│   │       └── resources/
│   │           ├── mapper/       # MyBatis XML映射文件
│   │           │   ├── CustomerMapper.xml
│   │           │   ├── InventoryMapper.xml
│   │           │   ├── OrderMapper.xml
│   │           │   ├── SupplierMapper.xml
│   │           │   ├── TransportMapper.xml
│   │           │   └── WarehouseMapper.xml
│   │           ├── templates/     # 代码生成器模板
│   │           │   ├── entity.java
│   │           │   ├── mapper.java
│   │           │   ├── mapper.xml
│   │           │   ├── service.java
│   │           │   └── serviceImpl.java
│   │           ├── application.yml    # 应用配置
│   │           ├── generator.properties # 代码生成器配置
│   │           └── spy.properties     # P6Spy配置
│   ├── .dockerignore              # Docker忽略文件
│   ├── .gitignore                 # Git忽略文件
│   ├── CODE_GENERATOR_GUIDE.md     # 代码生成器指南
│   ├── Dockerfile                 # Docker镜像构建文件
│   └── pom.xml                    # Maven配置
├── database/                      # 数据库脚本
│   ├── fix_schema.sql             # 数据库修复脚本
│   ├── init.sql                   # 数据库初始化脚本
│   └── update_base_entity.sql     # 基础实体更新脚本
├── deploy/                        # 部署配置
│   ├── docker-compose.yml         # Docker Compose完整配置
│   ├── docker-compose-db.yml      # Docker Compose数据库配置
│   ├── pytest.ini                 # Pytest配置
│   └── requirements-test.txt      # 测试依赖
├── docs/                          # 文档目录
│   ├── CHANGELOG.md               # 项目变更日志
│   ├── DEPLOY.md                  # Docker部署文档
│   ├── DEPLOY_LOCAL.md            # 本地部署文档
│   ├── DOCKER_DB.md               # Docker数据库文档
│   ├── LAUNCHER_GUIDE.md          # 启动器使用指南
│   ├── MCP_MYSQL_FIX.md           # MCP MySQL配置修复
│   ├── MYBATIS_PLUS_REFACTOR.md   # MyBatis-Plus重构文档
│   └── 环境配置参考.md             # 环境配置参考
├── scripts/                       # 脚本目录
│   ├── log-utility.ps1            # 日志工具脚本
│   ├── start-docker-db.ps1        # 启动Docker数据库脚本
│   └── stop-docker-db.ps1         # 停止Docker数据库脚本
├── tests/                         # 测试目录
│   ├── README.md                  # 测试文档
│   ├── conftest.py                # 测试配置
│   ├── test_api.py                # API测试
│   ├── test_dashboard.py           # 仪表盘测试
│   ├── test_e2e.py                # 端到端测试
│   └── test_screenshot.py         # 截图测试
├── .gitignore                     # Git忽略文件
├── README.md                      # 项目说明
├── launcher.py                    # 项目启动器
├── launcher_config.json           # 启动器配置
└── 启动器.bat                     # 启动器批处理文件
```

## 快速开始

### 前置要求

- Python 3.7+ (用于运行启动器)
- Java 8+ (用于运行后端)
- Node.js 16+ (用于运行前端)
- MySQL 8.0 (数据库)
- Docker & Docker Compose (可选，用于容器化部署)

### 方式一：使用启动器（推荐）

1. 双击运行 `启动器.bat` 文件
2. 启动器会自动检查环境
3. 点击"一键启动所有服务"按钮
4. 等待服务启动完成
5. 点击"打开前端页面"访问系统

### 方式二：手动启动

#### 1. 启动数据库

**使用本地MySQL**:
```bash
# 确保MySQL服务已启动
# 创建数据库
mysql -u root -p
CREATE DATABASE logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p logistics_db < database/init.sql
```

**使用Docker MySQL**:
```bash
docker run -d --name logistics-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=logistics_db \
  -p 3307:3306 \
  mysql:8.0
```

#### 2. 启动后端

```bash
cd backend
mvn clean package -DskipTests
java -jar target/logistics-dashboard-1.0.0.jar
```

后端服务将在 `http://localhost:8080` 启动

#### 3. 启动前端

```bash
cd FrontEnd
npm install
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

### 方式三：Docker Compose部署

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 详细使用说明

### 前端功能

#### 左侧面板
- **基本信息**: 显示企业基本信息
- **库存占比**: 使用饼图展示各类库存占比
- **BBC清关**: 使用柱状图展示清关数据

#### 中间面板
- **全国仓库地图**: 使用地图展示全国仓库分布
- **入库出库趋势**: 使用折线图展示入库出库趋势
- **季度数据统计**: 使用柱状图展示季度业务数据

#### 右侧面板
- **仓库/进关信息**: 支持切换显示仓库信息或进关信息
- **BBC清关滚动列表**: 滚动显示清关记录
- **仓库利用比**: 使用仪表盘展示仓库利用率

### 后端API

#### 仓库管理
- `GET /api/warehouse/list` - 获取仓库列表
- `GET /api/warehouse/{id}` - 获取仓库详情
- `POST /api/warehouse` - 创建仓库
- `PUT /api/warehouse/{id}` - 更新仓库
- `DELETE /api/warehouse/{id}` - 删除仓库

#### 库存管理
- `GET /api/inventory/list` - 获取库存列表
- `GET /api/inventory/{id}` - 获取库存详情
- `POST /api/inventory` - 创建库存
- `PUT /api/inventory/{id}` - 更新库存
- `DELETE /api/inventory/{id}` - 删除库存

#### 订单管理
- `GET /api/order/list` - 获取订单列表
- `GET /api/order/{id}` - 获取订单详情
- `POST /api/order` - 创建订单
- `PUT /api/order/{id}` - 更新订单
- `DELETE /api/order/{id}` - 删除订单

#### 供应商管理
- `GET /api/supplier/list` - 获取供应商列表
- `GET /api/supplier/{id}` - 获取供应商详情
- `POST /api/supplier` - 创建供应商
- `PUT /api/supplier/{id}` - 更新供应商
- `DELETE /api/supplier/{id}` - 删除供应商

#### 客户管理
- `GET /api/customer/list` - 获取客户列表
- `GET /api/customer/{id}` - 获取客户详情
- `POST /api/customer` - 创建客户
- `PUT /api/customer/{id}` - 更新客户
- `DELETE /api/customer/{id}` - 删除客户

#### 运输管理
- `GET /api/transport/list` - 获取运输列表
- `GET /api/transport/{id}` - 获取运输详情
- `POST /api/transport` - 创建运输
- `PUT /api/transport/{id}` - 更新运输
- `DELETE /api/transport/{id}` - 删除运输

## 配置说明

### 后端配置

文件位置: `backend/src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3307/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: root

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl
  global-config:
    db-config:
      logic-delete-field: isDeleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 前端配置

文件位置: `FrontEnd/src/utils/request.js`

```javascript
const BASE_URL = 'http://localhost:8080/api';
```

### 启动器配置

文件位置: `launcher_config.json`

```json
{
  "backend_path": "backend",
  "frontend_path": "FrontEnd",
  "backend_port": 8080,
  "frontend_port": 3000,
  "mysql_port": 3307,
  "mysql_user": "root",
  "mysql_password": "root",
  "database_name": "logistics_db"
}
```

## 部署指南

### 本地部署

详见 [DEPLOY_LOCAL.md](file:///c:/Users/15071/Documents/大数据面板/docs/DEPLOY_LOCAL.md)

### Docker部署

详见 [DEPLOY.md](file:///c:/Users/15071/Documents/大数据面板/docs/DEPLOY.md)

### Docker数据库部署

详见 [DOCKER_DB.md](file:///c:/Users/15071/Documents/大数据面板/docs/DOCKER_DB.md)

## 测试

### 运行测试

```bash
# 安装测试依赖
python -m pip install -r deploy/requirements-test.txt

# 安装Playwright浏览器
python -m playwright install chromium

# 运行所有测试
python -m pytest tests/ -v --headed

# 运行特定测试
python -m pytest tests/test_dashboard.py -v --headed
```

详见 [tests/README.md](file:///c:/Users/15071/Documents/大数据面板/tests/README.md)

## 常见问题

### 1. 端口被占用

**问题**: 启动服务时提示端口被占用

**解决方案**:
- 使用启动器自动检测并清理端口占用
- 或手动修改配置文件中的端口号
- 或手动停止占用端口的进程

### 2. 数据库连接失败

**问题**: 后端无法连接到数据库

**解决方案**:
- 确认MySQL服务已启动
- 检查数据库连接配置（主机、端口、用户名、密码）
- 确认数据库已创建并初始化

### 3. 前端无法访问后端API

**问题**: 前端显示数据加载失败

**解决方案**:
- 确认后端服务已启动
- 检查前端API配置地址是否正确
- 查看浏览器控制台的网络请求错误

### 4. MCP MySQL连接失败

**问题**: MCP工具无法连接到MySQL

**解决方案**:
- 确认MySQL服务已启动
- 检查MCP配置中的数据库连接信息
- 确保使用正确的启动命令: `python -m uv tool run --from mysql-mcp-server mysql_mcp_server`

详见 [MCP_MYSQL_FIX.md](file:///c:/Users/15071/Documents/大数据面板/docs/MCP_MYSQL_FIX.md)

### 5. 启动器无法启动

**问题**: 双击启动器无反应

**解决方案**:
- 确认Python已安装并添加到PATH
- 在命令行中运行 `python launcher.py` 查看错误
- 检查是否有权限问题

## 更新日志

详见 [CHANGELOG.md](file:///c:/Users/15071/Documents/大数据面板/docs/CHANGELOG.md)

## 技术支持

### 文档
- [启动器使用指南](file:///c:/Users/15071/Documents/大数据面板/docs/LAUNCHER_GUIDE.md)
- [MyBatis-Plus重构文档](file:///c:/Users/15071/Documents/大数据面板/docs/MYBATIS_PLUS_REFACTOR.md)
- [代码生成器指南](file:///c:/Users/15071/Documents/大数据面板/backend/CODE_GENERATOR_GUIDE.md)

### 联系方式
- 提交Issue到项目仓库
- 查看项目文档获取更多信息

## 许可证

本项目仅供学习和参考使用。

## 贡献指南

欢迎提交Issue和Pull Request来改进项目。

---

**最后更新**: 2025-12-28
