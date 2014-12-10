@echo off

cd %~dp0

java -cp anon-enterprise-only.jar;lib/* org.anon.enterprise.runner.AnonStandaloneRunner %1