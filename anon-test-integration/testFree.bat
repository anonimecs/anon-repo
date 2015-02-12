call setenv.bat

call mvn clean package -P free

cd anon-build-test
call mvn package -P free

cd c:\Temp\anonimecs\anonimecsFreeWin
startFree.bat

cd cd %ANON_HOME%