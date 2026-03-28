@echo off
echo ========================================
echo GoldenConnect Uninstaller
echo ========================================
echo.
echo This will remove GoldenConnect from your system.
echo.
set /p choice="Are you sure? (Y/N): "
if /i "%choice%"=="Y" goto uninstall
echo Uninstallation cancelled.
pause
exit /b 0

:uninstall
echo.
echo Removing GoldenConnect...

REM Remove files
if exist "%PROGRAMFILES%\GoldenConnect" rmdir /s /q "%PROGRAMFILES%\GoldenConnect"

REM Remove shortcuts
if exist "%APPDATA%\Microsoft\Windows\Start Menu\Programs\GoldenConnect" rmdir /s /q "%APPDATA%\Microsoft\Windows\Start Menu\Programs\GoldenConnect"
if exist "%USERPROFILE%\Desktop\GoldenConnect.bat" del "%USERPROFILE%\Desktop\GoldenConnect.bat"

echo.
echo ========================================
echo UNINSTALLATION COMPLETE!
echo ========================================
echo.
echo GoldenConnect has been removed from your system.
echo.
pause
