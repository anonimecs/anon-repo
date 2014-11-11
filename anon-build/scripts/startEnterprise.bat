@echo off

cd %~dp0

call db/startDerby.bat

echo "waiting to start the db"
ping 192.0.2.2 -n 1 -w 5000 > nul
echo "database started"

start java -Dspring.profiles.active=enterprise_edition -jar anonimecsEnterprise.war


echo "waiting to start the app"
ping 192.0.2.2 -n 1 -w 10000 > nul
echo "application started"


start iexplore http://localhost:8080/anon-gui/pages/cockpit.jsf