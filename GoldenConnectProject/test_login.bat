@echo off
echo Testing GoldenConnect Login Flow...
echo.

echo 1. Compiling the project...
call mvn clean compile

echo.
echo 2. Running the main application...
call mvn javafx:run

pause
