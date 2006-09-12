@echo off
REM There is no need to call this if you set the MULE_HOME in your environment properties
if "%MULE_HOME%" == "" SET MULE_HOME=..\..\..

REM Set your application specific classpath like this
SET MULE_LIB=%MULE_HOME%\examples\hello\classes;%MULE_HOME%\examples\hello\conf

call %MULE_HOME%\bin\mule.bat -config ../conf/hello-spring-config.xml -builder spring
