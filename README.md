# Data_Dashboard

物流仓储大数据可视化展示系统

## 项目介绍

这是一个完整的物流仓储大数据可视化展示系统，包含前端Vue 3应用和后端Spring Boot服务，支持实时数据展示、仓库管理、订单追踪等功能。

## 技术栈

### 前端
- Vue 3 (Composition API)
- Vite
- ECharts 5
- jQuery (兼容原项目)
- Axios

### 后端
- Spring Boot 3.x
- MyBatis-Plus
- MySQL 8.0
- P6Spy (SQL性能监控)

## 项目结构

```
Data_Dashboard/
├── FrontEnd/              # 前端Vue应用
│   ├── src/
│   │   ├── components/    # Vue组件
│   │   ├── api/          # API接口
│   │   └── utils/        # 工具函数
│   ├── public/           # 静态资源
│   ├── package.json      # 前端依赖配置
│   └── vite.config.js    # Vite配置
├── backend/              # 后端Spring Boot应用
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/logistics/
│   │   │   │       ├── controller/    # 控制器
│   │   │   │       ├── service/       # 服务层
│   │   │   │       ├── mapper/        # 数据访问层
│   │   │   │       ├── entity/        # 实体类
│   │   │   │       ├── vo/            # 视图对象
│   │   │   │       ├── config/        # 配置类
│   │   │   │       ├── filter/        # 过滤器
│   │   │   │       └── util/          # 工具类
│   │   │   └── resources/
│   │   │       ├── mapper/            # MyBatis映射文件
│   │   │       └── application.yml    # 应用配置
│   └── pom.xml          # Maven依赖配置
├── database/            # 数据库脚本
├── deploy/              # 部署配置
│   ├── docker-compose.yml
│   └── docker-compose-db.yml
├── docs/                # 文档
│   ├── CHANGELOG.md
│   ├── DEPLOY.md
│   └── PROJECT_MANUAL.md
├── scripts/             # 脚本文件
└── tests/               # 测试文件
```

## 快速开始

### 前置要求
- Node.js 18+
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Docker (可选)

### 启动数据库

使用Docker启动MySQL数据库：

```bash
cd deploy
docker-compose -f docker-compose-db.yml up -d
```

### 启动后端服务

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 启动前端服务

```bash
cd FrontEnd
npm install
npm run dev
```

## 功能模块

### 前端功能
1. **头部** - 实时时间显示
2. **左侧面板** - 基本信息展示、企业库存占比饼图、BBC清关柱状图
3. **中间面板** - 全国仓库地图、入库出库趋势、季度数据统计
4. **右侧面板** - 仓库/进关信息切换、BBC清关滚动列表、仓库利用比仪表盘

### 后端功能
- 客户管理
- 供应商管理
- 仓库管理
- 库存管理
- 订单管理
- 运输管理
- SQL注入防护
- XSS攻击防护

## 安全特性

- SQL注入防护：使用MyBatis-Plus预编译语句和自定义SQL注入检测工具
- XSS防护：实现XSS过滤器，自动清理用户输入
- 参数验证：使用Jakarta Validation API进行输入验证
- 日志记录：P6Spy监控SQL执行性能

## 部署

详细部署说明请参考 [DEPLOY.md](docs/DEPLOY.md)

## 开发指南

详细开发指南请参考 [PROJECT_MANUAL.md](docs/PROJECT_MANUAL.md)

## 许可证

MIT License
