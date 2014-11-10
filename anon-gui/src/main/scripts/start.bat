start java -Dspring.profiles.active=free_edition -jar anonimecsFree.war
ping 192.0.2.2 -n 1 -w 10000 > nul 
start iexplore http://localhost:8080/anon-gui/pages/cockpit.jsf