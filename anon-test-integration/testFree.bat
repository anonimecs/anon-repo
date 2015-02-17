<<<<<<< HEAD
call setenv.bat

call mvn clean install -P free

cd anon-test-integration
call mvn package -P free

cd c:\Temp\anonimecs\anonimecsFreeWin
startFree.bat

=======
call setenv.bat

call mvn clean install -P free

cd anon-test-integration
call mvn package -P free

cd c:\Temp\anonimecs\anonimecsFreeWin
startFree.bat

>>>>>>> f433e150161bc23d087604a6480c2ef237bf6713
cd cd %ANON_HOME%