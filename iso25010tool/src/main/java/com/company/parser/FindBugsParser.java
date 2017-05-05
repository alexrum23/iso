package com.company.parser;

import com.company.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class FindBugsParser{
    private String FILE_PATH;

    public FindBugsParser(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    public boolean parse(String project) {
        StringBuilder sql = new StringBuilder();
        try{
            File file = new File(FILE_PATH + project + ".xml");
            DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList bugs = doc.getElementsByTagName("BugInstance");
            String classname="",priority="";

            Node node;
            for (int i = 0; i< bugs.getLength(); i++) {
                node = bugs.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element)node;

                    Element class_node = (Element) element.getElementsByTagName("Class").item(0);
                    classname=class_node.getAttribute("classname");

                    priority = element.getAttribute("priority").equals("1")?"bug_count":"warning_count";
                }

                sql.append("UPDATE classes SET "+priority+"="+priority+"+1 WHERE name='"+classname+"';\n");
            }
            //System.out.println(sql.toString());
            Database.getInstance().executeUpdate(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
