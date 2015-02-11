@echo off

cd %~dp0


set mypath=%~dp0
set DERBY_INSTALL=%mypath%db\db-derby-10.4.2.0-bin
set CLASSPATH=%DERBY_INSTALL%lib\derbytools.jar;%DERBY_INSTALL%lib\derbynet.jar;.

start %DERBY_INSTALL%\bin\startNetworkServer.bat

echo "waiting to start the db"
timeout /T 3  > nul
echo "database started"



start java -Dspring.profiles.active=enterprise_edition -Dderby.dir=%mypath% -jar anonimecsEnterprise.war

echo "waiting to start the app"
timeout /T 20  > nul
echo "application started"


start iexplore http://localhost:8080/anon/pages/connection/connectionAdd.jsf