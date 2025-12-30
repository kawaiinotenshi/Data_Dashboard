# 物流管理系统 - Python桌面应用

一个基于Python tkinter的物流管理系统桌面应用程序，提供登录功能和数据库数据管理功能。

## 功能特性

- 用户登录验证
- 数据库数据可视化展示
- 数据增删改查操作
- 数据搜索功能
- 支持所有数据库表的管理

## 系统要求

- Python 3.7+
- MySQL数据库
- tkinter（Python内置）

## 安装步骤

1. 安装Python依赖：
```bash
pip install -r requirements.txt
```

2. 配置数据库连接：
编辑 `config.py` 文件，修改数据库连接信息：
```python
DB_CONFIG = {
    'host': 'localhost',
    'port': 3307,
    'user': 'root',
    'password': 'root',
    'database': 'logistics_db',
    'charset': 'utf8mb4'
}
```

3. 修改登录凭据（可选）：
编辑 `config.py` 文件，修改管理员账号：
```python
ADMIN_CREDENTIALS = {
    'username': 'admin',
    'password': 'admin123'
}
```

## 使用方法

### 启动应用

```bash
python main.py
```

### 登录

- 默认用户名：`admin`
- 默认密码：`admin123`

### 数据管理

1. 在左侧列表中选择要管理的数据表
2. 使用搜索框搜索数据（按名称或位置）
3. 点击"新增"按钮添加新数据
4. 双击或点击"修改"按钮编辑数据
5. 点击"删除"按钮删除选中数据
6. 点击"刷新"按钮重新加载数据

## 文件结构

```
PythonApp/
├── main.py              # 主程序入口
├── config.py            # 配置文件
├── database.py          # 数据库操作类
├── login_window.py      # 登录窗口
├── data_management.py   # 数据管理窗口
├── requirements.txt     # Python依赖
└── README.md           # 说明文档
```

## 注意事项

1. 确保MySQL数据库服务已启动
2. 确保数据库连接配置正确
3. 删除数据操作不可恢复，请谨慎操作
4. 建议定期备份数据库

## 技术栈

- Python 3.x
- tkinter（GUI框架）
- mysql-connector-python（数据库驱动）
