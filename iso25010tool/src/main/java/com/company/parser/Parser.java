package com.company.parser;

public class Parser {

    private String project;
    private SourceMeterParser sourceMeterParser;
    private FindBugsParser findBugsParser;
    private EclEmmaParser eclEmmaParser;

    public Parser(String path, String project_name) {
        project=project_name;
        String project_path= path +"\\"+ project;
        sourceMeterParser=new SourceMeterParser(project_path+"\\java\\");
        findBugsParser = new FindBugsParser(project_path+"\\FindBugs\\");
        eclEmmaParser = new EclEmmaParser(project_path+"\\EclEmma\\");
    }

    public void parse(){
        if (sourceMeterParser!=null)sourceMeterParser.parse(project);
        findBugsParser.parse(project);
        eclEmmaParser.parse(project);
    }
}
