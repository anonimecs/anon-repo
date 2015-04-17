#!/bin/sh

#test the java version
mkfifo mypipe
if [ ` java -version 2>mypipe|grep -c "java version.*1\.7" mypipe` -eq 0 ]; then echo "java 7 is required to run this application"; rm mypipe; exit; fi
rm mypipe


echo "logs are in anonimecs.log"

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $CURRENT_DIR

export DERBY_HOME=$CURRENT_DIR/db
export DERBY_EXECUTABLE=%DERBY_INSTALL%/bin/stopNetworkServer
export CLASSPATH=$DERBY_HOME/lib/derbytools.jar:$DERBY_HOME/lib/derbynet.jar:.
export CATALINA_HOME=%CURRENT_DIR%/app
export APP_EXECUTABLE=%CATALINA_HOME%/bin/shutdown.sh

$APP_EXECUTABLE > anonimecs.log 2>&1 &
echo "waiting to stop the application"
sleep 5
echo "application stopped"

$DERBY_EXECUTABLE > anonimecs.log 2>&1 &
echo "waiting to stop the db"
sleep 5
echo "database stopped"

