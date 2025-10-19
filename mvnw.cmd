<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Copyright 2022-2024 the original author or authors.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script for Windows
@REM
@REM Optional ENV vars
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@ECHO OFF

SETLOCAL

SET "MAVEN_PROJECT_DIR=%~dp0"

SET "MVNW_VERBOSE=false"
IF "%1" == "-v" SET "MVNW_VERBOSE=true"
IF "%1" == "--verbose" SET "MVNW_VERBOSE=true"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECT_DIR%/.mvn/wrapper/maven-wrapper.properties") DO (
    IF "%%A"=="wrapperUrl" SET "MVNW_WRAPPER_URL=%%B"
)

IF NOT DEFINED MVNW_WRAPPER_URL (
  ECHO Could not locate the Maven Wrapper properties file at '%MAVEN_PROJECT_DIR%/.mvn/wrapper/maven-wrapper.properties'.
  GOTO :eof_error
)

SET "MVNW_WRAPPER_JAR_BASE_NAME=%MVNW_WRAPPER_URL:/=%"
SET "MVNW_WRAPPER_JAR_BASE_NAME=%MVNW_WRAPPER_JAR_BASE_NAME:*.jar=%"
SET "MVNW_WRAPPER_JAR_BASE_NAME=%MVNW_WRAPPER_JAR_BASE_NAME:*-=%"
SET "MVNW_WRAPPER_JAR_BASE_NAME=maven-wrapper"

SET "MVNW_WRAPPER_JAR_DIR=%MAVEN_PROJECT_DIR%/.mvn/wrapper"
SET "MVNW_WRAPPER_JAR=%MVNW_WRAPPER_JAR_DIR%/%MVNW_WRAPPER_JAR_BASE_NAME%.jar"
SET "MVNW_DOWNLOAD_URL=%MVNW_WRAPPER_URL%"

IF "%MVNW_VERBOSE%" == "true" (
  ECHO MVNW_WRAPPER_URL: %MVNW_WRAPPER_URL%
  ECHO MVNW_WRAPPER_JAR: %MVNW_WRAPPER_JAR%
  ECHO MVNW_DOWNLOAD_URL: %MVNW_DOWNLOAD_URL%
)

IF NOT EXIST "%MVNW_WRAPPER_JAR%" (
    ECHO Downloading %MVNW_DOWNLOAD_URL%
    powershell.exe -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webClient = New-Object System.Net.WebClient; $webClient.DownloadFile('%MVNW_DOWNLOAD_URL%', '%MVNW_WRAPPER_JAR%') }"
    IF ERRORLEVEL 1 (
        ECHO Failed to download %MVNW_DOWNLOAD_URL%
        GOTO :eof_error
    )
)

SET "MAVEN_CMD_LINE_ARGS=%*"

@REM The following complex command consumes the command line arguments and determines the location of the Java home
@REM directory. The algorithm is as follows:
@REM
@REM 1. Scan the command line arguments for the --java-home argument. The value of this argument is returned.
@REM 2. If the --java-home argument is not found, scan the command line arguments for the --java-version argument.
@REM    The value of this argument is used to look up a toolchain. If a toolchain is found, its path is returned.
@REM 3. If neither of the above arguments are found, the value of the JAVA_HOME environment variable is returned.
@REM 4. If the JAVA_HOME environment variable is not set, the java executable is resolved from the PATH. The parent
@REM    of the parent of the executable is returned.
@REM
@REM The location is ultimately stored in the JAVA_HOME environment variable.

FOR /f "tokens=* usebackq" %%a IN (`"%JAVA_HOME%/bin/java.exe" -Xmx32m -Dfile.encoding=UTF-8 -cp "%MVNW_WRAPPER_JAR%" "org.springframework.boot.maven.wrapper.MavenWrapperExecutor" %MAVEN_CMD_LINE_ARGS%`) DO (
  SET "MAVEN_JAVA_EXE=%%a"
)

IF NOT DEFINED MAVEN_JAVA_EXE (
  FOR /f "tokens=* usebackq" %%a IN (`"%JAVA_HOME%/bin/java" -Xmx32m -Dfile.encoding=UTF-8 -cp "%MVNW_WRAPPER_JAR%" "org.springframework.boot.maven.wrapper.MavenWrapperExecutor" %MAVEN_CMD_LINE_ARGS%`) DO (
    SET "MAVEN_JAVA_EXE=%%a"
  )
)

IF NOT DEFINED MAVEN_JAVA_EXE (
  FOR /f "tokens=* usebackq" %%a IN (`java -Xmx32m -Dfile.encoding=UTF-8 -cp "%MVNW_WRAPPER_JAR%" "org.springframework.boot.maven.wrapper.MavenWrapperExecutor" %MAVEN_CMD_LINE_ARGS%`) DO (
    SET "MAVEN_JAVA_EXE=%%a"
  )
)

IF NOT DEFINED MAVEN_JAVA_EXE (
  ECHO The JAVA_HOME environment variable is not defined correctly.
  ECHO It must point to a JDK installation.
  GOTO :eof_error
)

"%MAVEN_JAVA_EXE%" %MAVEN_OPTS% -cp "%MVNW_WRAPPER_JAR%" "org.apache.maven.wrapper.MavenWrapper" %MAVEN_CMD_LINE_ARGS%

SET "MVNW_ERRORLEVEL=%ERRORLEVEL%"
GOTO :eof

:eof_error
SET "MVNW_ERRORLEVEL=1"

:eof
EXIT /B %MVNW_ERRORLEVEL%
@GOTO :EOF
