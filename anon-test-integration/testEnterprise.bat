<<<<<<< HEAD
call setenv.bat

call mvn clean install -P enterprise

cd anon-test-integration
call mvn package -P enterprise

cd c:\Temp\anonimecs\anonimecs
startEnterprise.bat

=======
call setenv.bat

call mvn clean install -P enterprise

cd anon-test-integration
call mvn package -P enterprise

cd c:\Temp\anonimecs\anonimecs
startEnterprise.bat

>>>>>>> f433e150161bc23d087604a6480c2ef237bf6713
cd %ANON_HOME%