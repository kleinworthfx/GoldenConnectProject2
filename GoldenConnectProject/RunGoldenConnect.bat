@echo off
echo ========================================
echo GoldenConnect JAR Launcher
========================================
echo.

REM Set the database password
set GOLDENCONNECT_DB_PASSWORD=7Hesed7Joseph7

echo Database password set.
echo Starting GoldenConnect application...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Check if JAR file exists
if not exist "target\goldenconnect-1.0.0.jar" (
    echo ERROR: Application JAR file not found!
    echo Please run: mvn clean package -DskipTests
    echo.
    pause
    exit /b 1
)

REM Run the application directly from JAR
java -jar "%~dp0target\goldenconnect-1.0.0.jar"

echo.
echo Application closed.
pause
