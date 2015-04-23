call setenv.bat

call mvn clean install -P release

cd anon-test-integration
call mvn package -P free

cd c:\Temp\anonimecs\anonimecs
startFree.bat

cd cd %ANON_HOME%