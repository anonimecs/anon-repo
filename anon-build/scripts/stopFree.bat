@echo off

set "CURRENT_DIR=%cd%"
set "CATALINA_HOME=%CURRENT_DIR%\app"
set "APP_EXECUTABLE=%CATALINA_HOME%\bin\catalina.bat"

call "%APP_EXECUTABLE%" stop
echo "waiting to stop the app"
timeout /T 5  > nul
echo "application stopped"
