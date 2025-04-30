# PowerShell script to fix POM files
# This script replaces <n> tags with <n> tags

$projectRoot = "C:\Users\yumenna ezzat\Downloads\SW2--project"
$pomFiles = @(
    "$projectRoot\pom.xml",
    "$projectRoot\common-library\pom.xml",
    "$projectRoot\api-gateway\pom.xml",
    "$projectRoot\user-service\pom.xml",
    "$projectRoot\file-service\pom.xml",
    "$projectRoot\repository-service\pom.xml"
)

foreach ($pomFile in $pomFiles) {
    if (Test-Path $pomFile) {
        Write-Host "Fixing $pomFile..."
        
        # Read the content
        $content = Get-Content -Path $pomFile -Raw
        
        # Create backup
        Copy-Item -Path $pomFile -Destination "$pomFile.bak" -Force
        
        # Replace the tag - this preserves the content inside the tag
        $fixedContent = $content -replace '<n>(.*?)</n>', '<n>$1</n>'
        
        # Write the fixed content back
        Set-Content -Path $pomFile -Value $fixedContent
        
        Write-Host "Fixed and backed up $pomFile"
    } else {
        Write-Host "File not found: $pomFile" -ForegroundColor Red
    }
}

Write-Host "`nAll POM files have been fixed. Run 'mvn clean install -DskipTests' to build the project." -ForegroundColor Green
