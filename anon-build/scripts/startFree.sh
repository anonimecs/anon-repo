#!/bin/bash

#test the java version
mkfifo mypipe
if [ ` java -version 2>mypipe|grep -c "java version.*1\.7" mypipe` -eq 0 ]; then echo "java 7 is required to run this application"; rm mypipe; exit; fi
rm mypipe

echo "logs are in anonimecsFree.log"

nohup java -Dspring.profiles.active=free_edition -jar anonimecsFree.war > anonimecsFree.log 2>&1 &
sleep 25
echo "go to http://`hostname`:8080/anon/pages/connection/connectionAdd.jsf"
