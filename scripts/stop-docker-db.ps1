Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Docker 数据库停止脚本" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectDir

Write-Host "当前目录: $projectDir" -ForegroundColor Green
Write-Host ""

Write-Host "正在停止 MySQL 容器..." -ForegroundColor Yellow
docker-compose -f docker-compose-db.yml down

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ MySQL 容器已停止" -ForegroundColor Green
    Write-Host ""
    Write-Host "提示: 数据卷中的数据已保留" -ForegroundColor Cyan
    Write-Host "如需完全删除数据，请运行: docker-compose -f docker-compose-db.yml down -v" -ForegroundColor Gray
} else {
    Write-Host "✗ 停止失败" -ForegroundColor Red
    exit 1
}
