@echo off
echo ========================================
echo GoldenConnect Database Setup
echo ========================================
echo.

REM Prompt for database credentials
set /p DB_USERNAME="Enter MySQL username (default: root): "
if "%DB_USERNAME%"=="" set DB_USERNAME=root

set /p DB_PASSWORD="Enter MySQL password: "
if "%DB_PASSWORD%"=="" (
    echo Error: Password is required!
    pause
    exit /b 1
)

set /p DB_URL="Enter database URL (default: jdbc:mysql://localhost:3306/goldenconnect): "
if "%DB_URL%"=="" set DB_URL=jdbc:mysql://localhost:3306/goldenconnect

echo.
echo Setting up environment variables...
echo Database URL: %DB_URL%
echo Username: %DB_USERNAME%
echo Password: [HIDDEN]
echo.

REM Set environment variables
set GOLDENCONNECT_DB_URL=%DB_URL%
set GOLDENCONNECT_DB_USERNAME=%DB_USERNAME%
set GOLDENCONNECT_DB_PASSWORD=%DB_PASSWORD%

echo Environment variables set successfully!
echo.
echo Starting GoldenConnect application...
echo.

REM Run the application
mvn javafx:run

echo.
echo Application closed.
pause
