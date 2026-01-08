# 大数据面板项目使用手册

## 1. 项目概述

### 1.1 项目背景与目标

本项目是一个基于前后端分离架构的物流大数据可视化面板系统，旨在为物流企业提供全面的数据管理和分析功能。系统整合了订单管理、客户管理、库存管理、运输管理等核心业务模块，并通过丰富的数据可视化图表直观展示业务数据，帮助企业管理层做出更明智的决策。

### 1.2 核心特点

- **前后端分离架构**：采用Vue.js前端和Spring Boot后端的分离架构，提高开发效率和系统可维护性
- **完整的数据管理**：支持客户、订单、库存、运输、仓库等核心业务数据的增删改查操作
- **丰富的数据可视化**：使用ECharts实现多种图表展示，包括趋势图、柱状图、饼图、地图等
- **完善的权限管理**：基于JWT的身份认证和基于角色的访问控制机制
- **容器化部署**：支持Docker容器化部署，简化环境配置和部署流程
- **RESTful API接口**：提供标准化的API接口，方便第三方系统集成
- **高性能设计**：使用Redis缓存、数据库连接池等技术优化系统性能

## 2. 技术栈与版本

### 2.1 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.0 | 后端应用框架 |
| MyBatis-Plus | 3.5.0 | ORM框架，简化数据库操作 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.0 | 缓存中间件，提高系统性能 |
| Maven | 3.6.3 | 项目构建工具 |
| JWT | - | 身份认证机制 |
| Lombok | - | 简化Java代码 |
| HikariCP | - | 高性能数据库连接池 |

### 2.2 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue.js | 3.0 | 前端框架 |
| Vite | 2.0 | 前端构建工具 |
| Element Plus | - | UI组件库 |
| Axios | - | HTTP客户端，用于API请求 |
| ECharts | - | 数据可视化图表库 |
| Vue Router | - | 路由管理 |
| Pinia | - | 状态管理 |

### 2.3 开发与部署工具

- IDE：IntelliJ IDEA / VS Code
- 版本控制：Git
- 容器化：Docker / Docker Compose
- CI/CD：GitHub Actions

## 3. 项目结构

### 3.1 目录结构

```
大数据面板/
├── backend/                # 后端项目目录
│   ├── src/               # 后端源代码
│   │   ├── main/java/com/logistics/  # 主代码包
│   │   │   ├── annotation/     # 自定义注解
│   │   │   ├── aspect/         # AOP切面
│   │   │   ├── common/         # 通用组件
│   │   │   ├── config/         # 配置类
│   │   │   ├── controller/     # 控制器
│   │   │   ├── entity/         # 实体类
│   │   │   ├── filter/         # 过滤器
│   │   │   ├── handler/        # 处理器
│   │   │   ├── interceptor/    # 拦截器
│   │   │   ├── mapper/         # Mapper接口
│   │   │   ├── service/        # 业务逻辑层
│   │   │   ├── util/           # 工具类
│   │   │   ├── vo/             # 视图对象
│   │   │   └── LogisticsApplication.java  # 启动类
│   │   └── main/resources/     # 资源文件
│   │       ├── mapper/         # MyBatis映射文件
│   │       ├── application.yml # 主配置文件
│   │       └── application-dev.yml # 开发环境配置
│   ├── Dockerfile         # Docker构建文件
│   └── pom.xml            # Maven配置文件
├── FrontEnd/              # 前端项目目录
│   ├── src/               # 前端源代码
│   │   ├── api/           # API接口定义
│   │   ├── assets/        # 资源文件
│   │   ├── components/    # Vue组件
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # Pinia状态管理
│   │   ├── utils/         # 工具函数
│   │   ├── views/         # 页面视图
│   │   ├── App.vue        # 根组件
│   │   └── main.js        # 入口文件
│   ├── Dockerfile         # Docker构建文件
│   ├── package.json       # 项目配置
│   └── vite.config.js     # Vite配置
├── deploy/                # 部署配置
│   ├── docker-compose.yml # Docker Compose配置
│   └── docker-compose-db.yml # 仅数据库的Docker配置
├── logistics_db.sql       # 完整数据库初始化脚本
├── backend/create_user_table.sql # 用户表创建脚本
├── start_project.bat      # 项目启动脚本
└── PROJECT_MANUAL.md      # 项目使用手册
```

### 3.2 核心模块说明

#### 3.2.1 后端核心模块

| 模块 | 主要职责 | 关键文件 |
|------|----------|----------|
| 控制器层 | 处理HTTP请求，返回响应结果 | backend/src/main/java/com/logistics/controller/
| 服务层 | 实现业务逻辑，调用数据访问层 | backend/src/main/java/com/logistics/service/
| 数据访问层 | 与数据库交互，执行CRUD操作 | backend/src/main/java/com/logistics/mapper/
| 实体层 | 定义数据模型，映射数据库表 | backend/src/main/java/com/logistics/entity/
| 视图对象层 | 定义API请求和响应的数据结构 | backend/src/main/java/com/logistics/vo/
| 配置层 | 系统配置，包括数据源、Redis等 | backend/src/main/java/com/logistics/config/
| 工具层 | 通用工具类，如JWT、Redis工具等 | backend/src/main/java/com/logistics/util/

#### 3.2.2 前端核心模块

| 模块 | 主要职责 | 关键文件 |
|------|----------|----------|
| 组件层 | 可复用的UI组件 | FrontEnd/src/components/
| 视图层 | 页面级组件，组合子组件 | FrontEnd/src/views/
| 路由层 | 管理页面路由 | FrontEnd/src/router/index.js
| 状态管理层 | 管理应用状态 | FrontEnd/src/stores/
| API层 | 定义与后端交互的接口 | FrontEnd/src/api/index.js
| 工具层 | 通用工具函数 | FrontEnd/src/utils/

## 4. 环境要求与安装

### 4.1 环境要求

| 环境 | 版本 | 说明 |
|------|------|------|
| JDK | 8或以上 | 后端开发和运行环境 |
| Node.js | 16或以上 | 前端开发和运行环境 |
| Maven | 3.6或以上 | 后端项目构建工具 |
| MySQL | 8.0或以上 | 关系型数据库 |
| Redis | 7.0或以上 | 缓存中间件 |
| Git | 2.0或以上 | 版本控制工具 |
| Docker | 19.03或以上 | 容器化部署工具（可选） |

### 4.2 环境安装

#### 4.2.1 JDK安装

1. 下载JDK 8或以上版本：[Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html)或[OpenJDK](https://adoptopenjdk.net/)
2. 安装并配置环境变量：
   - JAVA_HOME：JDK安装路径
   - PATH：添加%JAVA_HOME%\bin

#### 4.2.2 Node.js安装

1. 下载Node.js 16或以上版本：[Node.js官网](https://nodejs.org/)
2. 安装并验证：
   ```bash
   node -v
   npm -v
   ```

#### 4.2.3 Maven安装

1. 下载Maven 3.6或以上版本：[Maven官网](https://maven.apache.org/)
2. 安装并配置环境变量：
   - MAVEN_HOME：Maven安装路径
   - PATH：添加%MAVEN_HOME%\bin

#### 4.2.4 MySQL安装

1. 下载MySQL 8.0或以上版本：[MySQL官网](https://www.mysql.com/)
2. 安装并配置：
   - 创建数据库：`logistics_db`
   - 设置字符集：utf8mb4
   - 创建用户并授权

#### 4.2.5 Redis安装

1. 下载Redis 7.0或以上版本：[Redis官网](https://redis.io/)
2. 安装并启动Redis服务

## 5. 项目配置与启动

### 5.1 项目配置

#### 5.1.1 后端配置

1. 进入`backend/src/main/resources`目录
2. 修改`application-dev.yml`文件（开发环境）：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
       username: root
       password: root
     redis:
       host: localhost
       port: 6379
       password: 
   server:
     port: 8081
   ```

#### 5.1.2 前端配置

1. 进入`FrontEnd`目录
2. 修改`.env.development`文件（开发环境）：
   ```env
   VITE_API_BASE_URL = 'http://localhost:8081/api'
   VITE_API_TIMEOUT = 10000
   ```

### 5.2 项目启动

#### 5.2.1 使用启动脚本（推荐）

1. 确保所有环境已安装配置完成
2. 双击运行根目录下的`start_project.bat`脚本
3. 脚本会自动检查环境并启动前后端服务
4. 等待启动完成后，根据提示访问相应地址

#### 5.2.2 手动启动

##### 启动后端服务

1. 进入`backend`目录
2. 执行Maven命令：
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
3. 等待服务启动完成

##### 启动前端服务

1. 进入`FrontEnd`目录
2. 安装依赖：
   ```bash
   npm install
   ```
3. 启动开发服务器：
   ```bash
   npm run dev
   ```
4. 等待服务启动完成

### 5.3 访问地址

- 前端面板：http://localhost:3000
- 后端API：http://localhost:8081
- 后端管理接口：http://localhost:8081/api

## 6. 数据库设计

### 6.1 数据库概述

本项目使用MySQL数据库，数据库名为`logistics_db`，采用InnoDB存储引擎，字符集为utf8mb4，支持完整的中文字符存储。

### 6.2 核心表结构

#### 6.2.1 基础实体设计

所有实体表都继承了以下基础字段设计：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键，自增 |
| created_at | datetime | 创建时间，默认当前时间 |
| updated_at | datetime | 更新时间，默认当前时间，更新时自动更新 |
| is_deleted | tinyint | 逻辑删除标识，0-未删除，1-已删除 |
| version | int | 乐观锁版本号 |

#### 6.2.2 核心业务表

1. **客户表（customer）**
   - 存储客户基本信息，包括名称、联系人、电话、地址等

2. **订单表（order）**
   - 存储订单信息，包括订单号、客户ID、产品信息、数量、价格等

3. **库存表（inventory）**
   - 存储库存信息，包括产品ID、仓库ID、数量、状态等

4. **运输表（transport）**
   - 存储运输信息，包括运输方式、起点、终点、状态等

5. **仓库表（warehouse）**
   - 存储仓库信息，包括仓库名称、地址、负责人等

6. **部门表（department）**
   - 存储部门信息，包括部门名称、描述等

7. **员工表（employee）**
   - 存储员工信息，包括姓名、部门ID、薪资、入职日期等

8. **用户表（user）**
   - 存储系统用户信息，包括用户名、密码、角色等

### 6.3 数据库初始化

项目提供了完整的数据库初始化脚本：

1. **logistics_db.sql**：包含所有表结构和初始化数据
2. **backend/create_user_table.sql**：仅包含用户表和初始管理员用户

可以通过以下方式初始化数据库：

1. 使用MySQL客户端导入`logistics_db.sql`文件
2. 使用Docker部署时，系统会自动导入数据库脚本

## 7. Docker部署

### 7.1 环境要求

- Docker：19.03或以上版本
- Docker Compose：1.27或以上版本

### 7.2 部署步骤

#### 7.2.1 完整部署（推荐）

1. 进入项目根目录
2. 执行以下命令：
   ```bash
   cd deploy
   docker-compose up -d
   ```
3. 等待所有容器启动完成（约1-2分钟）
4. 访问地址：
   - 前端面板：http://localhost
   - 后端API：http://localhost:8080
   - Grafana监控：http://localhost:3000（用户名/密码：admin/admin）

#### 7.2.2 仅部署数据库

如果只需要部署数据库服务：

```bash
cd deploy
docker-compose -f docker-compose-db.yml up -d
```

### 7.3 Docker容器说明

| 容器名称 | 服务 | 端口 | 说明 |
|---------|------|------|------|
| logistics-redis | Redis缓存 | 6379:6379 | 用于缓存数据，提高系统性能 |
| logistics-mysql | MySQL数据库 | 3307:3306 | 存储系统数据，端口映射到宿主机3307 |
| logistics-backend | 后端服务 | 8080:8080 | 提供API接口，端口映射到宿主机8080 |
| logistics-frontend | 前端服务 | 80:80 | 提供Web界面，端口映射到宿主机80 |
| logistics-prometheus | Prometheus监控 | 9090:9090 | 监控系统指标 |
| logistics-grafana | Grafana可视化 | 3000:3000 | 展示监控数据图表 |

### 7.4 Docker配置说明

- 所有容器使用自定义网络`logistics-network`进行通信
- 数据持久化：使用Docker卷保存MySQL、Redis等数据
- 健康检查：为每个容器配置了健康检查机制
- 资源限制：为每个容器配置了CPU和内存使用限制
- 日志管理：配置了日志轮转和大小限制

## 8. API文档

### 8.1 API接口规范

本项目采用RESTful风格的API设计，接口命名遵循以下规范：

- 使用HTTP动词表示操作类型：GET（查询）、POST（创建）、PUT（更新）、DELETE（删除）
- 使用名词表示资源：如/users、/orders、/inventory等
- 使用复数形式表示资源集合：如/users而不是/user
- 使用查询参数进行过滤、排序和分页：如/users?page=1&size=10&sort=name,asc

### 8.2 API响应格式

所有API接口返回统一的响应格式：

```json
{
  "code": 200,           // 响应码，200表示成功
  "message": "操作成功",  // 响应消息
  "data": {}             // 响应数据，根据接口不同而变化
}
```

### 8.3 核心API接口

#### 客户管理
- GET /api/customers：获取客户列表
- POST /api/customers：创建客户
- GET /api/customers/{id}：获取客户详情
- PUT /api/customers/{id}：更新客户信息
- DELETE /api/customers/{id}：删除客户

#### 订单管理
- GET /api/orders：获取订单列表
- POST /api/orders：创建订单
- GET /api/orders/{id}：获取订单详情
- PUT /api/orders/{id}：更新订单信息
- DELETE /api/orders/{id}：删除订单

#### 库存管理
- GET /api/inventory：获取库存列表
- POST /api/inventory：创建库存记录
- GET /api/inventory/{id}：获取库存详情
- PUT /api/inventory/{id}：更新库存信息
- DELETE /api/inventory/{id}：删除库存记录

#### 运输管理
- GET /api/transport：获取运输列表
- POST /api/transport：创建运输记录
- GET /api/transport/{id}：获取运输详情
- PUT /api/transport/{id}：更新运输信息
- DELETE /api/transport/{id}：删除运输记录

#### 仓库管理
- GET /api/warehouse：获取仓库列表
- POST /api/warehouse：创建仓库
- GET /api/warehouse/{id}：获取仓库详情
- PUT /api/warehouse/{id}：更新仓库信息
- DELETE /api/warehouse/{id}：删除仓库

#### 用户管理
- POST /api/auth/login：用户登录
- POST /api/auth/logout：用户登出
- GET /api/users：获取用户列表
- POST /api/users：创建用户
- PUT /api/users/{id}：更新用户信息

## 9. 开发指南

### 9.1 代码规范

#### 9.1.1 Java代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码，减少样板代码
- 类名使用大驼峰命名法，首字母大写
- 方法名使用小驼峰命名法，首字母小写
- 变量名使用小驼峰命名法，首字母小写
- 常量名使用全大写，下划线分隔
- 包名使用小写字母，多级包名用点分隔

#### 9.1.2 Vue代码规范

- 遵循Vue官方风格指南
- 组件名使用大驼峰命名法
- 方法名使用小驼峰命名法
- 变量名使用小驼峰命名法
- 使用单文件组件（.vue）
- 合理使用Vue的响应式系统

### 9.2 开发流程

1. 从Git仓库拉取最新代码
2. 创建新的分支进行开发：`git checkout -b feature/xxx`
3. 编写代码并提交：`git commit -m "feat: xxx功能开发"`
4. 推送分支到远程仓库：`git push origin feature/xxx`
5. 创建Pull Request进行代码审查
6. 审查通过后，合并代码到主分支

### 9.3 测试规范

- **单元测试**：使用JUnit和Mockito进行后端单元测试，覆盖率要求达到80%以上
- **集成测试**：使用Spring Boot Test进行后端集成测试
- **前端测试**：使用Jest和Vue Test Utils进行前端测试
- **测试执行**：提交代码前必须执行所有测试，确保测试通过

## 10. 性能优化

### 10.1 后端性能优化

1. **数据库优化**
   - 使用索引提高查询效率
   - 优化SQL语句，避免全表扫描
   - 使用数据库连接池管理连接
   - 合理设计表结构，避免冗余字段

2. **缓存优化**
   - 使用Redis缓存热点数据
   - 设置合理的缓存过期时间
   - 使用缓存预热提高系统启动性能
   - 使用缓存穿透、缓存击穿、缓存雪崩的解决方案

3. **代码优化**
   - 使用Lambda表达式和Stream API简化代码
   - 避免不必要的对象创建
   - 使用并发编程提高处理效率
   - 使用延迟加载减少资源消耗

### 10.2 前端性能优化

1. **资源优化**
   - 使用CDN加速静态资源加载
   - 压缩JavaScript和CSS文件
   - 使用图片懒加载
   - 使用字体图标代替图片图标

2. **代码优化**
   - 使用组件懒加载
   - 合理使用Vue的计算属性和监听器
   - 避免不必要的重渲染
   - 使用虚拟列表处理大数据量

3. **网络优化**
   - 减少HTTP请求数量
   - 使用HTTP/2协议
   - 合理使用缓存策略
   - 使用Gzip压缩传输数据

## 11. 常见问题与解决方案

### 11.1 后端服务启动失败

- **数据库连接问题**：检查数据库连接配置是否正确，包括URL、用户名、密码等
- **Redis连接问题**：检查Redis服务是否启动，配置是否正确
- **端口占用问题**：检查端口是否被其他进程占用，可以使用`netstat -ano`命令查看
- **依赖问题**：检查Maven依赖是否正确，执行`mvn clean install`重新安装依赖
- **日志查看**：查看后端日志文件（`backend/logs/logistics-dashboard.log`）获取详细错误信息

### 11.2 前端页面无法访问后端API

- **API地址配置问题**：检查前端配置的API地址是否正确
- **跨域问题**：检查后端是否配置了跨域支持
- **后端服务状态**：检查后端服务是否正常运行
- **网络问题**：检查网络连接是否正常
- **浏览器控制台**：查看浏览器控制台的错误信息

### 11.3 数据库连接失败

- **MySQL服务状态**：检查MySQL服务是否启动
- **数据库配置问题**：检查数据库用户名和密码是否正确
- **数据库是否存在**：检查`logistics_db`数据库是否存在
- **防火墙设置**：检查防火墙是否允许MySQL端口访问
- **连接数限制**：检查MySQL最大连接数设置

### 11.4 Docker容器启动失败

- **Docker服务状态**：检查Docker服务是否启动
- **配置文件问题**：检查Docker Compose配置文件是否正确
- **端口占用问题**：检查容器端口是否被其他进程占用
- **容器日志**：使用`docker logs <容器名>`查看容器日志获取详细错误信息
- **资源限制**：检查系统资源是否足够，如内存、磁盘空间等

## 12. 项目维护与扩展

### 12.1 项目维护

1. **日志管理**：定期查看系统日志，及时发现和解决问题
2. **数据备份**：定期备份数据库，确保数据安全
3. **性能监控**：使用Grafana监控系统性能指标
4. **安全更新**：及时更新依赖库，修复安全漏洞
5. **文档维护**：定期更新项目文档，确保文档与代码同步

### 12.2 项目扩展

1. **功能扩展**：
   - 可以在现有模块基础上添加新功能
   - 可以添加新的业务模块，如报表模块、数据分析模块等

2. **技术扩展**：
   - 可以添加消息队列，如RabbitMQ或Kafka，处理异步任务
   - 可以添加搜索引擎，如Elasticsearch，提高查询效率
   - 可以添加分布式锁，如Redisson，处理并发问题
   - 可以添加分布式事务，如Seata，处理跨服务事务

3. **架构扩展**：
   - 可以将单体应用拆分为微服务架构
   - 可以添加API网关，如Spring Cloud Gateway，统一管理API接口
   - 可以添加服务注册与发现，如Nacos或Eureka，管理微服务
   - 可以添加配置中心，如Nacos或Config Server，统一管理配置

## 13. 联系方式

- 项目负责人：XXX
- 技术支持：XXX
- 邮箱：XXX@example.com
- Git仓库地址：XXX
- 项目文档地址：XXX

## 14. 更新日志

### V1.0.0（2024-XX-XX）

- 初始版本发布
- 实现了客户、订单、库存、运输、仓库等核心业务模块
- 实现了数据可视化功能
- 实现了用户认证和权限管理
- 支持Docker容器化部署
- 提供了完整的API接口

### V1.1.0（2024-XX-XX）

- 优化了系统性能
- 增加了更多的数据可视化图表
- 改进了用户界面
- 修复了已知bug

---

**© 2024 大数据面板项目组. All rights reserved.**