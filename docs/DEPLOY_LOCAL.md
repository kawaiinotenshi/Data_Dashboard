# 物流仓储大数据展示系统 - 本地部署指南

## 前置要求

### 1. 安装 Java 开发环境
- **JDK 11 或更高版本**
- 下载地址: https://www.oracle.com/java/technologies/downloads/
- 验证安装:
  ```bash
  java -version
  ```

### 2. 安装 Maven
- **Maven 3.6 或更高版本**
- 下载地址: https://maven.apache.org/download.cgi
- 验证安装:
  ```bash
  mvn -version
  ```

### 3. 安装 Node.js
- **Node.js 16 或更高版本**
- 下载地址: https://nodejs.org/
- 验证安装:
  ```bash
  node -v
  npm -v
  ```

### 4. 安装 MySQL
- **MySQL 8.0**
- 下载地址: https://dev.mysql.com/downloads/mysql/
- 确保服务已启动

## 数据库配置

### 1. 创建数据库
使用 MySQL 客户端或命令行创建数据库:

```sql
CREATE DATABASE logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 初始化数据库表
执行数据库初始化脚本:

```bash
cd c:\Users\15071\Documents\大数据面板
mysql -u root -p logistics_db < database\init.sql
```

或在 MySQL 客户端中执行 `database/init.sql` 文件的内容。

### 3. 验证数据库
```sql
USE logistics_db;
SHOW TABLES;
```

应该看到以下表:
- warehouse
- inventory
- transport
- orders
- supplier
- customer

## 后端部署

### 1. 进入后端目录
```bash
cd c:\Users\15071\Documents\大数据面板\BackEnd
```

### 2. 编译打包
```bash
mvn clean package -DskipTests
```

### 3. 运行后端服务
```bash
java -jar target\logistics-dashboard-1.0.0.jar
```

或者使用 Maven 直接运行:
```bash
mvn spring-boot:run
```

### 4. 验证后端服务
访问: http://localhost:8080/api/warehouses

应该返回仓库数据的 JSON 数组。

## 前端部署

### 1. 进入前端目录
```bash
cd c:\Users\15071\Documents\大数据面板\FrontEnd
```

### 2. 安装依赖
```bash
npm install
```

### 3. 启动开发服务器
```bash
npm run dev
```

### 4. 访问前端应用
打开浏览器访问开发服务器显示的地址，通常是: http://localhost:5173

## 生产环境部署

### 前端构建
```bash
cd c:\Users\15071\Documents\大数据面板\FrontEnd
npm run build
```

构建完成后，`dist` 目录包含所有静态文件，可以部署到任何 Web 服务器（Nginx、Apache 等）。

### 后端部署
后端已经打包为 JAR 文件，可以直接运行:

```bash
java -jar BackEnd\target\logistics-dashboard-1.0.0.jar
```

## 常见问题

### 1. 数据库连接失败
**错误信息**: `Communications link failure`

**解决方案**:
- 确认 MySQL 服务已启动
- 检查 `BackEnd/src/main/resources/application.yml` 中的数据库配置
- 确认用户名密码正确（默认: root/root）

### 2. 端口被占用
**错误信息**: `Port 8080 was already in use`

**解决方案**:
- 修改 `BackEnd/src/main/resources/application.yml` 中的端口号
- 或停止占用 8080 端口的进程

### 3. Maven 依赖下载失败
**解决方案**:
- 检查网络连接
- 配置 Maven 镜像源（阿里云镜像）
- 删除本地仓库中的 `.lastUpdated` 文件后重新下载

### 4. 前端无法连接后端 API
**解决方案**:
- 确认后端服务已启动
- 检查 API 地址配置（默认: http://localhost:8080/api）
- 检查浏览器控制台的跨域错误

## 配置说明

### 后端配置文件
位置: `BackEnd/src/main/resources/application.yml`

关键配置项:
```yaml
server:
  port: 8080  # 后端服务端口

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistics_db  # 数据库连接地址
    username: root  # 数据库用户名
    password: root  # 数据库密码
```

### 前端配置
前端 API 服务配置位于 `FrontEnd/src/api/` 目录下的各个文件。

默认 API 基础地址: `http://localhost:8080/api`

## 目录结构

```
大数据面板/
├── deploy/                     # 部署配置文件
│   ├── docker-compose.yml     # Docker Compose 配置文件
│   ├── docker-compose-db.yml  # Docker 数据库配置文件
│   ├── pytest.ini             # Pytest 配置文件
│   └── requirements-test.txt  # Python 测试依赖
├── docs/                       # 文档目录
│   ├── CHANGELOG.md           # 项目变更日志
│   ├── DEPLOY.md              # Docker 部署文档
│   ├── DEPLOY_LOCAL.md        # 本地部署文档
│   ├── DOCKER_DB.md           # Docker 数据库部署文档
│   └── 环境配置参考.md         # 环境配置参考文档
├── scripts/                    # 脚本目录
│   ├── log-utility.ps1        # 日志工具脚本
│   ├── start-docker-db.ps1    # 启动 Docker 数据库脚本
│   └── stop-docker-db.ps1     # 停止 Docker 数据库脚本
├── FrontEnd/                   # 前端项目
│   ├── Dockerfile              # 前端 Docker 镜像构建文件
│   ├── nginx.conf              # Nginx 配置文件
│   └── .dockerignore           # Docker 忽略文件
├── BackEnd/                    # 后端项目
│   ├── Dockerfile              # 后端 Docker 镜像构建文件
│   └── .dockerignore           # Docker 忽略文件
└── database/                   # 数据库脚本
    └── init.sql                # 数据库初始化脚本
```

## 系统架构

```
┌─────────────────┐
│   前端 (Vue 3)  │
│   Port: 5173    │
└────────┬────────┘
         │ HTTP
         ↓
┌─────────────────┐
│  后端 (Spring    │
│      Boot)      │
│   Port: 8080    │
└────────┬────────┘
         │ JDBC
         ↓
┌─────────────────┐
│  MySQL 8.0      │
│   Port: 3306    │
└─────────────────┘
```

## 性能优化建议

### 1. 数据库优化
- 为常用查询字段添加索引
- 定期清理历史数据
- 配置连接池参数

### 2. 后端优化
- 启用缓存机制
- 优化 SQL 查询
- 使用异步处理

### 3. 前端优化
- 启用 Gzip 压缩
- 使用 CDN 加速
- 优化图片和静态资源

## 监控和日志

### 后端日志
后端日志输出到控制台，包含:
- SQL 查询日志
- API 请求日志
- 错误日志

### 前端日志
前端日志输出到浏览器控制台，包含:
- API 请求和响应
- 错误信息
- 调试信息

## 技术支持

如有问题，请检查:
1. 所有服务是否正常启动
2. 端口是否被占用
3. 数据库连接是否正常
4. 配置文件是否正确
5. 查看日志文件获取详细错误信息
