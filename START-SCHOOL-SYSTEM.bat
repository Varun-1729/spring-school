@echo off
title School Management System - Starting...
color 0A

echo.
echo ========================================
echo   🎓 SCHOOL MANAGEMENT SYSTEM 🎓
echo ========================================
echo.
echo 🚀 Starting application...
echo 📊 Please wait while the system loads...
echo.

REM Change to project directory
cd /d "%~dp0"

REM Start the Spring Boot application
echo ⚡ Launching Spring Boot application...
mvn spring-boot:run

REM If Maven fails, try with Java directly
if errorlevel 1 (
    echo.
    echo ⚠️  Maven command failed, trying alternative method...
    echo.
    
    REM Build first
    mvn clean package -DskipTests
    
    REM Run the JAR file
    java -jar target\*.jar
)

REM Keep window open if there's an error
if errorlevel 1 (
    echo.
    echo ❌ Error starting the application!
    echo 📋 Please check the error messages above.
    echo.
    pause
)
