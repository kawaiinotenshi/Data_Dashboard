@echo off
chcp 65001 >nul
echo ========================================
echo     大数据面板系统 - Docker启动脚本
echo ========================================
echo.

echo [1/4] 正在启动Docker容器...
docker-compose up -d

echo.
echo [2/4] 等待服务启动...
timeout /t 10 /nobreak >nul

echo.
echo [3/4] 检查服务状态...
docker-compose ps

echo.
echo ========================================
echo     服务访问地址
echo ========================================
echo.
echo 前端页面（三个看板）：
echo   - 物流看板：     http://localhost/dashboard
echo   - 供应链看板：   http://localhost/supply-chain
echo   - 管理系统：     http://localhost/admin
echo.
echo 后端服务：
echo   - API接口：      http://localhost:8080/api
echo   - Swagger文档：  http://localhost:8080/swagger-ui.html
echo.
echo 监控服务：
echo   - Grafana：      http://localhost:3000  (admin/admin)
echo   - Prometheus：   http://localhost:9090
echo.
echo 数据库：
echo   - MySQL：        localhost:3307 (root/root)
echo   - Redis：        localhost:6379
echo.
echo ========================================
echo     常用命令
echo ========================================
echo.
echo 查看日志： docker-compose logs -f [服务名]
echo 停止服务： docker-compose down
echo 重启服务： docker-compose restart [服务名]
echo 查看状态： docker-compose ps
echo.
echo ========================================
echo     系统启动完成！
echo ========================================
echo.

pause
