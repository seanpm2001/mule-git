@echo off
REM There is no need to call this if you set the MULE_HOME in your environment properties
SET MULE_HOME=..\..\..

REM Set your application specific classpath like this
SET CLASSPATH=%MULE_HOME%\samples\voipservice\conf;%MULE_HOME%\samples\voipservice\classes;
REM set the LoanConsumer class as the main class to run
SET MULE_MAIN=org.mule.samples.voipservice.client.VoipConsumer

call %MULE_HOME%\bin\mule.bat

SET MULE_MAIN=
SET CLASSPATH=