export DEV_ANON=/Users/csaba/dev_anon

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_55.jdk/Contents/Home
export M2_HOME=$DEV_ANON/maven
export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
export DERBY_HOME=$DEV_ANON/db-derby-10.4.2.0-bin
export CLASSPATH=$DERBY_HOME/lib/derbytools.jar:$DERBY_HOME/lib/derbynet.jar:.

cd $DEV_ANON/projects

$DERBY_HOME/bin/startNetworkServer
