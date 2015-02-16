call setenv.bat

call mvn clean package -P enterprise

cd anon-build-test
call mvn package -P enterprise

cd c:\Temp\anonimecs\anonimecs
startEnterprise.bat

cd %ANON_HOME%