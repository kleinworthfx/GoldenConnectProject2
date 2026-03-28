@echo off
echo ========================================
echo GoldenConnect - Bridging Generations
echo ========================================
echo.
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

REM Set database environment variables if not set
if "%GOLDENCONNECT_DB_PASSWORD%"=="" (
    echo Database password not set. Please set environment variables:
    echo set GOLDENCONNECT_DB_PASSWORD=your_mysql_password
    echo.
    echo Or run: run_with_db.bat
    echo.
    pause
    exit /b 1
)

REM Run the application
java -jar "%~dp0target\goldenconnect-1.0.0.jar"

echo.
echo Application closed.
pause
