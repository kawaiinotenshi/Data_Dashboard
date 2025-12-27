Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Docker 数据库启动脚本" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectDir

Write-Host "当前目录: $projectDir" -ForegroundColor Green
Write-Host ""

Write-Host "步骤 1: 检查 Docker 是否运行..." -ForegroundColor Yellow
try {
    docker ps | Out-Null
    Write-Host "✓ Docker 正在运行" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker 未运行，请先启动 Docker Desktop" -ForegroundColor Red
    exit 1
}
Write-Host ""

Write-Host "步骤 2: 检查是否存在旧容器..." -ForegroundColor Yellow
$existingContainer = docker ps -a -q -f name=logistics-mysql
if ($existingContainer) {
    Write-Host "发现旧容器，正在删除..." -ForegroundColor Yellow
    docker rm -f logistics-mysql | Out-Null
    Write-Host "✓ 旧容器已删除" -ForegroundColor Green
} else {
    Write-Host "✓ 没有旧容器" -ForegroundColor Green
}
Write-Host ""

Write-Host "步骤 3: 启动 MySQL 容器..." -ForegroundColor Yellow
docker-compose -f docker-compose-db.yml up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ MySQL 容器启动成功" -ForegroundColor Green
    Write-Host ""
    Write-Host "步骤 4: 等待 MySQL 初始化..." -ForegroundColor Yellow
    Write-Host "这可能需要 30-60 秒..." -ForegroundColor Gray
    
    $maxAttempts = 30
    $attempt = 0
    
    while ($attempt -lt $maxAttempts) {
        $attempt++
        Write-Host "检查中... ($attempt/$maxAttempts)" -NoNewline -ForegroundColor Gray
        
        try {
            docker exec logistics-mysql mysqladmin ping -h localhost -uroot -proot 2>$null | Out-Null
            Write-Host "`r✓ MySQL 已就绪!                   " -ForegroundColor Green
            break
        } catch {
            Start-Sleep -Seconds 2
            Write-Host "`r检查中... ($attempt/$maxAttempts)    " -NoNewline -ForegroundColor Gray
        }
    }
    
    if ($attempt -eq $maxAttempts) {
        Write-Host "`r✗ MySQL 启动超时" -ForegroundColor Red
        Write-Host ""
        Write-Host "查看日志:" -ForegroundColor Yellow
        docker-compose -f docker-compose-db.yml logs mysql
        exit 1
    }
    
    Write-Host ""
    Write-Host "步骤 5: 验证数据库初始化..." -ForegroundColor Yellow
    $tables = docker exec logistics-mysql mysql -uroot -proot logistics_db -e "SHOW TABLES;" 2>$null
    if ($tables) {
        Write-Host "✓ 数据库初始化成功" -ForegroundColor Green
        Write-Host ""
        Write-Host "数据库表:" -ForegroundColor Cyan
        docker exec logistics-mysql mysql -uroot -proot logistics_db -e "SHOW TABLES;" 2>$null | Select-Object -Skip 1
    } else {
        Write-Host "✗ 数据库初始化失败" -ForegroundColor Red
        Write-Host ""
        Write-Host "查看日志:" -ForegroundColor Yellow
        docker-compose -f docker-compose-db.yml logs mysql
        exit 1
    }
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "Docker 数据库启动成功!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "连接信息:" -ForegroundColor Cyan
    Write-Host "  主机: localhost" -ForegroundColor White
    Write-Host "  端口: 3306" -ForegroundColor White
    Write-Host "  数据库: logistics_db" -ForegroundColor White
    Write-Host "  用户名: root" -ForegroundColor White
    Write-Host "  密码: root" -ForegroundColor White
    Write-Host ""
    Write-Host "常用命令:" -ForegroundColor Cyan
    Write-Host "  查看日志: docker-compose -f docker-compose-db.yml logs -f" -ForegroundColor White
    Write-Host "  停止数据库: docker-compose -f docker-compose-db.yml down" -ForegroundColor White
    Write-Host "  进入数据库: docker exec -it logistics-mysql mysql -uroot -proot logistics_db" -ForegroundColor White
    Write-Host ""
    
} else {
    Write-Host "✗ MySQL 容器启动失败" -ForegroundColor Red
    Write-Host ""
    Write-Host "查看日志:" -ForegroundColor Yellow
    docker-compose -f docker-compose-db.yml logs mysql
    exit 1
}
