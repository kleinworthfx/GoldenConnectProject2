@echo off
echo ========================================
echo GoldenConnect Launcher
echo ========================================
echo.

REM Set the database password (change this to your actual password)
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

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Apache Maven
    echo.
    pause
    exit /b 1
)

REM Run the application using Maven (this handles JavaFX automatically)
mvn javafx:run

echo.
echo Application closed.
pause
