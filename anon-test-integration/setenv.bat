set ANON_HOME=c:\dev_anon
set JAVA_HOME=%ANON_HOME%\jdk17
set M2_HOME=%ANON_HOME%\maven
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%

rd /S /Q c:\Temp\anonimecs
mkdir c:\Temp\anonimecs

cd %ANON_HOME%\anon-repo