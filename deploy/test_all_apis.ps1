# API Test Script - Test all backend interfaces
$baseUrl = "http://localhost:8080"
$headers = @{
    "Content-Type" = "application/json"
}

Write-Host "Starting API interface testing..." -ForegroundColor Green
Write-Host ""

$apiTests = @(
    @{Name = "Dashboard Data API"; Url = "$baseUrl/api/dashboard/data"; Method = "GET"},
    @{Name = "Order Statistics API"; Url = "$baseUrl/api/order/statistics"; Method = "GET"},
    @{Name = "Order List API"; Url = "$baseUrl/api/order/list"; Method = "GET"},
    @{Name = "Warehouse Statistics API"; Url = "$baseUrl/api/warehouse/statistics"; Method = "GET"},
    @{Name = "Warehouse List API"; Url = "$baseUrl/api/warehouse/list"; Method = "GET"},
    @{Name = "Transport Statistics API"; Url = "$baseUrl/api/transport/statistics"; Method = "GET"},
    @{Name = "Transport List API"; Url = "$baseUrl/api/transport/list"; Method = "GET"},
    @{Name = "Customer Statistics API"; Url = "$baseUrl/api/customer/statistics"; Method = "GET"},
    @{Name = "Customer List API"; Url = "$baseUrl/api/customer/list"; Method = "GET"},
    @{Name = "Supplier Statistics API"; Url = "$baseUrl/api/supplier/statistics"; Method = "GET"},
    @{Name = "Supplier List API"; Url = "$baseUrl/api/supplier/list"; Method = "GET"}
)

$successCount = 0
$failCount = 0

foreach ($test in $apiTests) {
    Write-Host "Testing: $($test.Name)" -ForegroundColor Cyan
    Write-Host "URL: $($test.Url)" -ForegroundColor Gray
    
    try {
        $response = Invoke-RestMethod -Uri $test.Url -Method $test.Method -Headers $headers -ErrorAction Stop
        
        Write-Host "Status: Success" -ForegroundColor Green
        Write-Host "Response Data: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor White
        $successCount++
    }
    catch {
        Write-Host "Status: Failed" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
            Write-Host "HTTP Status Code: $statusCode" -ForegroundColor Red
        }
        $failCount++
    }
    
    Write-Host ""
}

Write-Host "======================================" -ForegroundColor Yellow
Write-Host "Testing Complete!" -ForegroundColor Yellow
Write-Host "Success: $successCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red
Write-Host "======================================" -ForegroundColor Yellow
