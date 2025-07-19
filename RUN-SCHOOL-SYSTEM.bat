@echo off
title 🎓 School Management System
color 0E

:MENU
cls
echo.
echo ==========================================
echo    🎓 SCHOOL MANAGEMENT SYSTEM 🎓
echo ==========================================
echo.
echo 🚀 Choose an option:
echo.
echo    [1] 🎯 Quick Start (Auto-open browser)
echo    [2] 🔧 Start Application Only  
echo    [3] 🌐 Open Browser (if app is running)
echo    [4] 🛑 Stop Application
echo    [5] ❌ Exit
echo.
set /p choice="Enter your choice (1-5): "

if "%choice%"=="1" goto QUICKSTART
if "%choice%"=="2" goto STARTONLY
if "%choice%"=="3" goto OPENBROWSER
if "%choice%"=="4" goto STOPAPP
if "%choice%"=="5" goto EXIT
goto MENU

:QUICKSTART
cls
echo.
echo 🚀 Quick Starting School Management System...
echo.
cd /d "%~dp0"
start /min cmd /c "mvn spring-boot:run"
echo ⏳ Waiting for application to start (45 seconds)...
timeout /t 45 /nobreak >nul
echo 🌐 Opening browser...
start http://localhost:8080/school-login.html
echo.
echo ✅ Application started and browser opened!
echo 📱 URL: http://localhost:8080/school-login.html
echo.
echo 🔐 Demo Credentials:
echo    👨‍💼 Admin: admin@school.com / admin123
echo    👩‍🏫 Teacher: teacher@school.com / teacher123  
echo    🎓 Student: student@school.com / student123
echo.
pause
goto MENU

:STARTONLY
cls
echo.
echo 🔧 Starting Application Only...
echo.
cd /d "%~dp0"
mvn spring-boot:run
pause
goto MENU

:OPENBROWSER
cls
echo.
echo 🌐 Opening Browser...
start http://localhost:8080/school-login.html
echo ✅ Browser opened! Go to: http://localhost:8080/school-login.html
pause
goto MENU

:STOPAPP
cls
echo.
echo 🛑 Stopping Application...
taskkill /f /im java.exe 2>nul
taskkill /f /im javaw.exe 2>nul
echo ✅ Application stopped!
pause
goto MENU

:EXIT
echo.
echo 👋 Goodbye!
timeout /t 2 >nul
exit
