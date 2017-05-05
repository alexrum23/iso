#!/bin/bash

project_name="agilefant"

./..\SourceMeter-8.1.0-x64-linux\Java\SourceMeterJava -projectName=$project_name -projectBaseDir=..\..\Projects\+$project_name+\ -resultsDir=..\Results -runFB=false -runPMD=false -runFaultHunter=false -runAndroidHunter=false -runVulnerabilityHunter=false -csvSeparator=";"

./..\findbugs-3.0.1\bin\findbugs -textui -maxRank 14 -progress -xml -output ..\Results\$project_name\FindBugs\$project_name.xml ..\..\Projects\$project_name\