@echo off
title 🎓 School Management System - Quick Start
color 0B

echo.
echo ==========================================
echo    🎓 SCHOOL MANAGEMENT SYSTEM 🎓
echo ==========================================
echo.
echo 🚀 Quick Start - One Click Launch!
echo.
echo 📋 What this will do:
echo    ✅ Start the Spring Boot application
echo    ✅ Open your browser automatically
echo    ✅ Navigate to the login page
echo.
echo ⏱️  Please wait 30-60 seconds for startup...
echo.

REM Change to project directory
cd /d "%~dp0"

REM Start the application in background
echo 🔄 Starting application...
start /min cmd /c "mvn spring-boot:run"

REM Wait for application to start
echo ⏳ Waiting for application to start...
timeout /t 45 /nobreak >nul

REM Open browser automatically
echo 🌐 Opening browser...
start http://localhost:8080/school-login.html

echo.
echo ✅ Application should be starting!
echo 🌐 Browser will open automatically
echo 📱 If browser doesn't open, go to: http://localhost:8080/school-login.html
echo.
echo 🔐 Demo Credentials:
echo    👨‍💼 Admin: admin@school.com / admin123
echo    👩‍🏫 Teacher: teacher@school.com / teacher123  
echo    🎓 Student: student@school.com / student123
echo.
echo 🛑 To stop the application, close this window
echo.
pause
