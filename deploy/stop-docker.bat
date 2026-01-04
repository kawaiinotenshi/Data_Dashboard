@echo off
chcp 65001 > nul
echo ========================================
echo 物流仓储大数据展示系统 - Docker停止
echo ========================================
echo.

cd /d "%~dp0"

echo [1/2] 停止所有服务...
docker-compose down
if %errorlevel% neq 0 (
    echo 警告: 停止服务时出现错误
    echo.
)
echo.

echo [2/2] 清理未使用的资源...
echo 可选: 是否删除数据卷（会删除所有数据库数据）？
set /p delete_volumes="输入 y 删除数据卷，其他键保留数据: "
if /i "%delete_volumes%"=="y" (
    echo 正在删除数据卷...
    docker-compose down -v
    echo 数据卷已删除
) else (
    echo 数据卷已保留
)
echo.

echo ========================================
echo 停止完成！
echo ========================================
echo.
echo 如需重新启动，请运行: start-docker.bat
echo.

pause
