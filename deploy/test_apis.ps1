# API接口测试脚本

$baseUrl = "http://localhost:8080/api"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "开始测试所有API接口" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$apis = @(
    @{ Name = "订单列表"; Method = "GET"; Url = "/order/list"; ExpectedFields = @("id", "orderNo", "status", "amount") },
    @{ Name = "订单统计"; Method = "GET"; Url = "/order/statistics"; ExpectedFields = @("totalOrders", "pendingOrders", "completedOrders", "totalAmount") },
    @{ Name = "仓库列表"; Method = "GET"; Url = "/warehouse/list"; ExpectedFields = @("id", "name", "location", "capacity") },
    @{ Name = "仓库统计"; Method = "GET"; Url = "/warehouse/statistics"; ExpectedFields = @("totalWarehouses", "totalCapacity", "usedCapacity", "availableCapacity") },
    @{ Name = "客户列表"; Method = "GET"; Url = "/customer/list"; ExpectedFields = @("id", "name", "phone", "email") },
    @{ Name = "供应商列表"; Method = "GET"; Url = "/supplier/list"; ExpectedFields = @("id", "name", "contact", "phone") },
    @{ Name = "运输列表"; Method = "GET"; Url = "/transport/list"; ExpectedFields = @("id", "vehicleNo", "driver", "status") },
    @{ Name = "运输统计"; Method = "GET"; Url = "/transport/statistics"; ExpectedFields = @("totalTransports", "inProgress", "completed") },
    @{ Name = "仪表盘数据"; Method = "GET"; Url = "/dashboard/data"; ExpectedFields = @("orderCount", "warehouseCount", "transportCount") }
)

$successCount = 0
$failCount = 0

foreach ($api in $apis) {
    Write-Host "`n测试: $($api.Name)" -ForegroundColor Yellow
    Write-Host "URL: $baseUrl$($api.Url)" -ForegroundColor Gray
    
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($api.Url)" -Method $api.Method -TimeoutSec 10
        
        if ($response.code -eq 200 -or $response.success -eq $true) {
            Write-Host "✓ 状态: 成功" -ForegroundColor Green
            
            $data = $response.data
            if ($data) {
                if ($data -is [System.Array]) {
                    Write-Host "✓ 数据类型: 数组，条数: $($data.Count)" -ForegroundColor Green
                    if ($data.Count -gt 0) {
                        $firstItem = $data[0]
                        $missingFields = @()
                        foreach ($field in $api.ExpectedFields) {
                            if (-not $firstItem.PSObject.Properties.Name.Contains($field)) {
                                $missingFields += $field
                            }
                        }
                        if ($missingFields.Count -eq 0) {
                            Write-Host "✓ 字段验证: 通过" -ForegroundColor Green
                        } else {
                            Write-Host "✗ 字段验证: 缺少字段 $($missingFields -join ', ')" -ForegroundColor Red
                        }
                    }
                } else {
                    Write-Host "✓ 数据类型: 对象" -ForegroundColor Green
                    $missingFields = @()
                    foreach ($field in $api.ExpectedFields) {
                        if (-not $data.PSObject.Properties.Name.Contains($field)) {
                            $missingFields += $field
                        }
                    }
                    if ($missingFields.Count -eq 0) {
                        Write-Host "✓ 字段验证: 通过" -ForegroundColor Green
                    } else {
                        Write-Host "✗ 字段验证: 缺少字段 $($missingFields -join ', ')" -ForegroundColor Red
                    }
                }
                
                Write-Host "数据预览: $($data | ConvertTo-Json -Depth 2 -Compress)" -ForegroundColor Gray
            } else {
                Write-Host "⚠ 数据为空" -ForegroundColor Yellow
            }
            
            $successCount++
        } else {
            Write-Host "✗ 状态: 失败 - $($response.message)" -ForegroundColor Red
            $failCount++
        }
    } catch {
        Write-Host "✗ 异常: $($_.Exception.Message)" -ForegroundColor Red
        $failCount++
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "测试完成" -ForegroundColor Cyan
Write-Host "成功: $successCount / $($apis.Count)" -ForegroundColor Green
Write-Host "失败: $failCount / $($apis.Count)" -ForegroundColor Red
Write-Host "========================================" -ForegroundColor Cyan
