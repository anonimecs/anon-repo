@echo off

set "CURRENT_DIR=%cd%"
set "CATALINA_HOME=%CURRENT_DIR%\app"
set "APP_EXECUTABLE=%CATALINA_HOME%\bin\catalina.bat"
set "CATALINA_OPTS=-Xms1024M -Xmx2048M -Dspring.profiles.active=free_edition"

call "%APP_EXECUTABLE%" start

echo "waiting to start the app"
timeout /T 25  > nul
echo "application started"

start iexplore http://localhost:8080/anon