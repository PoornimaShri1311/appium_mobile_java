# PowerShell script to get the latest ExtentReport
$reportsDir = "reports"
$latestReport = Get-ChildItem "$reportsDir\ExtentReport_*.html" | Sort-Object Name -Descending | Select-Object -First 1

if ($latestReport) {
    Write-Host "📊 Latest ExtentReport: $($latestReport.Name)"
    Write-Host "🕒 Created: $($latestReport.CreationTime)"
    Write-Host "📁 Full path: $($latestReport.FullName)"
    
    # Option to open the report
    $response = Read-Host "Would you like to open this report? (y/n)"
    if ($response -eq 'y' -or $response -eq 'Y') {
        Start-Process $latestReport.FullName
    }
} else {
    Write-Host "❌ No timestamped ExtentReports found in $reportsDir directory"
}