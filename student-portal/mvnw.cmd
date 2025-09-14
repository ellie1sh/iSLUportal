@ECHO OFF
SETLOCAL
SET BASE_DIR=%~dp0
SET MVNW_JAR=%BASE_DIR%\.mvn\wrapper\maven-wrapper.jar
SET MVNW_PROPERTIES=%BASE_DIR%\.mvn\wrapper\maven-wrapper.properties

FOR /F "tokens=2 delims==" %%A IN ('findstr /R "^wrapperUrl=" "%MVNW_PROPERTIES%"') DO SET WRAPPER_URL=%%A

IF NOT EXIST "%MVNW_JAR%" (
  ECHO Downloading Maven Wrapper jar from: %WRAPPER_URL%
  powershell -Command "(New-Object Net.WebClient).DownloadFile('%WRAPPER_URL%', '%MVNW_JAR%')"
)

SET JAVA_EXE=java
IF DEFINED JAVA_HOME SET JAVA_EXE=%JAVA_HOME%\bin\java.exe

"%JAVA_EXE%" -cp "%MVNW_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*

