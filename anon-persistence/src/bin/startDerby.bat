set JAVA_HOME=c:\dev_anon\jdk17
set M2_HOME=c:\dev_anon\maven
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%
set DERBY_INSTALL=c:\dev_anon\db-derby-10.4.2.0-bin\
set CLASSPATH=%DERBY_INSTALL%\lib\derbytools.jar;%DERBY_INSTALL%\lib\derbynet.jar;.

cd c:\dev_anon\projects

%DERBY_INSTALL%\bin\startNetworkServer.bat
