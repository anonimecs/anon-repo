#!/bin/sh

#test the java version
mkfifo mypipe
if [ ` java -version 2>mypipe|grep -c "java version.*1\.7" mypipe` -eq 0 ]; then echo "java 7 is required to run this application"; rm mypipe; exit; fi
rm mypipe


echo "logs are in anonimecs.log"

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $CURRENT_DIR

export DERBY_HOME=$CURRENT_DIR/db
export DERBY_EXECUTABLE=%DERBY_INSTALL%/bin/startNetworkServer
export DERBY_OPTS=-Dderby.system.home=%CURRENT_DIR%/db/data"
export CLASSPATH=$DERBY_HOME/lib/derbytools.jar:$DERBY_HOME/lib/derbynet.jar:.
export CATALINA_HOME=%CURRENT_DIR%/app
export APP_EXECUTABLE=%CATALINA_HOME%/bin/startup.sh
export CATALINA_OPTS=-Xms1024M -Xmx2048M -Dspring.profiles.active=enterprise_edition -Dderby.dir=%CURRENT_DIR%

nohup $DERBY_EXECUTABLE > anonimecs.log 2>&1 &
echo "waiting to start the db"
sleep 3
echo "database started"

$APP_EXECUTABLE > anonimecs.log 2>&1 &
echo "waiting to start the application"
sleep 25
echo "application started"

echo "go to http://`hostname`:8080/anon"
