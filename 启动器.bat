@echo off
chcp 65001 > nul
echo ========================================
echo 物流仓储大数据面板 - 项目启动器
echo ========================================
echo.

python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Python，请先安装 Python 3.7+
    echo 下载地址: https://www.python.org/downloads/
    pause
    exit /b 1
)

echo [信息] 正在启动项目启动器...
echo.

python launcher.py

if %errorlevel% neq 0 (
    echo.
    echo [错误] 启动器运行失败
    pause
)
