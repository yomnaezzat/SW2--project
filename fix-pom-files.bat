@echo off
echo Fixing POM files by replacing ^<n^> tag with ^<name^> tag...

REM Fix common-library pom.xml
powershell -Command "(Get-Content -Raw 'common-library\pom.xml') -replace '<n>(.*?)</n>', '<n>$1</n>' | Set-Content 'common-library\pom.xml'"
echo Fixed common-library\pom.xml

REM Fix api-gateway pom.xml
powershell -Command "(Get-Content -Raw 'api-gateway\pom.xml') -replace '<n>(.*?)</n>', '<n>$1</n>' | Set-Content 'api-gateway\pom.xml'"
echo Fixed api-gateway\pom.xml

REM Fix user-service pom.xml
powershell -Command "(Get-Content -Raw 'user-service\pom.xml') -replace '<n>(.*?)</n>', '<n>$1</n>' | Set-Content 'user-service\pom.xml'"
echo Fixed user-service\pom.xml

REM Fix file-service pom.xml
powershell -Command "(Get-Content -Raw 'file-service\pom.xml') -replace '<n>(.*?)</n>', '<n>$1</n>' | Set-Content 'file-service\pom.xml'"
echo Fixed file-service\pom.xml

REM Fix repository-service pom.xml
powershell -Command "(Get-Content -Raw 'repository-service\pom.xml') -replace '<n>(.*?)</n>', '<n>$1</n>' | Set-Content 'repository-service\pom.xml'"
echo Fixed repository-service\pom.xml

REM Fix parent pom.xml
powershell -Command "(Get-Content -Raw 'pom.xml') -replace '<n>(.*?)</n>', '<n>$1</n>' | Set-Content 'pom.xml'"
echo Fixed pom.xml

echo.
echo All POM files have been fixed. Run 'mvn clean install -DskipTests' to build the project.
echo.
