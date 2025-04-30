# PowerShell script to replace POM files with corrected versions
# Created on May 1, 2025

$projectRoot = "C:\Users\yumenna ezzat\Downloads\SW2--project"
$services = @("", "eureka-server", "api-gateway", "user-service", "file-service", "repository-service", "SW2Project")

Write-Host "Starting POM file replacement..." -ForegroundColor Green

foreach ($service in $services) {
    $serviceDir = Join-Path -Path $projectRoot -ChildPath $service
    $originalPomPath = Join-Path -Path $serviceDir -ChildPath "pom.xml"
    $correctedPomPath = Join-Path -Path $serviceDir -ChildPath "corrected-pom.xml"
    
    if ($service -eq "") {
        $correctedPomPath = Join-Path -Path $projectRoot -ChildPath "corrected-parent-pom.xml"
    }
    
    if (Test-Path $correctedPomPath) {
        # Create backup of original POM
        $backupPath = Join-Path -Path $serviceDir -ChildPath "pom.xml.bak"
        Copy-Item -Path $originalPomPath -Destination $backupPath -Force
        
        # Replace original POM with corrected version
        Copy-Item -Path $correctedPomPath -Destination $originalPomPath -Force
        
        $serviceName = if ($service -eq "") { "parent" } else { $service }
        Write-Host "Replaced POM file for $serviceName service. Backup created at $backupPath" -ForegroundColor Yellow
    }
    else {
        Write-Host "Corrected POM file not found for $service" -ForegroundColor Red
    }
}

Write-Host "POM replacement completed." -ForegroundColor Green
Write-Host "To verify the changes, you can run: mvn validate" -ForegroundColor Cyan
