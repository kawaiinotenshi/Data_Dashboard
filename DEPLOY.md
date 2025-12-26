# 物流仓储大数据展示系统 - Docker 部署文档

## 项目简介

这是一个基于 Vue 3 + Spring Boot + MySQL 的物流仓储大数据展示系统，使用 Docker 容器化部署，无需手动配置环境。

## 技术栈

- **前端**: Vue 3 + Vite + ECharts + Nginx
- **后端**: Spring Boot 2.7.x + MyBatis-Plus
- **数据库**: MySQL 8.0
- **容器化**: Docker + Docker Compose

## 系统要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少 4GB 可用内存
- 至少 10GB 可用磁盘空间

## 快速开始

### 1. 安装 Docker

#### Windows
1. 下载 Docker Desktop for Windows: https://www.docker.com/products/docker-desktop
2. 安装并启动 Docker Desktop
3. 确保 Docker 和 Docker Compose 已正确安装

#### Linux (Ubuntu/Debian)
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

#### macOS
1. 下载 Docker Desktop for Mac: https://www.docker.com/products/docker-desktop
2. 安装并启动 Docker Desktop

### 2. 验证 Docker 安装

```bash
docker --version
docker-compose --version
```

### 3. 部署项目

#### 方式一：一键启动（推荐）

```bash
cd c:\Users\15071\Documents\大数据面板
docker-compose up -d
```

#### 方式二：分步启动

```bash
cd c:\Users\15071\Documents\大数据面板

# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d
```

### 4. 查看服务状态

```bash
docker-compose ps
```

预期输出：
```
NAME                  STATUS              PORTS
logistics-frontend    Up (healthy)        0.0.0.0:80->80/tcp
logistics-backend     Up (healthy)        0.0.0.0:8080:8080/tcp
logistics-mysql       Up (healthy)        0.0.0.0:3306->3306/tcp
```

### 5. 访问系统

- **前端访问**: http://localhost
- **后端 API**: http://localhost:8080/api
- **MySQL 数据库**: localhost:3306
  - 用户名: root
  - 密码: root123
  - 数据库: logistics_db

## 常用命令

### 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f frontend
docker-compose logs -f backend
docker-compose logs -f mysql
```

### 停止服务

```bash
docker-compose stop
```

### 启动服务

```bash
docker-compose start
```

### 重启服务

```bash
docker-compose restart
```

### 停止并删除容器

```bash
docker-compose down
```

### 停止并删除容器和数据卷（谨慎使用）

```bash
docker-compose down -v
```

### 重新构建镜像

```bash
docker-compose build --no-cache
docker-compose up -d
```

### 进入容器

```bash
# 进入前端容器
docker exec -it logistics-frontend sh

# 进入后端容器
docker exec -it logistics-backend sh

# 进入 MySQL 容器
docker exec -it logistics-mysql bash
```

## 目录结构

```
大数据面板/
├── docker-compose.yml          # Docker Compose 配置文件
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

## 配置说明

### 端口配置

可以在 `docker-compose.yml` 中修改端口映射：

```yaml
services:
  mysql:
    ports:
      - "3306:3306"  # 修改为其他端口，如 "13306:3306"

  backend:
    ports:
      - "8080:8080"  # 修改为其他端口，如 "18080:8080"

  frontend:
    ports:
      - "80:80"      # 修改为其他端口，如 "8080:80"
```

### 数据库配置

数据库配置在 `docker-compose.yml` 中：

```yaml
environment:
  MYSQL_ROOT_PASSWORD: root123        # MySQL root 密码
  MYSQL_DATABASE: logistics_db         # 数据库名称
  MYSQL_USER: logistics               # 普通用户
  MYSQL_PASSWORD: logistics123         # 普通用户密码
```

后端数据库连接配置在 `BackEnd/src/main/resources/application.yml` 中：

```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/logistics_db
    username: root
    password: root123
```

## 数据持久化

MySQL 数据存储在 Docker 卷中，容器重启或删除后数据不会丢失。

查看数据卷：

```bash
docker volume ls
```

备份数据：

```bash
docker exec logistics-mysql mysqldump -uroot -proot123 logistics_db > backup.sql
```

恢复数据：

```bash
docker exec -i logistics-mysql mysql -uroot -proot123 logistics_db < backup.sql
```

## 健康检查

系统包含健康检查机制：

- **MySQL**: 每 10 秒检查一次，最多重试 5 次
- **Backend**: 每 30 秒检查一次，最多重试 3 次
- **Frontend**: 依赖 Backend 启动

查看健康状态：

```bash
docker-compose ps
```

## 故障排查

### 1. 容器无法启动

查看日志：

```bash
docker-compose logs [service_name]
```

常见原因：
- 端口被占用：修改 `docker-compose.yml` 中的端口映射
- 内存不足：关闭其他应用或增加 Docker 内存限制
- 权限问题：确保 Docker 有足够的权限

### 2. 数据库连接失败

检查 MySQL 容器状态：

```bash
docker-compose ps mysql
docker-compose logs mysql
```

确保后端配置中的数据库地址为 `mysql`（不是 `localhost`）。

### 3. 前端无法访问后端

检查网络连接：

```bash
docker network ls
docker network inspect 大数据面板_logistics-network
```

确保所有容器在同一个网络中。

### 4. 数据未初始化

检查数据库初始化脚本：

```bash
docker exec logistics-mysql mysql -uroot -proot123 -e "SHOW DATABASES;"
docker exec logistics-mysql mysql -uroot -proot123 logistics_db -e "SHOW TABLES;"
```

如果表不存在，手动执行初始化脚本：

```bash
docker exec -i logistics-mysql mysql -uroot -proot123 < database/init.sql
```

## 性能优化

### 1. 调整 Docker 资源限制

在 `docker-compose.yml` 中添加资源限制：

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

### 2. 启用 MySQL 查询缓存

在 `docker-compose.yml` 中添加 MySQL 配置：

```yaml
command:
  - --character-set-server=utf8mb4
  - --collation-server=utf8mb4_unicode_ci
  - --query-cache-size=64M
  - --query-cache-type=1
```

### 3. 使用 Nginx 缓存

在 `nginx.conf` 中添加缓存配置：

```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m;

location /api {
    proxy_cache my_cache;
    proxy_cache_valid 200 60m;
    proxy_pass http://backend:8080;
}
```

## 安全建议

1. **修改默认密码**: 修改 MySQL root 密码和后端数据库密码
2. **限制端口访问**: 使用防火墙限制端口访问
3. **使用 HTTPS**: 在生产环境中配置 SSL 证书
4. **定期备份**: 定期备份数据库数据
5. **更新镜像**: 定期更新 Docker 镜像以获取安全补丁

## 生产环境部署

### 1. 使用环境变量

创建 `.env` 文件：

```env
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_DATABASE=production_db
MYSQL_USER=app_user
MYSQL_PASSWORD=app_password
```

修改 `docker-compose.yml`：

```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  MYSQL_DATABASE: ${MYSQL_DATABASE}
  MYSQL_USER: ${MYSQL_USER}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
```

### 2. 使用外部数据库

修改 `docker-compose.yml`，移除 MySQL 服务，修改后端配置：

```yaml
backend:
  environment:
    SPRING_DATASOURCE_URL: jdbc:mysql://external-mysql-host:3306/logistics_db
```

### 3. 使用 Nginx 反向代理

添加 Nginx 服务：

```yaml
nginx:
  image: nginx:alpine
  ports:
    - "443:443"
  volumes:
    - ./nginx/nginx.conf:/etc/nginx/nginx.conf
    - ./nginx/ssl:/etc/nginx/ssl
  depends_on:
    - frontend
```

## 监控和日志

### 查看资源使用情况

```bash
docker stats
```

### 导出日志

```bash
docker-compose logs > logs.txt
```

### 使用 ELK Stack 集成日志

可以集成 Elasticsearch、Logstash 和 Kibana 进行日志管理。

## 更新和升级

### 更新代码

```bash
git pull
docker-compose build --no-cache
docker-compose up -d
```

### 更新 Docker 镜像

```bash
docker-compose pull
docker-compose up -d
```

## 常见问题

### Q: 如何修改前端访问端口？
A: 修改 `docker-compose.yml` 中 frontend 服务的 ports 配置。

### Q: 如何查看数据库数据？
A: 使用 MySQL 客户端连接或进入容器：
```bash
docker exec -it logistics-mysql mysql -uroot -proot123 logistics_db
```

### Q: 如何备份数据？
A: 使用 mysqldump 命令备份数据库。

### Q: 如何扩展服务？
A: 使用 Docker Swarm 或 Kubernetes 进行服务扩展。

### Q: 如何查看容器 IP？
A: 使用 `docker inspect [container_name]` 查看。

## 技术支持

如有问题，请查看：
- Docker 官方文档: https://docs.docker.com
- Docker Compose 文档: https://docs.docker.com/compose
- 项目日志: `FrontEnd/project.log`

## 许可证

本项目仅供学习和参考使用。

## 更新日志

- 2025-12-26: 初始版本，完成 Docker 容器化部署