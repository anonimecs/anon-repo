@echo off

set "CURRENT_DIR=%cd%"
set "DERBY_INSTALL=%CURRENT_DIR%\db"
set "DERBY_EXECUTABLE=%DERBY_INSTALL%\bin\startNetworkServer.bat"
set "DERBY_OPTS=-Dderby.system.home=%CURRENT_DIR%\db\data"
set "CLASSPATH=%DERBY_INSTALL%lib\derbytools.jar;%DERBY_INSTALL%lib\derbynet.jar;."
set "CATALINA_HOME=%CURRENT_DIR%\app"
set "APP_EXECUTABLE=%CATALINA_HOME%\bin\catalina.bat"
set "CATALINA_OPTS=-Xms1024M -Xmx2048M -Dspring.profiles.active=enterprise_edition -Dderby.dir=%CURRENT_DIR%"

start /min %DERBY_EXECUTABLE%

echo "waiting to start the db"
timeout /T 3  > nul
echo "database started"

call "%APP_EXECUTABLE%" start

echo "waiting to start the app"
timeout /T 25  > nul
echo "application started"

start iexplore http://localhost:8080/anon