start java -Dspring.profiles.active=free_edition -jar anonimecsFree.war

echo "waiting to start the app"
ping 192.0.2.2 -n 1 -w 10000 > nul
echo "application started"

start iexplore http://localhost:8080/anon-gui/pages/cockpit.jsf