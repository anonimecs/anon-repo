#!/bin/bash
#
# Anonimecs start script
#
#JAVA_HOME=
DB_PORT=1527

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

# START DERBY SERVER
LOGFILE=$INSTALL_DIR/anonimecs.log
DERBY_HOME=$INSTALL_DIR/db
DERBY_LIB=$DERBY_HOME/lib
DERBY_OPTS=-Dderby.system.home=$DERBY_HOME/data
LOCALCLASSPATH=$DERBY_LIB/derby.jar:$DERBY_LIB/derbynet.jar:$DERBY_LIB/derbytools.jar:$DERBY_LIB/derbyclient.jar

echo "DERBY HOME: $DERBY_HOME";

nohup $_RUNJAVA $DERBY_OPTS -classpath $LOCALCLASSPATH org.apache.derby.drda.NetworkServerControl stop -p $DB_PORT $@ >> "$LOGFILE" 2>&1&
echo "Database stoped.";

# STOP TOMCAT SERVER
export CATALINA_HOME=$INSTALL_DIR/app
export CATALINA_EXEC=$CATALINA_HOME/bin/shutdown.sh

nohup $CATALINA_EXEC
exit 0