package com.company;

import com.company.analyzer.*;
import com.company.parser.*;

public class Main{

    public static void main(String[] args){    	
    
    	Parser parser = new Parser(args[0], args[1]);
        parser.parse();
        Analyzer analyzer = new Analyzer(args[1]);
        analyzer.analyze();

        parser = new Parser(args[0], args[2]);
        parser.parse();
        analyzer = new Analyzer(args[2]);
        analyzer.analyze();

        parser = new Parser(args[0], args[3]);
        parser.parse();
        analyzer = new Analyzer(args[3]);
        analyzer.analyze();
    }
}
