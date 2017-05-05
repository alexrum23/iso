@echo off

SET project_name=projectforge

REM SourceMeter analysis
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-application -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-business -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-common -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-excel -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-integration-test -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-jax-rs -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-launcher -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-model -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\%project_name%-wicket -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\plugins -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
 ..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\tools -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"

REM FindBugs analysis
REM ..\findbugs-3.0.1\bin\findbugs.bat -textui -maxRank 14 -progress -html -output ..\..\Analyzer\report\%project_name%\FindBugs\%project_name%.html ..\..\Projects\%project_name%-master\
REM PAUSE
..\findbugs-3.0.1\bin\findbugs.bat -textui -maxRank 14 -progress -xml -output ..\Results\%project_name%\FindBugs\%project_name%.xml ..\..\Projects\%project_name%-master\
REM PAUSE



