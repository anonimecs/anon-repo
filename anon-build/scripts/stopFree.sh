#!/bin/bash
#
# Anonimecs       stop script
#
#                                                                                                                           
#JAVA_HOME=
                                                                                                                   
# DO NOT CHANGE BELOW                                                                                                                                                      
                                                                                                                                               
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
                                                                                                                                                            
LOGFILE=$INSTALL_DIR/anonimecs.log

# STOP TOMCAT SERVER
export CATALINA_HOME=$INSTALL_DIR/app
export CATALINA_EXEC=$CATALINA_HOME/bin/shutdown.sh
                                                                                                                                                            
nohup $CATALINA_EXEC
