@echo off

cd %~dp0

start java -Dspring.profiles.active=free_edition -jar anonimecsFree.war

echo "waiting to start the app"
timeout /T 25  > nul
echo "application started"

start iexplore http://localhost:8080/anon/pages/connection/connectionAdd.jsf