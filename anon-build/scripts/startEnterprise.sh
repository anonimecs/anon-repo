#!/bin/bash
                                                                                                                                                         
echo "logs are in anonimecs.log"
                                                                                                                                                         
CURRENT_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $CURRENT_DIR
                                                                                                                                                         
export DERBY_HOME=$CURRENT_DIR/db
export DERBY_EXECUTABLE=$DERBY_HOME/bin/startNetworkServer
export DERBY_OPTS=-Dderby.system.home=$CURRENT_DIR/db/data
export CLASSPATH=$DERBY_HOME/lib/derbytools.jar:$DERBY_HOME/lib/derbynet.jar:.
export CATALINA_HOME=$CURRENT_DIR/app
export APP_EXECUTABLE=$CATALINA_HOME/bin/startup.sh
export CATALINA_OPTS="-Xms256M -Xmx2048M -Dspring.profiles.active=enterprise_edition -Dderby.dir=$CURRENT_DIR"
export HOSTNAME=`hostname`
                                                                                                                                                         
                                                                                                                                                         
$DERBY_EXECUTABLE > anonimecs.log &
echo "waiting to start the db"
sleep 3
echo "database started"
                                                                                                                                                         
"$APP_EXECUTABLE" > anonimecs.log &
echo "waiting to start the application"
sleep 10
echo "application started"
                                                                                                                                                         
echo "go to http://$HOSTNAME:8080/anon"
                                          