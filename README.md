# 物流供应链可视化面板

## 项目简介

这是一个基于Vue.js + Spring Boot + MySQL的物流供应链可视化管理系统，提供仓储管理、客户管理、供应商管理、订单管理、库存管理、运输管理等核心功能，支持数据可视化大屏展示。

## 项目结构

```
大数据面板/
├── FrontEnd/              # 前端Vue.js项目
│   ├── src/              # 源代码
│   ├── public/           # 静态资源
│   ├── Dockerfile        # Docker构建文件
│   └── package.json      # 依赖配置
├── backend/              # 后端Spring Boot项目
│   ├── src/              # 源代码
│   ├── Dockerfile        # Docker构建文件
│   └── pom.xml           # Maven配置
├── deploy/               # Docker部署配置
│   ├── docker-compose.yml      # Docker Compose编排文件
│   ├── start-docker.bat        # Windows启动脚本
│   ├── start-docker.sh         # Linux/Mac启动脚本
│   ├── stop-docker.bat         # Windows停止脚本
│   ├── stop-docker.sh          # Linux/Mac停止脚本
│   └── DOCKER_DEPLOY.md        # Docker部署详细文档
├── docs/                 # 项目文档
│   ├── DEPLOY_LOCAL.md   # 本地部署文档
│   ├── DEPLOY.md         # 通用部署文档
│   └── PROJECT_MANUAL.md # 项目手册
├── logistics_db.sql      # 数据库初始化脚本
├── README.md             # 项目说明（本文件）
└── project.log           # 项目日志
```

## 部署方式

本项目支持两种部署方式：

### 方式一：本地开发部署

适合开发环境和需要自定义配置的场景。

#### 前置要求

- Node.js 18+
- npm 9+
- Java 11+
- Maven 3.8+
- MySQL 8.0+

#### 快速启动

1. **初始化数据库**

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p logistics_db < logistics_db.sql
```

2. **配置后端**

编辑 `backend/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

3. **启动后端**

```bash
cd backend
mvn clean package
java -jar target/*.jar
```

或使用启动脚本：

```bash
# Windows
start-dev.bat

# Linux/Mac
mvn spring-boot:run
```

4. **启动前端**

```bash
cd FrontEnd
npm install
npm run dev
```

5. **访问应用**

- 前端应用: http://localhost:5173
- 后端API: http://localhost:8080/api

详细文档请参考：[本地部署文档](docs/DEPLOY_LOCAL.md)

### 方式二：Docker容器化部署

适合生产环境和快速部署场景，无需手动配置环境。

#### 前置要求

- Docker Desktop 20.10+
- Docker Compose 1.29+
- 至少 4GB 可用内存
- 至少 10GB 可用磁盘空间

#### 快速启动

**Windows系统：**

```bash
cd deploy
start-docker.bat
```

**Linux/Mac系统：**

```bash
cd deploy
chmod +x start-docker.sh
./start-docker.sh
```

#### 访问应用

- 前端应用: http://localhost
- 后端API: http://localhost:8080/api
- MySQL数据库: localhost:3306 (用户名: root, 密码: root)

详细文档请参考：[Docker部署文档](deploy/DOCKER_DEPLOY.md)

## 功能模块

### 1. 仓储管理（Warehouse）
- 仓库信息管理
- 仓库容量监控
- 仓库位置管理

### 2. 客户管理（Customer）
- 客户信息维护
- 客户联系人管理
- 客户地址管理

### 3. 供应商管理（Supplier）
- 供应商信息管理
- 供应商评级管理
- 供应商合作记录

### 4. 订单管理（Order）
- 订单创建和编辑
- 订单状态跟踪
- 订单统计分析

### 5. 库存管理（Inventory）
- 库存实时监控
- 库存预警管理
- 库存出入库记录

### 6. 运输管理（Transport）
- 运输任务管理
- 运输路线规划
- 运输状态跟踪

### 7. 数据可视化大屏
- 仓储数据大屏
- 供应链数据大屏
- 物流数据大屏

## API接口

### 基础信息

- **基础URL**: http://localhost:8080/api
- **认证方式**: 暂无（开发环境）
- **数据格式**: JSON

### 主要接口

| 模块 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 仓储 | /warehouses | GET | 获取所有仓库 |
| 仓储 | /warehouses/{id} | GET | 获取单个仓库 |
| 仓储 | /warehouses | POST | 创建仓库 |
| 仓储 | /warehouses/{id} | PUT | 更新仓库 |
| 仓储 | /warehouses/{id} | DELETE | 删除仓库 |
| 客户 | /customers | GET | 获取所有客户 |
| 客户 | /customers/{id} | GET | 获取单个客户 |
| 客户 | /customers | POST | 创建客户 |
| 客户 | /customers/{id} | PUT | 更新客户 |
| 客户 | /customers/{id} | DELETE | 删除客户 |
| 供应商 | /suppliers | GET | 获取所有供应商 |
| 供应商 | /suppliers/{id} | GET | 获取单个供应商 |
| 供应商 | /suppliers | POST | 创建供应商 |
| 供应商 | /suppliers/{id} | PUT | 更新供应商 |
| 供应商 | /suppliers/{id} | DELETE | 删除供应商 |
| 订单 | /orders | GET | 获取所有订单 |
| 订单 | /orders/{id} | GET | 获取单个订单 |
| 订单 | /orders | POST | 创建订单 |
| 订单 | /orders/{id} | PUT | 更新订单 |
| 订单 | /orders/{id} | DELETE | 删除订单 |
| 库存 | /inventories | GET | 获取所有库存 |
| 库存 | /inventories/{id} | GET | 获取单个库存 |
| 库存 | /inventories | POST | 创建库存 |
| 库存 | /inventories/{id} | PUT | 更新库存 |
| 库存 | /inventories/{id} | DELETE | 删除库存 |
| 运输 | /transports | GET | 获取所有运输 |
| 运输 | /transports/{id} | GET | 获取单个运输 |
| 运输 | /transports | POST | 创建运输 |
| 运输 | /transports/{id} | PUT | 更新运输 |
| 运输 | /transports/{id} | DELETE | 删除运输 |
| 仪表盘 | /dashboard | GET | 获取仪表盘数据 |

详细API文档请参考：[API文档](docs/API.md)

## 技术栈

### 前端技术

- **框架**: Vue.js 3
- **构建工具**: Vite
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **UI组件**: 自定义组件
- **图表库**: ECharts

### 后端技术

- **框架**: Spring Boot 2.7
- **ORM**: MyBatis Plus
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **日志**: Logback
- **监控**: Spring Boot Actuator

### 部署技术

- **容器化**: Docker
- **编排**: Docker Compose
- **反向代理**: Nginx
- **Web服务器**: Nginx

## 环境配置

### 本地开发环境

**前端配置** (.env.development):

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

**后端配置** (application-dev.yml):

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistics_db
    username: root
    password: root
```

### Docker生产环境

**前端配置** (.env.production):

```env
VITE_API_BASE_URL=/api
```

**后端配置** (application-prod.yml):

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://mysql:3306/logistics_db
    username: root
    password: root
```

## 常见问题

### Q1: 本地部署时数据库连接失败

**A**: 检查MySQL服务是否启动，用户名密码是否正确，数据库是否已创建。

```bash
# 检查MySQL服务
mysql -u root -p -e "SHOW DATABASES;"

# 检查数据库
mysql -u root -p logistics_db -e "SHOW TABLES;"
```

### Q2: Docker部署时服务无法启动

**A**: 检查Docker Desktop是否运行，端口是否被占用。

```bash
# 检查Docker状态
docker ps -a

# 查看服务日志
docker-compose logs -f
```

### Q3: 前端无法访问后端API

**A**: 检查后端服务是否正常启动，跨域配置是否正确。

```bash
# 测试后端API
curl http://localhost:8080/api/warehouses
```

### Q4: 如何备份数据

**本地部署**:

```bash
mysqldump -u root -p logistics_db > backup.sql
```

**Docker部署**:

```bash
docker-compose exec mysql mysqldump -uroot -proot logistics_db > backup.sql
```

## 开发指南

### 前端开发

```bash
cd FrontEnd

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 运行测试
npm run test
```

### 后端开发

```bash
cd backend

# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包项目
mvn clean package

# 启动应用
java -jar target/*.jar
```

## 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交Pull Request

## 版本历史

- **v1.0.0** (2025-01-04)
  - 初始版本发布
  - 完成核心功能开发
  - 支持本地部署和Docker部署
  - 完成API接口测试
  - 完成前后端集成测试

## 许可证

本项目采用 MIT 许可证。

## 联系方式

- 项目地址: [GitHub Repository]
- 问题反馈: [Issues]
- 文档地址: [Wiki]

## 致谢

感谢所有为本项目做出贡献的开发者！

---

**最后更新**: 2025-01-04
