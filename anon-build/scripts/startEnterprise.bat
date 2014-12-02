@echo off

cd %~dp0


set mypath=%~dp0
set DERBY_INSTALL=%mypath%db\db-derby-10.4.2.0-bin
set CLASSPATH=%DERBY_INSTALL%lib\derbytools.jar;%DERBY_INSTALL%lib\derbynet.jar;.

start %DERBY_INSTALL%\bin\startNetworkServer.bat

echo "waiting to start the db"
ping 192.0.2.2 -n 1 -w 5000 > nul
echo "database started"



start java -Dspring.profiles.active=enterprise_edition -jar anonimecsEnterprise.war

echo "waiting to start the app"
ping 192.0.2.3 -n 1 -w 10000 > nul
echo "application started"


start iexplore http://localhost:8080/anon-gui/login/