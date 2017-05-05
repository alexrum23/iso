package com.company.parser;

import com.company.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SourceMeterParser {
    private String FILE_PATH;

    public SourceMeterParser(String path) {
        FILE_PATH=path;
    }

    public boolean parse(String project){

        int project_id = Database.getInstance().getProjectId(project);
        //File file = new File(FILE_PATH);
        File[] files = new File(FILE_PATH).listFiles();
        for (File f: files) {
            if (f.isDirectory()) {
                String line = "";
                String csvSplitBy = "\";\"";
                ArrayList<String> indeces = null;
                try (BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()+"\\"+project+"-Class.csv"))) {
                    if ((line = br.readLine()) == null) {
                        System.out.println("File is empty");
                        return false;
                    } else {
                        indeces = new ArrayList<>(Arrays.asList(line.split(csvSplitBy)));
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("INSERT INTO classes(name,project_id,lcom5,cbo,cboi,nii,noi,cllc,loc,cd,ad,wmc,tnm,nl) VALUES");
                    while ((line = br.readLine()) != null) {
                        String[] metrics = line.split(csvSplitBy);
                        stringBuilder.append("('" + metrics[indeces.indexOf("LongName")] + "', " + project_id + ", " + metrics[indeces.indexOf("LCOM5")] + ", " + metrics[indeces.indexOf("CBO")] +
                                ", " + metrics[indeces.indexOf("CBOI")] + ", " + metrics[indeces.indexOf("NII")] + ", " + metrics[indeces.indexOf("NOI")] +
                                ", " + metrics[indeces.indexOf("CLLC")] + ", " + metrics[indeces.indexOf("LOC")] + ", " + metrics[indeces.indexOf("CD")] +
                                ", " + metrics[indeces.indexOf("AD")] + ", " + metrics[indeces.indexOf("WMC")] + ", " + metrics[indeces.indexOf("TNM")] +
                                ", " + metrics[indeces.indexOf("NL")] +
                                "),");
                    }
                    if (stringBuilder.charAt(stringBuilder.length()-1)==',') {
                        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), ";");
                        Database.getInstance().executeUpdate(stringBuilder.toString());
                    }
                    stringBuilder=null;
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        return true;
    }


}
