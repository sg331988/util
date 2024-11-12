# util
utility project
======================
@echo off
setlocal
 
REM Set the base directory to the current directory
set BASE_DIR=%~dp0
 
REM Set paths for Java and JAR files
set BIN_DIR=%BASE_DIR%bin
set JAVA_FILE=OpenApiGeneratorRunner.java
set JAR_FILE=openapi-generator-cli-7.8.0.jar
 
REM Move to the bin directory
cd /d %BIN_DIR%
 
REM Compile the Java program
javac -cp "%JAR_FILE%" %JAVA_FILE%
 
REM Run the Java program
java -cp ".;%JAR_FILE%" OpenApiGeneratorRunner
 
endlocal
pause
======================================================

# Comma-separated list of folders
folders=account,job,composite,admin
 
# Config and Swagger file mappings for each folder
account.config.file=accountconfig.json
account.swagger.file=ext.account.v1.account_ext-1.0.swagger.json
account.generatedto=false

==============================================================

{
  "generatorName": "java",
  "inputSpec": "ext.account.v1.account_ext-1.0.swagger.json", 
  "outputDir": "./",  
  "modelPackage": "com.guidewire.beazley.common.cloudapi.dto.account",
  "apiPackage": "com.capgemini.apitest.cloudapi.account.api",
  "invokerPackage": "com.capgemini.apitest.cloudapi.account.invoker",
 
  "library": "feign",  
  "serializableModel": true,
  "dateLibrary": "java8", 
  "useBeanValidation": false,  
  "java8": true,  
  "jsonLibrary": "jackson", 
  "generateModelTests": true, 
  "generateApiTests": true, 
  "generateApiDocumentation": true, 
  "generateModelDocumentation": true, 
  "generateApis": true,  
  "generateModels": true, 
  "generateSupportingFiles": true, 
  "httpClient": "apache"
}
