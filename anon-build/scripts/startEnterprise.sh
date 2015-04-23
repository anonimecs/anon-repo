#!/bin/bash
#
# Anonimecs       start script
#
#                                                                                                                           
#JAVA_HOME=
DB_PORT=1527
                                                                                                                                          
# DO NOT CHANGE BELOW                                                                                                                                                      
APPSERVER_LOGIN="http://`hostname`:8080/anon"
                                                                                                                                               
# CHECK FOR JAVA_HOME OR JRE_HOME                                                                                                                                                      
darwin=false
case "`uname`" in
Darwin*) darwin=true;;
esac
                                                                                                                                                            
if [ -z "$JAVA_HOME" ]; then
        if $darwin; then
                if [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
                JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Home"
        fi
        else
                JAVA_PATH=`which java 2>/dev/null`
                if [ "x$JAVA_PATH" != "x" ]; then
                        JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
                fi
        fi
        if [ -z "$JAVA_HOME" ]; then
                echo "The JAVA_HOME environment variable is not defined"
                exit 1
        fi
fi
if [ -z "$_RUNJAVA" ]; then
  _RUNJAVA="$JAVA_HOME"/bin/java
fi
if [ ! -x "$_RUNJAVA" ] ; then
  echo "Error: JAVA is not defined correctly."
  echo "Cannot execute $_RUNJAVA"
  exit 1
fi

# SET APP FOLDERS                                                                                                                                                        
CURRENT_DIR=$(pwd)
SCRIPT_DIR=$(dirname $0)
cd $SCRIPT_DIR
INSTALL_DIR="`pwd`"
                                                                                                                                                            
echo "INSTALL DIR: $INSTALL_DIR";
echo "USING JAVA: $_RUNJAVA";
                                                                                                                                                            
# START DERBY SERVER
LOGFILE=$INSTALL_DIR/anonimecs.log
DERBY_HOME=$INSTALL_DIR/db
DERBY_LIB=$DERBY_HOME/lib
DERBY_OPTS=-Dderby.system.home=$DERBY_HOME/data
LOCALCLASSPATH=$DERBY_LIB/derby.jar:$DERBY_LIB/derbynet.jar:$DERBY_LIB/derbytools.jar:$DERBY_LIB/derbyclient.jar
                                                                                                                                                            
echo "DERBY HOME: $DERBY_HOME";
#echo "DERBY LIB: $DERBY_LIB";
#echo "DERBY CLASSPATH: $LOCALCLASSPATH";
                                                                                                                                                            
nohup $_RUNJAVA $DERBY_OPTS -classpath $LOCALCLASSPATH org.apache.derby.drda.NetworkServerControl start -p $DB_PORT $@ >> $LOGFILE 2>&1&
sleep 3
DERBY_CHK="nohup $_RUNJAVA -classpath $LOCALCLASSPATH org.apache.derby.drda.NetworkServerControl ping -p $DB_PORT >> $LOGFILE 2>&1&"
eval $DERBY_CHK
result=$?
if [ $result -ne 0 ]; then
        echo "Database startup failed. For more information look at anonimecs.log"
        exit 1
fi
                                                                                                                                                            
echo "Database started.";

# START TOMCAT SERVER
export CATALINA_HOME=$INSTALL_DIR/app
export CATALINA_BASE=$CATALINA_HOME
export CATALINA_EXEC=$CATALINA_HOME/bin/startup.sh
export CATALINA_OPTS="-Xms256M -Xmx2048M -Dspring.profiles.active=enterprise_edition -Dderby.dir=$INSTALL_DIR"
                                                                                                                                                            
nohup $CATALINA_EXEC

echo Anonimecs FREE edition was started.
echo The tomcat logs are in app/logs/catalina.out
echo You can access the application at http://`hostname`:8080/anon

