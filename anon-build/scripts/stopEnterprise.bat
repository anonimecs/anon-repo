@echo off

set "CURRENT_DIR=%cd%"
set "DERBY_INSTALL=%CURRENT_DIR%\db"
set "DERBY_EXECUTABLE=%DERBY_INSTALL%\bin\stopNetworkServer.bat"
set "DERBY_OPTS=-Dderby.system.home=%CURRENT_DIR%\db\data"
set "CLASSPATH=%DERBY_INSTALL%lib\derbytools.jar;%DERBY_INSTALL%lib\derbynet.jar;."
set "CATALINA_HOME=%CURRENT_DIR%\app"
set "APP_EXECUTABLE=%CATALINA_HOME%\bin\catalina.bat"

call "%APP_EXECUTABLE%" stop
echo "waiting to stop the app"
timeout /T 5  > nul
echo "application stoped"

call %DERBY_EXECUTABLE%
echo "waiting to stop the db"
timeout /T 3  > nul
echo "database stoped"
