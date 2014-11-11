#!/bin/sh

echo "java 7 is required to run this application"
echo "logs are in anonimecsFree.log"

nohup java -Dspring.profiles.active=free_edition -jar anonimecsFree.war > anonimecsFree.log 2>&1 &
sleep 10
echo "go to http://`hostname`:8080/anon-gui/pages/cockpit.jsf"
