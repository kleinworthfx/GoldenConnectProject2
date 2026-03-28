@echo off
echo ========================================
echo GoldenConnect Executable Builder
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Apache Maven
    pause
    exit /b 1
)

echo Building GoldenConnect application...
echo.

REM Clean and package the application
echo Step 1: Cleaning previous builds...
call mvn clean

echo Step 2: Compiling and packaging...
call mvn package -DskipTests

if errorlevel 1 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo Step 3: Creating executable wrapper...
echo.

REM Create a launcher script
echo @echo off > GoldenConnect.exe
echo echo Starting GoldenConnect... >> GoldenConnect.exe
echo java -jar "%~dp0target\goldenconnect-1.0.0.jar" >> GoldenConnect.exe
echo pause >> GoldenConnect.exe

echo.
echo ========================================
echo BUILD COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo Files created:
echo - target\goldenconnect-1.0.0.jar (Executable JAR)
echo - GoldenConnect.exe (Windows Launcher)
echo.
echo To run the application:
echo 1. Double-click GoldenConnect.exe
echo 2. Or run: java -jar target\goldenconnect-1.0.0.jar
echo.
echo Note: Users need Java 17+ installed to run the application
echo.
pause
