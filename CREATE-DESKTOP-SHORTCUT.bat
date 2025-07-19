@echo off
title Create Desktop Shortcut
color 0C

echo.
echo ==========================================
echo    🎓 SCHOOL MANAGEMENT SYSTEM 🎓
echo ==========================================
echo.
echo 🖥️  Creating Desktop Shortcut...
echo.

REM Get current directory
set "CURRENT_DIR=%~dp0"

REM Create VBS script to create shortcut
echo Set oWS = WScript.CreateObject("WScript.Shell") > CreateShortcut.vbs
echo sLinkFile = "%USERPROFILE%\Desktop\🎓 School Management System.lnk" >> CreateShortcut.vbs
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> CreateShortcut.vbs
echo oLink.TargetPath = "%CURRENT_DIR%RUN-SCHOOL-SYSTEM.bat" >> CreateShortcut.vbs
echo oLink.WorkingDirectory = "%CURRENT_DIR%" >> CreateShortcut.vbs
echo oLink.Description = "School Management System - One Click Launch" >> CreateShortcut.vbs
echo oLink.Save >> CreateShortcut.vbs

REM Execute VBS script
cscript CreateShortcut.vbs >nul

REM Clean up
del CreateShortcut.vbs

echo ✅ Desktop shortcut created successfully!
echo.
echo 🖥️  You can now find "🎓 School Management System" on your desktop
echo 🖱️  Double-click it to start the application
echo.
echo 📋 The shortcut will:
echo    ✅ Start the Spring Boot application
echo    ✅ Open browser automatically  
echo    ✅ Show you login credentials
echo    ✅ Provide menu options
echo.
pause
