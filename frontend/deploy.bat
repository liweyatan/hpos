@echo off
echo === Building Vue frontend ===
call npm run build
if errorlevel 1 (
    echo Build failed!
    pause
    exit /b 1
)
echo === Copying to backend static directory ===
xcopy /E /Y /Q dist\* "..\backend\src\main\resources\static\"
echo === Done! Frontend deployed to backend ===
echo.
echo To start the full system:
echo   cd ..\backend
echo   .\mvnw.cmd spring-boot:run
echo.
pause
