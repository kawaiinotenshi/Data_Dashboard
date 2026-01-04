# Docker部署指南

## 前置要求

- Docker Desktop 20.10+ 或 Docker Engine 20.10+
- Docker Compose 1.29+
- 至少 4GB 可用内存
- 至少 10GB 可用磁盘空间

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd 大数据面板
```

### 2. 启动所有服务

```bash
cd deploy
docker-compose up -d
```

### 3. 访问应用

- 前端应用: http://localhost
- 后端API: http://localhost:8080/api
- MySQL数据库: localhost:3306 (用户名: root, 密码: root)

### 4. 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f frontend
docker-compose logs -f backend
docker-compose logs -f mysql
```

### 5. 停止服务

```bash
docker-compose down
```

## 详细说明

### 服务架构

项目包含三个主要服务：

1. **MySQL数据库** (端口 3306)
   - 镜像: mysql:8.0
   - 数据库名: logistics_db
   - 初始化脚本: logistics_db.sql
   - 数据持久化: Docker volume

2. **后端服务** (端口 8080)
   - 基础镜像: openjdk:11-jre-slim
   - 构建镜像: maven:3.8-openjdk-11
   - 健康检查: /api/warehouses
   - 依赖: MySQL服务

3. **前端服务** (端口 80)
   - 基础镜像: nginx:alpine
   - 构建镜像: node:18-alpine
   - 健康检查: HTTP GET /
   - 依赖: 后端服务

### 环境变量配置

#### MySQL服务

- MYSQL_ROOT_PASSWORD: root
- MYSQL_DATABASE: logistics_db
- MYSQL_USER: logistics
- MYSQL_PASSWORD: root
- TZ: Asia/Shanghai

#### 后端服务

- SPRING_PROFILES_ACTIVE: prod
- SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/logistics_db
- SPRING_DATASOURCE_USERNAME: root
- SPRING_DATASOURCE_PASSWORD: root
- DB_HOST: mysql
- DB_PORT: 3306
- DB_NAME: logistics_db
- DB_USERNAME: root
- DB_PASSWORD: root

#### 前端服务

- NODE_ENV: production
- VITE_API_BASE_URL: /api

### 数据持久化

MySQL数据存储在Docker volume中，即使容器删除也不会丢失数据：

```bash
# 查看volume
docker volume ls

# 删除volume (会删除所有数据)
docker-compose down -v
```

### 健康检查

所有服务都配置了健康检查：

- MySQL: 每10秒检查一次，超时5秒，重试5次
- 后端: 每30秒检查一次，超时10秒，重试3次，启动等待60秒
- 前端: 每30秒检查一次，超时3秒，重试3次，启动等待5秒

### 网络配置

所有服务运行在同一个Docker网络中，可以通过服务名称相互访问：

- MySQL服务名: mysql
- 后端服务名: backend
- 前端服务名: frontend

## 常用命令

### 构建镜像

```bash
# 构建所有服务镜像
docker-compose build

# 构建特定服务镜像
docker-compose build frontend
docker-compose build backend
```

### 启动服务

```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 启动所有服务（前台运行，查看日志）
docker-compose up

# 启动特定服务
docker-compose up -d mysql
```

### 停止服务

```bash
# 停止所有服务
docker-compose stop

# 停止特定服务
docker-compose stop backend
```

### 重启服务

```bash
# 重启所有服务
docker-compose restart

# 重启特定服务
docker-compose restart backend
```

### 查看状态

```bash
# 查看所有服务状态
docker-compose ps

# 查看服务详细信息
docker-compose ps backend
```

### 查看日志

```bash
# 查看所有服务日志（实时）
docker-compose logs -f

# 查看最近100行日志
docker-compose logs --tail=100

# 查看特定服务日志
docker-compose logs -f backend

# 查看特定时间的日志
docker-compose logs --since="2024-01-01T00:00:00" backend
```

### 进入容器

```bash
# 进入后端容器
docker-compose exec backend bash

# 进入MySQL容器
docker-compose exec mysql bash

# 进入MySQL数据库
docker-compose exec mysql mysql -uroot -proot123 logistics_db
```

### 清理资源

```bash
# 停止并删除容器
docker-compose down

# 停止并删除容器、网络、volume
docker-compose down -v

# 删除所有未使用的镜像
docker image prune -a

# 删除所有未使用的volume
docker volume prune
```

## 故障排查

### 服务无法启动

1. 检查端口是否被占用：
```bash
netstat -ano | findstr :80
netstat -ano | findstr :8080
netstat -ano | findstr :3306
```

2. 查看服务日志：
```bash
docker-compose logs backend
```

3. 检查Docker资源：
```bash
docker system df
```

### 数据库连接失败

1. 检查MySQL服务状态：
```bash
docker-compose ps mysql
docker-compose logs mysql
```

2. 测试数据库连接：
```bash
docker-compose exec mysql mysql -uroot -proot123 -e "SHOW DATABASES;"
```

3. 检查数据库初始化：
```bash
docker-compose exec mysql mysql -uroot -proot123 logistics_db -e "SHOW TABLES;"
```

### 前端无法访问后端

1. 检查后端服务状态：
```bash
docker-compose ps backend
docker-compose logs backend
```

2. 检查网络连接：
```bash
docker-compose exec frontend ping backend
```

3. 测试API端点：
```bash
curl http://localhost:8080/api/warehouses
```

### 内存不足

1. 增加Docker内存限制：
   - 打开Docker Desktop
   - Settings -> Resources -> Memory
   - 增加到至少4GB

2. 清理未使用的资源：
```bash
docker system prune -a
```

## 性能优化

### 调整MySQL配置

编辑docker-compose.yml，添加MySQL配置：

```yaml
mysql:
  command:
    - --character-set-server=utf8mb4
    - --collation-server=utf8mb4_unicode_ci
    - --max-connections=200
    - --innodb-buffer-pool-size=1G
```

### 调整后端JVM参数

编辑backend/Dockerfile，修改JVM参数：

```dockerfile
ENTRYPOINT ["java", "-Xmx1g", "-Xms512m", "-jar", "app.jar"]
```

### 使用多阶段构建

项目已经使用多阶段构建来减小镜像大小：
- 前端: node:18-alpine -> nginx:alpine
- 后端: maven:3.8-openjdk-11 -> openjdk:11-jre-slim

## 安全建议

### 1. 修改默认密码

修改docker-compose.yml中的密码：

```yaml
environment:
  MYSQL_ROOT_PASSWORD: your_secure_password
  MYSQL_PASSWORD: your_secure_password
  SPRING_DATASOURCE_PASSWORD: your_secure_password
```

### 2. 使用环境变量文件

创建.env文件：

```env
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_PASSWORD=your_secure_password
DB_PASSWORD=your_secure_password
```

修改docker-compose.yml：

```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
  DB_PASSWORD: ${DB_PASSWORD}
```

### 3. 限制容器权限

项目已经使用非root用户运行后端服务：

```dockerfile
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser
```

### 4. 使用私有镜像仓库

将镜像推送到私有仓库：

```bash
# 登录私有仓库
docker login your-registry.com

# 标记镜像
docker tag logistics-frontend your-registry.com/logistics-frontend:latest
docker tag logistics-backend your-registry.com/logistics-backend:latest

# 推送镜像
docker push your-registry.com/logistics-frontend:latest
docker push your-registry.com/logistics-backend:latest
```

## 生产环境部署

### 1. 使用反向代理

配置Nginx反向代理：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api {
        proxy_pass http://localhost:8080/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 2. 使用HTTPS

使用Let's Encrypt获取免费SSL证书：

```bash
# 安装certbot
sudo apt-get install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com
```

### 3. 配置自动重启

docker-compose.yml已经配置了restart: always，确保服务自动重启。

### 4. 监控和日志

使用Docker日志驱动：

```yaml
services:
  backend:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

### 5. 备份数据

定期备份MySQL数据：

```bash
# 备份数据库
docker-compose exec mysql mysqldump -uroot -proot123 logistics_db > backup.sql

# 恢复数据库
docker-compose exec -T mysql mysql -uroot -proot123 logistics_db < backup.sql
```

## 更新和升级

### 更新代码

```bash
# 拉取最新代码
git pull

# 重新构建并启动
docker-compose up -d --build
```

### 更新镜像

```bash
# 拉取最新镜像
docker-compose pull

# 重新创建容器
docker-compose up -d
```

## 技术支持

如遇到问题，请：

1. 查看日志: `docker-compose logs -f`
2. 检查服务状态: `docker-compose ps`
3. 查看项目文档: [README.md](../README.md)
4. 提交Issue: [项目Issue页面]

## 附录

### 端口映射

| 服务 | 容器端口 | 主机端口 | 协议 |
|------|---------|---------|------|
| 前端 | 80 | 80 | HTTP |
| 后端 | 8080 | 8080 | HTTP |
| MySQL | 3306 | 3306 | TCP |

### 目录结构

```
大数据面板/
├── deploy/
│   ├── docker-compose.yml    # Docker Compose配置文件
│   └── docker-compose-db.yml # 仅数据库的Docker Compose配置
├── FrontEnd/
│   ├── Dockerfile            # 前端Dockerfile
│   ├── nginx.conf            # Nginx配置
│   └── .dockerignore         # Docker忽略文件
├── backend/
│   ├── Dockerfile            # 后端Dockerfile
│   └── .dockerignore         # Docker忽略文件
└── logistics_db.sql          # 数据库初始化脚本
```

### 相关文档

- [项目README](../README.md)
- [部署文档](../docs/DEPLOY.md)
- [Docker官方文档](https://docs.docker.com/)
- [Docker Compose文档](https://docs.docker.com/compose/)
