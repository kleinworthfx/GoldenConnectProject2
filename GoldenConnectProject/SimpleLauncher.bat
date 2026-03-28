@echo off
echo ========================================
echo GoldenConnect Simple Launcher
========================================
echo.

REM Set the database password
set GOLDENCONNECT_DB_PASSWORD=7Hesed7Joseph7

echo Database password set.
echo Starting GoldenConnect application...
echo.

REM Run using Maven (handles JavaFX automatically)
mvn javafx:run

echo.
echo Application closed.
pause
