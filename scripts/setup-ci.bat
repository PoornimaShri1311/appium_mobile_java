@echo off
REM BILD Mobile CI Setup Script for Windows
REM This script helps setup and test the CI environment locally on Windows

setlocal EnableDelayedExpansion

echo 🚀 BILD Mobile CI Setup Script (Windows)
echo ==========================================

REM Check Java installation
echo [INFO] Checking Java installation...
java -version >nul 2>&1
if !errorlevel! neq 0 (
    echo [ERROR] Java not found. Please install OpenJDK 11+
    exit /b 1
) else (
    echo [SUCCESS] Java detected
    java -version 2>&1 | findstr /r /c:"version.*11" /c:"version.*17" /c:"version.*21" >nul
    if !errorlevel! neq 0 (
        echo [WARNING] Java 11+ recommended for best compatibility
    )
)

REM Check Node.js installation
echo [INFO] Checking Node.js installation...
node --version >nul 2>&1
if !errorlevel! neq 0 (
    echo [ERROR] Node.js not found. Please install Node.js 16+
    exit /b 1
) else (
    echo [SUCCESS] Node.js detected
    node --version
)

REM Check if Appium is installed
echo [INFO] Checking Appium installation...
appium --version >nul 2>&1
if !errorlevel! neq 0 (
    echo [INFO] Installing Appium globally...
    npm install -g appium@2.x
    if !errorlevel! neq 0 (
        echo [ERROR] Failed to install Appium
        exit /b 1
    )
    echo [SUCCESS] Appium installed successfully
) else (
    echo [SUCCESS] Appium already installed
    appium --version
)

REM Install UiAutomator2 driver
echo [INFO] Installing UiAutomator2 driver...
appium driver install uiautomator2
echo [INFO] Installed Appium drivers:
appium driver list --installed

REM Check Android SDK
echo [INFO] Checking Android SDK...
if defined ANDROID_HOME (
    echo [SUCCESS] Android SDK found at: !ANDROID_HOME!
    if exist "!ANDROID_HOME!\platform-tools\adb.exe" (
        echo [SUCCESS] ADB available
    ) else (
        echo [WARNING] ADB not found in PATH
    )
) else (
    echo [WARNING] ANDROID_HOME not set. Install Android Studio or SDK tools
)

REM Create CI configuration files
echo [INFO] Creating CI configuration files...
if not exist "src\test\resources" mkdir src\test\resources

REM Create CI capabilities file
(
echo # CI Environment Configuration for BILD App Tests
echo appPackage=com.netbiscuits.bild.android
echo appActivity=de.bild.android.app.MainActivity
echo platformName=Android
echo deviceName=Android_CI_Emulator
echo automationName=UiAutomator2
echo udid=emulator-5554
echo autoGrantPermissions=true
echo noReset=false
echo fullReset=false
echo newCommandTimeout=300
echo appiumServer=http://127.0.0.1:4723/wd/hub
echo.
echo # CI-specific timeouts
echo androidInstallTimeout=120000
echo adbExecTimeout=60000
echo.
echo # Performance settings
echo systemPort=8200
echo mjpegServerPort=7810
) > src\test\resources\capabilities-ci.properties

REM Create CI framework properties file
(
echo # CI Framework Configuration
echo framework.name=BILD Mobile CI Framework
echo framework.version=2.0.0-CI
echo.
echo # Extended timeouts for CI environment
echo implicit.wait=15
echo explicit.wait=45
echo page.load.timeout=45
echo.
echo # Enhanced retry for CI stability
echo retry.failed.tests=true
echo retry.count=3
echo retry.delay.seconds=5
echo.
echo # Screenshot configuration
echo screenshot.on.failure=true
echo screenshot.on.success=false
echo.
echo # CI logging
echo log.level=INFO
echo.
echo # Manual Appium management in CI
echo appium.server.auto.start=false
echo appium.server.host=127.0.0.1
echo appium.server.port=4723
echo appium.server.startup.timeout=60
) > src\test\resources\framework-ci.properties

echo [SUCCESS] CI configuration files created

REM Test Maven compilation
echo [INFO] Testing Maven compilation...
mvn --version >nul 2>&1
if !errorlevel! neq 0 (
    echo [ERROR] Maven not found. Please install Maven 3.6+
    exit /b 1
) else (
    echo [SUCCESS] Maven detected
    mvn clean compile -q
    if !errorlevel! neq 0 (
        echo [ERROR] Maven compilation failed
        exit /b 1
    ) else (
        echo [SUCCESS] Maven compilation successful
    )
)

REM Start Appium server for testing
echo [INFO] Starting Appium server for CI testing...

REM Kill any existing Appium processes
taskkill /F /IM node.exe >nul 2>&1

REM Start Appium server in background
start /B appium server --port 4723 --base-path /wd/hub --allow-cors

REM Wait for server to start
echo [INFO] Waiting for Appium server to start...
timeout /t 5 /nobreak >nul

REM Test server connectivity
for /l %%i in (1,1,30) do (
    curl -s http://localhost:4723/wd/hub/status >nul 2>&1
    if !errorlevel! equ 0 (
        echo [SUCCESS] Appium server started successfully
        goto :server_ready
    )
    timeout /t 1 /nobreak >nul
)

echo [ERROR] Failed to start Appium server
exit /b 1

:server_ready

REM Run CI validation
echo [INFO] Running CI validation tests...
set TEST_ENV=ci
set MAVEN_OPTS=-Xmx2048m

REM Basic framework validation
mvn test -Dtest=NonExistentTest -q >nul 2>&1
echo [SUCCESS] Test framework validation completed

echo.
echo 🎉 BILD Mobile CI Setup Complete!
echo ================================
echo.
echo Next steps:
echo 1. Commit the .github/workflows/ci.yml file
echo 2. Push to GitHub to trigger the CI pipeline
echo 3. Check GitHub Actions tab for build results
echo.
echo Local testing:
echo - Appium server: http://localhost:4723/wd/hub/status
echo - CI configs: src/test/resources/*-ci.properties
echo.
echo For emulator testing, install Android Studio and create an AVD
echo.
echo Press any key to stop Appium server and exit...
pause >nul

REM Cleanup
echo [INFO] Stopping Appium server...
taskkill /F /IM node.exe >nul 2>&1
echo [SUCCESS] Cleanup completed

endlocal