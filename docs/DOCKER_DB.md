# Docker 数据库部署方案

## 概述

本项目提供两种数据库部署方式：

1. **本地数据库**：使用本地安装的 MySQL
2. **Docker 数据库**：使用 Docker 容器运行 MySQL（推荐）

## Docker 数据库优势

- 环境隔离，不影响本地 MySQL
- 快速部署，一键启动
- 数据持久化，容器重启不丢失数据
- 易于迁移和备份
- 版本控制，确保一致性

## 快速开始

### 方式一：使用启动脚本（推荐）

#### Windows PowerShell

```powershell
# 启动 Docker 数据库
.\scripts\start-docker-db.ps1

# 停止 Docker 数据库
.\scripts\stop-docker-db.ps1
```

### 方式二：使用 Docker Compose

```bash
# 启动数据库
docker-compose -f deploy/docker-compose-db.yml up -d

# 查看日志
docker-compose -f deploy/docker-compose-db.yml logs -f

# 停止数据库
docker-compose -f deploy/docker-compose-db.yml down

# 停止并删除数据（谨慎使用）
docker-compose -f deploy/docker-compose-db.yml down -v
```

## 配置说明

### Docker Compose 配置

文件：[docker-compose-db.yml](file:///c:/Users/15071/Documents/大数据面板/docker-compose-db.yml)

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: logistics-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root        # MySQL root 密码
      MYSQL_DATABASE: logistics_db     # 数据库名称
      TZ: Asia/Shanghai                # 时区
    ports:
      - "3306:3306"                    # 端口映射
    volumes:
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql  # 初始化脚本
      - mysql-data:/var/lib/mysql     # 数据持久化
```

### 后端配置

文件：[backend/src/main/resources/application.yml](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/application.yml)

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: root
```

**注意**：使用 Docker 数据库时，后端配置保持不变，因为 Docker 容器的 3306 端口映射到了主机的 3306 端口。

## 连接信息

- **主机**: localhost
- **端口**: 3306
- **数据库**: logistics_db
- **用户名**: root
- **密码**: root

## 常用命令

### 查看容器状态

```bash
docker ps | findstr logistics-mysql
```

### 查看容器日志

```bash
docker logs logistics-mysql
```

### 进入 MySQL 容器

```bash
docker exec -it logistics-mysql bash
```

### 连接到 MySQL

```bash
# 方式一：使用 docker exec
docker exec -it logistics-mysql mysql -uroot -proot logistics_db

# 方式二：使用本地 MySQL 客户端
mysql -h localhost -P 3306 -u root -p logistics_db
```

### 查看数据库表

```bash
docker exec logistics-mysql mysql -uroot -proot logistics_db -e "SHOW TABLES;"
```

### 查看表数据

```bash
# 查看仓库信息
docker exec logistics-mysql mysql -uroot -proot logistics_db -e "SELECT * FROM warehouse;"

# 查看库存占比
docker exec logistics-mysql mysql -uroot -proot logistics_db -e "SELECT * FROM inventory_ratio;"
```

## 数据持久化

数据存储在 Docker 卷中：

```bash
# 查看数据卷
docker volume ls | findstr mysql-data

# 查看数据卷详情
docker volume inspect 大数据面板_mysql-data
```

### 备份数据

```bash
# 备份整个数据库
docker exec logistics-mysql mysqldump -uroot -proot logistics_db > backup.sql

# 备份特定表
docker exec logistics-mysql mysqldump -uroot -proot logistics_db warehouse > warehouse_backup.sql
```

### 恢复数据

```bash
# 恢复整个数据库
docker exec -i logistics-mysql mysql -uroot -proot logistics_db < backup.sql

# 恢复特定表
docker exec -i logistics-mysql mysql -uroot -proot logistics_db < warehouse_backup.sql
```

## 切换数据库部署方式

### 从本地数据库切换到 Docker 数据库

1. 确保本地 MySQL 已停止或端口不冲突
2. 启动 Docker 数据库：
   ```powershell
   .\start-docker-db.ps1
   ```
3. 后端配置无需修改，保持使用 localhost:3306

### 从 Docker 数据库切换到本地数据库

1. 停止 Docker 数据库：
   ```powershell
   .\stop-docker-db.ps1
   ```
2. 启动本地 MySQL 服务
3. 确保本地 MySQL 配置与后端配置一致

## 故障排查

### 容器无法启动

```bash
# 查看详细日志
docker-compose -f docker-compose-db.yml logs mysql

# 检查端口是否被占用
netstat -ano | findstr :3306
```

### 数据库连接失败

```bash
# 检查容器是否运行
docker ps | findstr logistics-mysql

# 测试连接
docker exec logistics-mysql mysqladmin ping -h localhost -uroot -proot
```

### 数据未初始化

```bash
# 检查数据库是否存在
docker exec logistics-mysql mysql -uroot -proot -e "SHOW DATABASES;"

# 检查表是否存在
docker exec logistics-mysql mysql -uroot -proot logistics_db -e "SHOW TABLES;"

# 手动执行初始化脚本
docker exec -i logistics-mysql mysql -uroot -proot < database/init.sql
```

### 重置数据库

```bash
# 停止并删除容器和数据卷
docker-compose -f docker-compose-db.yml down -v

# 重新启动
.\start-docker-db.ps1
```

## 性能优化

### 调整 MySQL 配置

在 [docker-compose-db.yml](file:///c:/Users/15071/Documents/大数据面板/docker-compose-db.yml) 中添加配置：

```yaml
command:
  - --character-set-server=utf8mb4
  - --collation-server=utf8mb4_unicode_ci
  - --max-connections=200
  - --innodb-buffer-pool-size=1G
```

### 资源限制

```yaml
services:
  mysql:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

## 安全建议

1. **修改默认密码**：在生产环境中修改 MySQL root 密码
2. **限制端口访问**：使用防火墙限制 3306 端口访问
3. **定期备份**：定期备份数据库数据
4. **使用 SSL**：在生产环境中启用 SSL 连接

## 完整部署（前端 + 后端 + 数据库）

如果需要同时部署前端、后端和数据库，使用完整的 [docker-compose.yml](file:///c:/Users/15071/Documents/大数据面板/docker-compose.yml)：

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看所有日志
docker-compose logs -f

# 停止所有服务
docker-compose down
```

## 更新日志

- 2025-12-27: 创建 Docker 数据库部署方案
