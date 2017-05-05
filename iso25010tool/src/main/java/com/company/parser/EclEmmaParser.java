package com.company.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.company.*;

public class EclEmmaParser {
    private String FILE_PATH;

    public EclEmmaParser(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    public void parse(String project){
        StringBuilder sql = new StringBuilder();
        String line = "",classname="";
        String csvSplitBy = ",";
        int code_coverage_index=0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH + project + ".csv"))){
            if (br.readLine() == null) {
                System.out.println(FILE_PATH + project + ".csv is empty");
                return;
            }

            while ((line = br.readLine()) != null) {
                String[] metrics = line.split(csvSplitBy);
                classname=metrics[1]+"."+metrics[2];
                code_coverage_index=count_index(metrics[3],metrics[4]);
                sql.append("UPDATE classes SET code_coverage_index="+code_coverage_index+" WHERE name='"+classname+"';\n");
            }
            //System.out.println(sql.toString());
            Database.getInstance().executeUpdate(sql.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private int count_index(String missed, String covered){
        int miss=Integer.parseInt(missed);
        int cover=Integer.parseInt(covered);
        return Math.round(cover*100/(miss+cover));
    }
}
