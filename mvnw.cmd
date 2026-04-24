@ECHO OFF
SETLOCAL

set "SCRIPT_DIR=%~dp0"
set "WRAPPER_DIR=%SCRIPT_DIR%.mvn\wrapper"
set "PROPERTIES_FILE=%WRAPPER_DIR%\maven-wrapper.properties"

for /f "tokens=1,* delims==" %%A in ('type "%PROPERTIES_FILE%"') do (
    if /I "%%A"=="mavenVersion" set "MAVEN_VERSION=%%B"
    if /I "%%A"=="distributionUrl" set "DISTRIBUTION_URL=%%B"
)

if not defined MAVEN_VERSION set "MAVEN_VERSION=3.9.9"
if not defined DISTRIBUTION_URL set "DISTRIBUTION_URL=https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip"

set "MAVEN_HOME=%WRAPPER_DIR%\apache-maven-%MAVEN_VERSION%"

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Apache Maven %MAVEN_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $wrapperDir='%WRAPPER_DIR%'; $zipPath=Join-Path $wrapperDir ('apache-maven-%MAVEN_VERSION%-bin.zip'); if (-not (Test-Path $wrapperDir)) { New-Item -ItemType Directory -Force -Path $wrapperDir | Out-Null }; Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile $zipPath; Expand-Archive -Path $zipPath -DestinationPath $wrapperDir -Force"
    if errorlevel 1 exit /b %errorlevel%
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %errorlevel%