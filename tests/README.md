# Playwright 自动化测试

## 环境要求

- Python 3.14+
- Node.js (用于运行前端开发服务器)
- MySQL (用于后端数据库)

## 安装步骤

### 1. 安装 Python 依赖

```bash
python -m pip install -r requirements-test.txt
```

### 2. 安装 Playwright 浏览器

```bash
python -m playwright install chromium
```

## 启动服务

### 启动前端开发服务器

```bash
cd FrontEnd
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

### 启动后端服务

```bash
cd BackEnd
java -jar target/logistics-dashboard-1.0.0.jar
```

后端服务将在 `http://localhost:8080/api` 启动

## 运行测试

### 运行所有测试

```bash
python -m pytest tests/ -v --headed
```

### 运行特定测试文件

```bash
python -m pytest tests/test_dashboard.py -v --headed
python -m pytest tests/test_api.py -v --headed
python -m pytest tests/test_screenshot.py -v --headed
python -m pytest tests/test_e2e.py -v --headed
```

### 无头模式运行（不显示浏览器窗口）

```bash
python -m pytest tests/ -v
```

### 生成测试报告

```bash
python -m pytest tests/ -v --html=report.html
```

## 测试文件说明

- `test_dashboard.py` - 仪表板页面加载和显示测试
- `test_api.py` - API 数据显示测试
- `test_screenshot.py` - 截图测试
- `test_e2e.py` - 端到端完整流程测试

## 测试结果

测试截图将保存在 `screenshots/` 目录下。

## 配置说明

- `pytest.ini` - Pytest 配置文件
- `conftest.py` - 测试夹具和全局配置
- `requirements-test.txt` - 测试依赖包列表
