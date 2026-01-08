@echo off
setlocal enabledelayedexpansion

echo =======================================
echo 物流仓储大数据展示系统启动脚本
echo =======================================
echo.

:: 检查是否有Git Bash环境
where git >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo 未检测到Git Bash环境，请确保已安装Git并添加到系统PATH中。
    pause
    exit /b 1
)

:: 检查是否有Java环境
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo 未检测到Java环境，请确保已安装JDK 1.8或更高版本并添加到系统PATH中。
    pause
    exit /b 1
)

:: 检查是否有Maven环境
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo 未检测到Maven环境，请确保已安装Maven并添加到系统PATH中。
    pause
    exit /b 1
)

:: 检查是否有Node.js环境
where node >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo 未检测到Node.js环境，请确保已安装Node.js 14或更高版本并添加到系统PATH中。
    pause
    exit /b 1
)

:: 检查是否有npm环境
where npm >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo 未检测到npm环境，请确保已安装npm并添加到系统PATH中。
    pause
    exit /b 1
)

echo 环境检查通过，正在启动服务...
echo.

:: 检查是否已安装依赖
if not exist "FrontEnd\node_modules" (
    echo 正在安装前端依赖...
    cd /d "FrontEnd"
    npm install
    if %ERRORLEVEL% NEQ 0 (
        echo 前端依赖安装失败，请检查网络或package.json配置。
        pause
        exit /b 1
    )
    cd ..
)

echo.
echo 启动后端服务（Spring Boot）
start "后端服务" cmd /k "cd /d "backend" && mvn spring-boot:run -D"spring-boot.run.profiles"=dev"

:: 等待后端服务启动（约5秒）
echo 等待后端服务启动...
timeout /t 5 /nobreak >nul

echo.
echo 启动前端服务（Vue）
start "前端服务" cmd /k "cd /d "FrontEnd" && npm run dev"

echo.
echo =======================================
echo 服务启动完成！
echo 访问地址：
echo - 前端面板：http://localhost:3000
echo - 后端API：http://localhost:8081
echo - API文档：http://localhost:8081/swagger-ui.html (未配置)
echo =======================================
echo.
echo 注意事项：
echo 1. 首次启动可能需要较长时间，请耐心等待。
echo 2. 如果服务启动失败，请检查命令窗口中的错误信息。
echo 3. 确保MySQL和Redis服务已启动并正确配置。
echo 4. 数据库配置文件位于：backend/src/main/resources/application-dev.yml
echo.
pause
