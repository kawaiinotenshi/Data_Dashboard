# 日志工具函数 - 确保日志以追加模式写入

<#
.SYNOPSIS
    向project.log追加日志记录

.DESCRIPTION
    此函数确保日志以追加模式写入project.log文件，不会覆盖已有内容。
    使用UTF-8编码确保中文正确显示。

.PARAMETER Message
    要写入的日志消息

.PARAMETER LogPath
    日志文件路径（默认为FrontEnd/project.log）

.EXAMPLE
    Write-Log "测试日志消息"
    Write-Log "完成了一项任务" -LogPath "custom.log"
#>
function Write-Log {
    param(
        [Parameter(Mandatory=$true)]
        [string]$Message,
        
        [string]$LogPath = "FrontEnd/project.log"
    )
    
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $logEntry = "【$timestamp】$Message"
    
    # 使用Add-Content追加内容，-Encoding UTF8确保中文正确显示
    Add-Content -Path $LogPath -Value $logEntry -Encoding UTF8
}

<#
.SYNOPSIS
    向project.log追加格式化的日志记录

.DESCRIPTION
    此函数用于写入多行格式化的日志记录，包含标题和详细内容。

.PARAMETER Title
    日志标题

.PARAMETER Details
    日志详细内容（可以是多行）

.PARAMETER LogPath
    日志文件路径（默认为FrontEnd/project.log）

.EXAMPLE
    Write-FormattedLog "任务完成" @(
        "- 完成了功能A",
        "- 完成了功能B",
        "- 状态: 成功"
    )
#>
function Write-FormattedLog {
    param(
        [Parameter(Mandatory=$true)]
        [string]$Title,
        
        [Parameter(Mandatory=$true)]
        [string[]]$Details,
        
        [string]$LogPath = "FrontEnd/project.log"
    )
    
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm"
    
    # 写入分隔线
    Add-Content -Path $LogPath -Value "========================================" -Encoding UTF8
    
    # 写入标题
    Add-Content -Path $LogPath -Value "【$timestamp】$Title" -Encoding UTF8
    
    # 写入详细内容
    foreach ($detail in $Details) {
        Add-Content -Path $LogPath -Value $detail -Encoding UTF8
    }
    
    # 写入分隔线
    Add-Content -Path $LogPath -Value "========================================" -Encoding UTF8
    Add-Content -Path $LogPath -Value "" -Encoding UTF8
}