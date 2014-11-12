#!/bin/bash

#test the java version
mkfifo mypipe
if [ ` java -version 2>mypipe|grep -c "java version.*1\.7" mypipe` -eq 0 ]; then echo "java 7 is required to run this application"; rm mypipe; exit; fi
rm mypipe


echo "logs are in anonimecs.log"

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $SCRIPTDIR

export DERBY_HOME=$SCRIPTDIR/db/db-derby-10.4.2.0-bin
export CLASSPATH=$DERBY_HOME/lib/derbytools.jar:$DERBY_HOME/lib/derbynet.jar:.

nohup $DERBY_HOME/bin/startNetworkServer > anonimecs.log 2>&1 &
echo "waiting to start the db"
sleep 5
echo "database started"


nohup java -Dspring.profiles.active=enterprise_edition -jar anonimecsEnterprise.war > anonimecs.log 2>&1 &
echo "waiting to start the application"
sleep 10
echo "application started"

echo "go to http://`hostname`:8080/anon-gui/pages/cockpit.jsf"
