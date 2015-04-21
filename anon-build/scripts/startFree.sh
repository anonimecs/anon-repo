#!/bin/sh

#test the java version
mkfifo mypipe
if [ ` java -version 2>mypipe|grep -c "java version.*1\.7" mypipe` -eq 0 ]; then echo "java 7 is required to run this application"; rm mypipe; exit; fi
rm mypipe


echo "logs are in anonimecs.log"

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $CURRENT_DIR

export CATALINA_HOME=%CURRENT_DIR%/app
export APP_EXECUTABLE=%CATALINA_HOME%/bin/startup.sh
export CATALINA_OPTS=-Xms256M -Xmx1024M -Dspring.profiles.active=free_edition -Dderby.dir=%CURRENT_DIR%

$APP_EXECUTABLE > anonimecs.log 2>&1 &
echo "waiting to start the application"
sleep 25
echo "application started"

echo "go to http://`hostname`:8080/anon"
