@echo off

cd %~dp0

start java -Dspring.profiles.active=free_edition -jar anonimecsFree.war

echo "waiting to start the app"
ping 192.0.2.2 -n 1 -w 15000 > nul
echo "application started"

start iexplore http://localhost:8080/anon-gui/connect/