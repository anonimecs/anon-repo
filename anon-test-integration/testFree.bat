call setenv.bat

call mvn clean install -P free

cd anon-test-integration
call mvn package -P free

cd c:\Temp\anonimecs\anonimecsFreeWin
startFree.bat

cd cd %ANON_HOME%