start java -Dspring.profiles.active=derby_embedded -jar ..\..\..\target\anon-gui-0.1-SNAPSHOT.war
sleep 5
start iexplore http://localhost:8080/anon-gui/pages/cockpit.jsf