call setenv.bat

call mvn clean install -P enterprise

cd anon-test-integration
call mvn package -P enterprise

cd c:\Temp\anonimecs\anonimecs
startEnterprise.bat

cd %ANON_HOME%