# MCP MySQL 配置修复指南

## 问题描述

MCP MySQL 连接失败，错误信息：
```
spawn uvx ENOENT
```

## 问题原因

Trae IDE 的 MCP MySQL 配置中使用了 `uvx` 命令，但在 Windows 上该命令不可用。

## 解决方案

### 方法 1：在 Trae IDE 中修改 MCP MySQL 配置

1. 打开 Trae IDE
2. 进入设置（Settings）
3. 找到 MCP（Model Context Protocol）配置
4. 找到 MySQL MCP 服务器配置
5. 修改启动命令：

**错误配置：**
```json
{
  "command": "uvx",
  "args": ["--from", "mysql-mcp-server", "mysql_mcp_server"]
}
```

**正确配置：**
```json
{
  "command": "python",
  "args": ["-m", "uv", "tool", "run", "--from", "mysql-mcp-server", "mysql_mcp_server"],
  "env": {
    "MYSQL_HOST": "localhost",
    "MYSQL_PORT": "3307",
    "MYSQL_USER": "root",
    "MYSQL_PASSWORD": "root",
    "MYSQL_DATABASE": "logistics_db"
  }
}
```

### 方法 2：使用命令行手动启动 MCP MySQL 服务器

如果您暂时无法修改 Trae IDE 的配置，可以使用以下命令手动启动：

```powershell
$env:MYSQL_HOST="localhost"
$env:MYSQL_PORT="3307"
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="root"
$env:MYSQL_DATABASE="logistics_db"
python -m uv tool run --from mysql-mcp-server mysql_mcp_server
```

### 方法 3：创建启动脚本

创建一个批处理文件来启动 MCP MySQL 服务器：

**mcp_mysql_start.bat**
```batch
@echo off
set MYSQL_HOST=localhost
set MYSQL_PORT=3307
set MYSQL_USER=root
set MYSQL_PASSWORD=root
set MYSQL_DATABASE=logistics_db
python -m uv tool run --from mysql-mcp-server mysql_mcp_server
```

## 验证安装

确保已安装必要的依赖：

```powershell
python -m pip install uv
python -m uv tool install mysql-mcp-server
```

## 数据库连接信息

- Host: localhost
- Port: 3307
- User: root
- Password: root
- Database: logistics_db

## 注意事项

1. 确保 MySQL 服务正在运行（端口 3307）
2. 确保 uv 包管理器已正确安装
3. 确保 Python 版本兼容（建议 Python 3.8+）
4. 如果使用 Docker MySQL，请确保容器正在运行

## 相关文件

- 数据库初始化脚本: `database/init.sql`
- 数据库修复脚本: `database/fix_schema.sql`
- 启动器配置: `launcher_config.json`

## 故障排除

如果仍然无法连接，请检查：

1. MySQL 服务是否正在运行
2. 端口 3307 是否被占用
3. 数据库用户名和密码是否正确
4. 数据库 logistics_db 是否存在
5. uv 包管理器是否正确安装