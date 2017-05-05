@echo off

SET project_name=agilefant

REM SourceMeter analysis
REM ..\SourceMeter-8.1.0-x64-windows\Java\SourceMeterJava.exe -projectName=%project_name% -projectBaseDir=..\..\Projects\%project_name%-master\ -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"
REM PAUSE

REM FindBugs analysis
..\findbugs-3.0.1\bin\findbugs.bat -textui -maxRank 14 -progress -html -output ..\..\Analyzer\report\%project_name%\FindBugs\%project_name%.html ..\..\Projects\%project_name%-master\
PAUSE
REM ..\findbugs-3.0.1\bin\findbugs.bat -textui -maxRank 14 -progress -xml -output ..\Results\%project_name%\FindBugs\%project_name%.xml ..\..\Projects\%project_name%-master\
REM PAUSE



