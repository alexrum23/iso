@echo off

SET project_name=miracle-framework

REM SourceMeter analysis
..\SourceMeter-8.1.0-x64-windows\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\ -resultsDir=..\Results -runFB=false -runFaultHunter=false -runPMD=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
PAUSE

REM FindBugs analysis
..\findbugs-3.0.1\bin\findbugs.bat -textui -maxRank 14 -progress -html -output ..\..\Analyzer\report\%project_name%\FindBugs\%project_name%.html ..\..\Projects\%project_name%-master\
PAUSE
..\findbugs-3.0.1\bin\findbugs.bat -textui -maxRank 14 -progress -xml -output ..\Results\%project_name%\FindBugs\%project_name%.xml ..\..\Projects\%project_name%-master\
PAUSE



