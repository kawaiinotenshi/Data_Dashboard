# 大数据面板项目使用手册

## 1. 项目概述

本项目是一个基于Spring Boot和Vue.js的大数据可视化面板系统，旨在为物流行业提供全面的数据管理和分析功能。系统整合了订单管理、用户管理、产品管理、数据统计等核心功能，通过直观的可视化界面展示业务数据，帮助企业做出更明智的决策。

### 1.1 项目特点
- 前后端分离架构，便于维护和扩展
- 完整的数据管理功能，支持增删改查操作
- 丰富的数据可视化图表，直观展示业务数据
- 完善的权限管理机制，确保数据安全
- 支持Docker容器化部署，简化环境配置
- 提供RESTful API接口，方便第三方系统集成

## 2. 技术栈

### 2.1 后端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.0 | 后端框架 |
| MyBatis-Plus | 3.5.0 | ORM框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.0 | 缓存中间件 |
| Maven | 3.6.3 | 项目构建工具 |
| JWT | - | 身份认证 |
| Lombok | - | 简化Java代码 |

### 2.2 前端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue.js | 3.0 | 前端框架 |
| Vite | 2.0 | 前端构建工具 |
| Element Plus | - | UI组件库 |
| Axios | - | HTTP客户端 |
| ECharts | - | 数据可视化图表库 |
| Vue Router | - | 路由管理 |
| Pinia | - | 状态管理 |

### 2.3 开发工具
- IDE：IntelliJ IDEA / VS Code
- 版本控制：Git
- 容器化：Docker / Docker Compose

## 3. 项目结构

### 3.1 项目目录结构
```
大数据面板/
├── backend/                # 后端项目目录
│   ├── src/               # 源代码目录
│   │   ├── main/          # 主代码目录
│   │   │   ├── java/      # Java源代码
│   │   │   │   └── com/logistics/  # 包名
│   │   │   │       ├── config/      # 配置类
│   │   │   │       ├── controller/  # 控制器
│   │   │   │       ├── entity/      # 实体类
│   │   │   │       │   └── base/    # 基础实体类
│   │   │   │       ├── mapper/      # Mapper接口
│   │   │   │       ├── service/     # 业务逻辑层
│   │   │   │       ├── utils/       # 工具类
│   │   │   │       └── Application.java  # 启动类
│   │   │   └── resources/  # 资源目录
│   │   │       ├── application.yml  # 配置文件
│   │   │       └── mapper/          # MyBatis映射文件
│   │   └── test/           # 测试代码
│   ├── Dockerfile          # Docker构建文件
│   └── pom.xml             # Maven配置文件
├── FrontEnd/               # 前端项目目录
│   ├── public/             # 静态资源
│   ├── src/                # 源代码目录
│   │   ├── assets/         # 资源文件
│   │   ├── components/     # 组件
│   │   ├── views/          # 页面视图
│   │   ├── router/         # 路由配置
│   │   ├── store/          # 状态管理
│   │   ├── utils/          # 工具类
│   │   ├── App.vue         # 根组件
│   │   └── main.js         # 入口文件
│   ├── Dockerfile          # Docker构建文件
│   ├── package.json        # 项目配置
│   └── vite.config.js      # Vite配置
├── deploy/                 # 部署配置
│   ├── docker-compose.yml      # Docker Compose配置
│   └── docker-compose-db.yml   # 仅数据库的Docker配置
├── logistics_db.sql        # 数据库初始化脚本
├── start_project.bat       # 项目启动脚本
└── README.md               # 项目说明文档
```

## 4. 环境要求

### 4.1 开发环境要求
| 环境 | 版本 | 说明 |
|------|------|------|
| JDK | 8或以上 | 后端开发和运行环境 |
| Node.js | 16或以上 | 前端开发和运行环境 |
| Maven | 3.6或以上 | 后端项目构建工具 |
| MySQL | 8.0或以上 | 关系型数据库 |
| Redis | 7.0或以上 | 缓存中间件 |
| Git | 2.0或以上 | 版本控制工具 |

### 4.2 运行环境要求
- 服务器：至少2核4G内存
- 操作系统：Windows/Linux/macOS
- 网络：支持HTTP/HTTPS协议

## 5. 安装配置

### 5.1 环境安装

#### 5.1.1 安装JDK
1. 下载JDK 8或以上版本：[Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html)或[OpenJDK](https://adoptopenjdk.net/)
2. 安装并配置环境变量：
   - JAVA_HOME：JDK安装路径
   - PATH：添加%JAVA_HOME%\bin

#### 5.1.2 安装Node.js
1. 下载Node.js 16或以上版本：[Node.js官网](https://nodejs.org/)
2. 安装并验证：
   ```bash
   node -v
   npm -v
   ```

#### 5.1.3 安装Maven
1. 下载Maven 3.6或以上版本：[Maven官网](https://maven.apache.org/)
2. 安装并配置环境变量：
   - MAVEN_HOME：Maven安装路径
   - PATH：添加%MAVEN_HOME%\bin

#### 5.1.4 安装MySQL
1. 下载MySQL 8.0或以上版本：[MySQL官网](https://www.mysql.com/)
2. 安装并配置：
   - 创建数据库：`logistics_db`
   - 设置字符集：utf8mb4
   - 创建用户并授权

#### 5.1.5 安装Redis
1. 下载Redis 7.0或以上版本：[Redis官网](https://redis.io/)
2. 安装并启动Redis服务

### 5.2 项目配置

#### 5.2.1 后端配置
1. 进入`backend/src/main/resources`目录
2. 修改`application.yml`文件：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
       username: root
       password: root
     redis:
       host: localhost
       port: 6379
       password: logistics_redis_password_2024
   server:
     port: 8081
   ```

#### 5.2.2 前端配置
1. 进入`FrontEnd`目录
2. 修改`.env.development`文件（开发环境）：
   ```env
   VITE_API_BASE_URL = 'http://localhost:8081/api'
   VITE_API_TIMEOUT = 10000
   ```

## 6. 运行项目

### 6.1 使用启动脚本（推荐）
1. 确保所有环境已安装配置完成
2. 双击运行`start_project.bat`脚本
3. 脚本会自动检查环境并启动前后端服务
4. 等待启动完成后，根据提示访问相应地址

### 6.2 手动运行

#### 6.2.1 启动后端服务
1. 进入`backend`目录
2. 执行Maven命令：
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
3. 等待服务启动完成

#### 6.2.2 启动前端服务
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

### 6.3 访问地址
- 前端面板：http://localhost:3000
- 后端API：http://localhost:8081
- Swagger文档：http://localhost:8081/swagger-ui.html（如果已配置）

## 7. 项目架构

### 7.1 系统架构图
```
┌─────────────────┐
│   前端应用      │
│   Vue.js +      │
│   Element Plus  │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   后端API接口   │
│   Spring Boot   │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   业务逻辑层    │
│   Service       │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   数据访问层    │
│   MyBatis-Plus  │
└─────────────────┘
         │
┌────────┴────────┐
▼                 ▼
┌─────────────────┐  ┌─────────────────┐
│   MySQL数据库   │  │   Redis缓存     │
└─────────────────┘  └─────────────────┘
```

### 7.2 核心模块

#### 7.2.1 用户管理模块
- 用户注册、登录、登出
- 用户信息管理
- 角色权限管理

#### 7.2.2 订单管理模块
- 订单创建、查询、更新、删除
- 订单状态管理
- 订单统计分析

#### 7.2.3 产品管理模块
- 产品信息管理
- 库存管理
- 产品分类管理

#### 7.2.4 数据统计模块
- 销售数据统计
- 用户行为分析
- 业务指标监控

#### 7.2.5 系统管理模块
- 日志管理
- 系统配置
- 数据备份与恢复

## 8. 核心功能

### 8.1 数据可视化
- 仪表盘：展示关键业务指标
- 趋势图：展示数据变化趋势
- 柱状图：对比不同类别的数据
- 饼图：展示数据占比
- 地图：展示地理位置数据

### 8.2 数据管理
- 批量导入/导出数据
- 数据筛选与排序
- 数据分页展示
- 数据验证与清洗

### 8.3 权限控制
- 基于角色的访问控制（RBAC）
- 细粒度权限管理
- 操作日志记录

### 8.4 系统集成
- RESTful API接口
- WebSocket实时通信
- 第三方系统集成支持

## 9. API文档

### 9.1 Swagger文档
如果项目已配置Swagger，可以通过以下地址访问API文档：
- http://localhost:8081/swagger-ui.html

### 9.2 API规范

#### 9.2.1 接口命名规范
- 使用RESTful风格
- 动词 + 名词组合
- 示例：
  - GET /api/users：获取用户列表
  - POST /api/users：创建用户
  - GET /api/users/{id}：获取用户详情
  - PUT /api/users/{id}：更新用户信息
  - DELETE /api/users/{id}：删除用户

#### 9.2.2 响应格式
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

## 10. 数据库设计

### 10.1 数据库概述
本项目使用MySQL数据库，数据库名为`logistics_db`，采用InnoDB存储引擎，字符集为utf8mb4。

### 10.2 实体关系图

### 10.3 核心实体类

#### 10.3.1 BaseEntity（基础实体类）
所有实体类的父类，包含通用字段：
- id：主键
- created_at：创建时间
- updated_at：更新时间
- deleted：删除标志

```java
@Data
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;
}
```

#### 10.3.2 User（用户实体类）
- username：用户名
- password：密码
- email：邮箱
- phone：手机号
- role_id：角色ID

#### 10.3.3 Order（订单实体类）
- order_no：订单号
- user_id：用户ID
- product_id：产品ID
- quantity：数量
- total_price：总价
- status：状态

#### 10.3.4 Product（产品实体类）
- name：产品名称
- description：产品描述
- price：价格
- stock：库存
- category_id：分类ID

## 11. Docker部署

### 11.1 环境要求
- 安装Docker：19.03或以上版本
- 安装Docker Compose：1.27或以上版本

### 11.2 部署步骤

#### 11.2.1 使用Docker Compose部署
1. 进入项目根目录
2. 执行以下命令：
   ```bash
   docker-compose -f deploy/docker-compose.yml up -d
   ```
3. 等待所有容器启动完成（首次启动可能需要更长时间，因为需要下载镜像和初始化数据库）
4. 访问地址：
   - 前端面板：http://localhost:80
   - 后端API：http://localhost:8080
   - Swagger文档：http://localhost:8080/swagger-ui.html
   - Grafana监控：http://localhost:3000
   - Prometheus监控：http://localhost:9090

#### 11.2.2 单独部署数据库
如果只需要部署数据库服务：
```bash
docker-compose -f deploy/docker-compose-db.yml up -d
```

### 11.3 Docker容器说明

| 容器名称 | 服务 | 端口 | 说明 |
|---------|------|------|------|
| logistics-mysql | MySQL数据库 | 3307:3306 | 使用`logistics_db.sql`初始化数据库 |
| logistics-redis | Redis缓存 | 6379:6379 | 缓存服务，密码：`logistics_redis_password_2024` |
| logistics-backend | 后端服务 | 8080:8080 | Spring Boot后端应用 |
| logistics-frontend | 前端服务 | 80:80 | Vue.js前端应用 |
| logistics-prometheus | Prometheus监控 | 9090:9090 | 监控数据收集 |
| logistics-grafana | Grafana可视化 | 3000:3000 | 监控数据可视化，默认账号：admin/admin |

### 11.4 数据库初始化
- Docker部署会自动使用项目根目录下的`logistics_db.sql`文件初始化数据库
- 该文件包含完整的数据库结构和初始数据
- 确保在部署前`logistics_db.sql`文件已存在于项目根目录

### 11.5 关键配置说明

#### 11.5.1 环境变量
- MySQL根密码：`root`
- MySQL用户：`logistics`，密码：`root`
- Redis密码：`logistics_redis_password_2024`
- Grafana管理员账号：`admin`，密码：`admin`

#### 11.5.2 持久化数据
所有服务的数据都通过Docker卷进行持久化，确保容器重启后数据不会丢失：
- MySQL数据：`mysql-data`卷
- Redis数据：`redis-data`卷
- Prometheus数据：`prometheus-data`卷
- Grafana数据：`grafana-data`卷
- 后端日志：`backend-logs`卷

#### 11.5.3 网络配置
- 所有容器都连接到`logistics-network`网络（172.20.0.0/16子网）
- 容器之间可以通过服务名称相互通信

### 11.6 健康检查
所有服务都配置了健康检查，确保服务正常运行：
- MySQL：通过`mysqladmin ping`检查
- Redis：通过`redis-cli ping`检查
- 后端：通过访问`/api/warehouse/list`端点检查
- 前端：通过访问根路径检查
- Prometheus：通过访问健康端点检查
- Grafana：通过访问健康API检查

### 11.7 资源限制
所有容器都配置了CPU和内存限制，确保系统资源合理分配

### 11.8 日志管理
所有容器的日志都配置了最大大小和保留数量，避免日志占用过多磁盘空间

## 12. 开发指南

### 12.1 代码规范

#### 12.1.1 Java代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 类名使用驼峰命名法，首字母大写
- 方法名使用驼峰命名法，首字母小写
- 变量名使用驼峰命名法，首字母小写
- 常量名使用全大写，下划线分隔

#### 12.1.2 Vue代码规范
- 遵循Vue官方风格指南
- 组件名使用大驼峰命名法
- 方法名使用驼峰命名法
- 变量名使用驼峰命名法
- 使用TypeScript（如果项目支持）

### 12.2 开发流程
1. 从Git仓库拉取最新代码
2. 创建新的分支进行开发
3. 编写代码并提交
4. 推送分支到远程仓库
5. 创建Pull Request进行代码审查
6. 合并代码到主分支

### 12.3 测试
- 单元测试：使用JUnit和Mockito
- 集成测试：使用Spring Boot Test
- 前端测试：使用Jest和Vue Test Utils

## 13. 常见问题

### 13.1 后端服务启动失败
- 检查数据库连接是否正确
- 检查Redis服务是否启动
- 检查端口是否被占用
- 查看日志文件获取详细错误信息

### 13.2 前端页面无法访问后端API
- 检查前端配置的API地址是否正确
- 检查后端服务是否启动
- 检查是否存在跨域问题
- 查看浏览器控制台的错误信息

### 13.3 数据库连接失败
- 检查MySQL服务是否启动
- 检查数据库用户名和密码是否正确
- 检查数据库是否存在
- 检查防火墙设置

### 13.4 Docker容器启动失败
- 检查Docker服务是否启动
- 检查Docker Compose配置文件是否正确
- 查看容器日志获取详细错误信息
- 检查端口是否被占用

## 14. 联系方式

- 项目负责人：XXX
- 技术支持：XXX
- 邮箱：XXX@example.com
- Git仓库地址：XXX

## 15. 更新日志

### V1.0.0（2024-XX-XX）
- 初始版本发布
- 实现用户管理、订单管理、产品管理功能
- 完成数据可视化面板
- 支持Docker部署

---

**© 2024 大数据面板项目组. All rights reserved.**