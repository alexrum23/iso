package com.company.analyzer;

import com.company.*;

import org.jsoup.Jsoup;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Analyzer {

    private String project_name;
    private int class_counter=0;
    //Functional Suitability
    private int completeness_warning=0, completeness_error=0;
    private int correctness_warning=0, correctness_error=0;
    private int appropriateness_warning=0, appropriateness_error=0;
    //Maintainability
    private int modularity_warning=0, modularity_error=0;
    private int reusability_warning=0, reusability_error=0;
    private int analyzability_warning=0, analyzability_error=0;
    private int modifiability_warning=0, modifiability_error=0;
    private int testability_warning=0, testability_error=0;

    public Analyzer(String project_name) {
        this.project_name = project_name;
        File file = new File("report/"+project_name+"/log.xml");
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void analyze(){
        ResultSet rs = readvalues(Database.getInstance().getProjectId(project_name));
        ClassMetrics classMetrics;
        try {
            while (rs.next()){
                classMetrics=new ClassMetrics(rs.getString("name"),project_name);
                classMetrics.calculate(rs.getInt("lcom5"),rs.getInt("cbo"),rs.getInt("cboi"),rs.getInt("nii"),rs.getInt("noi"),rs.getFloat("cllc"),rs.getInt("loc"),rs.getFloat("cd"),rs.getFloat("ad"),rs.getInt("wmc"),rs.getInt("tnm"),rs.getInt("nl"),rs.getInt("code_coverage_index"),rs.getInt("bug_count"),rs.getInt("warning_count"));
                //Functional Suitability
                if (classMetrics.getCompleteness()==Grade.ERROR) completeness_error++;
                else if (classMetrics.getCompleteness()==Grade.WARNING) completeness_warning++;
                if (classMetrics.getCorrectness()==Grade.ERROR) correctness_error++;
                else if (classMetrics.getCorrectness()==Grade.WARNING) correctness_warning++;
                if (classMetrics.getAppropriateness()==Grade.ERROR) appropriateness_error++;
                else if (classMetrics.getAppropriateness()==Grade.WARNING) appropriateness_warning++;
                //Maintainability
                if (classMetrics.getModularity()==Grade.ERROR) modularity_error++;
                else if (classMetrics.getModularity()==Grade.WARNING) modularity_warning++;
                if (classMetrics.getReusability()==Grade.ERROR) reusability_error++;
                else if (classMetrics.getReusability()==Grade.WARNING) reusability_warning++;
                if (classMetrics.getAnalyzability()==Grade.ERROR) analyzability_error++;
                else if (classMetrics.getAnalyzability()==Grade.WARNING) analyzability_warning++;
                if (classMetrics.getModifiability()==Grade.ERROR) modifiability_error++;
                else if (classMetrics.getModifiability()==Grade.WARNING) modifiability_warning++;
                if (classMetrics.getTestability()==Grade.ERROR) testability_error++;
                else if (classMetrics.getTestability()==Grade.WARNING) testability_warning++;

                class_counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (class_counter!=0) report_to_html();
    }

    private ResultSet readvalues(int project_id){
        String sql = "SELECT * FROM classes WHERE project_id="+project_id+";";
        return Database.getInstance().executeQuery(sql);
    }

    public void report_to_xml(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc;
            Element projects;
            try {
                doc = docBuilder.parse(project_name+"/index.html");
                projects = doc.getDocumentElement();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.println("New report.xml will be created");
                doc = docBuilder.newDocument();
                projects = doc.createElement("projects");
                doc.appendChild(projects);
            }

            Element project = doc.createElement("project");
            projects.appendChild(project);

            // set attribute to staff element

            project.setAttribute("name", project_name);

            Element classes = doc.createElement("classes");
            classes.appendChild(doc.createTextNode(Integer.toString(class_counter)));
            project.appendChild(classes);

            Element completeness_good = doc.createElement("completeness_good");
            completeness_good.appendChild(doc.createTextNode(Integer.toString(class_counter-completeness_warning-completeness_error)));
            Element completeness_warnings = doc.createElement("completeness_warnings");
            completeness_warnings.appendChild(doc.createTextNode(Integer.toString(completeness_warning)));
            Element completeness_errors = doc.createElement("completeness_errors");
            completeness_errors.appendChild(doc.createTextNode(Integer.toString(completeness_error)));

            Element funct_completeness =doc.createElement("Functional_Completeness");
            funct_completeness.appendChild(completeness_good);
            funct_completeness.appendChild(completeness_warnings);
            funct_completeness.appendChild(completeness_errors);
            project.appendChild(funct_completeness);

            Element correctness_good = doc.createElement("correctness_good");
            correctness_good.appendChild(doc.createTextNode(Integer.toString(class_counter-correctness_warning-correctness_error)));
            Element correctness_warnings = doc.createElement("correctness_warnings");
            correctness_warnings.appendChild(doc.createTextNode(Integer.toString(correctness_warning)));
            Element correctness_errors = doc.createElement("correctness_errors");
            correctness_errors.appendChild(doc.createTextNode(Integer.toString(correctness_error)));

            Element funct_correctness =doc.createElement("Functional_Correctness");
            funct_correctness.appendChild(correctness_good);
            funct_correctness.appendChild(correctness_warnings);
            funct_correctness.appendChild(correctness_errors);
            project.appendChild(funct_correctness);

            Element appropriateness_good = doc.createElement("appropriateness_good");
            appropriateness_good.appendChild(doc.createTextNode(Integer.toString(class_counter-appropriateness_warning-appropriateness_error)));
            Element appropriateness_warnings = doc.createElement("appropriateness_warnings");
            appropriateness_warnings.appendChild(doc.createTextNode(Integer.toString(appropriateness_warning)));
            Element appropriateness_errors = doc.createElement("appropriateness_errors");
            appropriateness_errors.appendChild(doc.createTextNode(Integer.toString(appropriateness_error)));

            Element funct_appropriateness =doc.createElement("Functional_Appropriateness");
            funct_appropriateness.appendChild(appropriateness_good);
            funct_appropriateness.appendChild(appropriateness_warnings);
            funct_appropriateness.appendChild(appropriateness_errors);
            project.appendChild(funct_appropriateness);

            Element modularity_good = doc.createElement("modularity_good");
            modularity_good.appendChild(doc.createTextNode(Integer.toString(class_counter-modularity_warning-modularity_error)));
            Element modularity_warnings = doc.createElement("modularity_warnings");
            modularity_warnings.appendChild(doc.createTextNode(Integer.toString(modularity_warning)));
            Element modularity_errors = doc.createElement("modularity_errors");
            modularity_errors.appendChild(doc.createTextNode(Integer.toString(modularity_error)));

            Element modularity =doc.createElement("Modularity");
            modularity.appendChild(modularity_good);
            modularity.appendChild(modularity_warnings);
            modularity.appendChild(modularity_errors);
            project.appendChild(modularity);

            Element reusability_good = doc.createElement("reusability_good");
            reusability_good.appendChild(doc.createTextNode(Integer.toString(class_counter-reusability_warning-reusability_error)));
            Element reusability_warnings = doc.createElement("reusability_warnings");
            reusability_warnings.appendChild(doc.createTextNode(Integer.toString(reusability_warning)));
            Element reusability_errors = doc.createElement("reusability_errors");
            reusability_errors.appendChild(doc.createTextNode(Integer.toString(reusability_error)));

            Element reusability =doc.createElement("Reusability");
            reusability.appendChild(reusability_good);
            reusability.appendChild(reusability_warnings);
            reusability.appendChild(reusability_errors);
            project.appendChild(reusability);

            Element analyzability_good = doc.createElement("analyzability_good");
            analyzability_good.appendChild(doc.createTextNode(Integer.toString(class_counter-analyzability_warning-analyzability_error)));
            Element analyzability_warnings = doc.createElement("analyzability_warnings");
            analyzability_warnings.appendChild(doc.createTextNode(Integer.toString(analyzability_warning)));
            Element analyzability_errors = doc.createElement("analyzability_errors");
            analyzability_errors.appendChild(doc.createTextNode(Integer.toString(analyzability_error)));

            Element analyzability =doc.createElement("Analyzability");
            analyzability.appendChild(analyzability_good);
            analyzability.appendChild(analyzability_warnings);
            analyzability.appendChild(analyzability_errors);
            project.appendChild(analyzability);

            Element modifiability_good = doc.createElement("modifiability_good");
            modifiability_good.appendChild(doc.createTextNode(Integer.toString(class_counter-modifiability_warning-modifiability_error)));
            Element modifiability_warnings = doc.createElement("modifiability_warnings");
            modifiability_warnings.appendChild(doc.createTextNode(Integer.toString(modifiability_warning)));
            Element modifiability_errors = doc.createElement("modifiability_errors");
            modifiability_errors.appendChild(doc.createTextNode(Integer.toString(modifiability_error)));

            Element modifiability =doc.createElement("Modifiability");
            modifiability.appendChild(modifiability_good);
            modifiability.appendChild(modifiability_warnings);
            modifiability.appendChild(modifiability_errors);
            project.appendChild(modifiability);

            Element testability_good = doc.createElement("testability_good");
            testability_good.appendChild(doc.createTextNode(Integer.toString(class_counter-testability_warning-testability_error)));
            Element testability_warnings = doc.createElement("testability_warnings");
            testability_warnings.appendChild(doc.createTextNode(Integer.toString(testability_warning)));
            Element testability_errors = doc.createElement("testability_errors");
            testability_errors.appendChild(doc.createTextNode(Integer.toString(testability_error)));

            Element testability =doc.createElement("Testability");
            testability.appendChild(testability_good);
            testability.appendChild(testability_warnings);
            testability.appendChild(testability_errors);
            project.appendChild(testability);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(project_name+"/index.html"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public void report_to_html() {
        File file = new File("report/index.html");
        try {
            if (!file.exists()) {
                file.createNewFile();
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>ISO 25010 Analysis</title>\n" +
                        "    <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">\n" +
                        "    <script type=\"text/javascript\">\n" +
                        "   function toggleFSDescription(proj_num,elid) {\n" +
                        "    if (document.getElementById) {\n" +
                        "     var element = document.getElementById(elid+proj_num);\n" +
                        "     var div = document.getElementById('FSblock'+proj_num);\n" +
                        "     if (element) {\n" +
                        "      div.innerHTML=element.outerHTML;\n" +
                        "      if (div.style.display == 'none') {\n" +
                        "       div.style.display='block';\n" +
                        "      }\n" +
                        "     }\n" +
                        "    }\n" +
                        "   }\n" +
                        "    function switch_hidden(id) {\n" +
                        "    var elements = document.getElementsByClassName('class '+id);\n" +
                        "     if (elements.item(0).style.display=='none'){\n" +
                        "      for (var i=0;i<elements.length;i++){\n" +
                        "       elements.item(i).style.display='table-row';\n" +
                        "      }\n" +
                        "     }else {" +
                        "       var metrics = document.getElementsByClassName(id);\n" +
                        "      for (var i=0;i<metrics.length;i++){\n" +
                        "       metrics.item(i).style.display='none';\n" +
                        "      }\n" +
                        "     }\n" +
                        "   }\n" +
                        "\n" +
                        "   function show_metrics(class_id){\n" +
                        "    var elements = document.getElementsByClassName(class_id);\n" +
                        "    for (var i=0;i<elements.length;i++){\n" +
                        "     if (elements.item(i).classList.contains('active')){\n" +
                        "      if (elements.item(i).style.display=='none') elements.item(i).style.display='table-row';\n" +
                        "      else elements.item(i).style.display='none';\n" +
                        "     }\n" +
                        "    }\n" +
                        "   }\n" +
                        "   function clear_table() {\n" +
                        "    var list = document.getElementsByClassName(\"metric\");\n" +
                        "    for (var i=0;i<list.length;i++){\n" +
                        "     list.item(i).style.display='none';\n" +
                        "     list.item(i).classList.remove(\"active\");\n" +
                        "    }\n" +
                        "   }\n" +
                        "\n" +
                        "   function set_active(project,subchar,level) {\n" +
                        "    document.getElementById('Main_block'+project).style.display='block';\n" +
                        "    clear_table();\n" +
                        "    document.getElementById('table_header_char'+project).innerHTML=subchar+' '+ level+'s';" +
                        "    var nodearray;\n" +
                        "    switch (subchar){\n" +
                        "     case 'Modularity':\n" +
                        "      nodearray=[document.getElementsByClassName(\"LCOM5\"),document.getElementsByClassName(\"CBO\"),document.getElementsByClassName(\"LOC\")];\n" +
                        "      break;\n" +
                        "     case 'Reusability':\n" +
                        "      nodearray=[document.getElementsByClassName(\"CBO\"),document.getElementsByClassName(\"NOI\"),document.getElementsByClassName(\"CD\")];\n" +
                        "      break;\n" +
                        "     case 'Analyzability':\n" +
                        "      nodearray=[document.getElementsByClassName(\"LCOM5\"),document.getElementsByClassName(\"LOC\"),document.getElementsByClassName(\"CD\"),document.getElementsByClassName(\"AD\"),document.getElementsByClassName(\"WMC\"),document.getElementsByClassName(\"CLLC\")];\n" +
                        "      break;\n" +
                        "     case 'Modifiability':\n" +
                        "      nodearray=[document.getElementsByClassName(\"CBOI\"),document.getElementsByClassName(\"NII\"),document.getElementsByClassName(\"WMC\"),document.getElementsByClassName(\"CLLC\")];\n" +
                        "      break;\n" +
                        "     case 'Testability':\n" +
                        "      nodearray=[document.getElementsByClassName(\"CBO\"),document.getElementsByClassName(\"TNM\"),document.getElementsByClassName(\"WMC\"),document.getElementsByClassName(\"NL\")];\n" +
                        "      break;\n" +
                        "    }\n" +
                        "    for (var i=0;i<nodearray.length;i++){\n" +
                        "     for (var j=0;j<nodearray[i].length;j++){\n" +
                        "      if (nodearray[i].item(j).classList.contains(level)) nodearray[i].item(j).classList.add('active');\n" +
                        "     }\n" +
                        "    }\n" +
                        "   }\n" +
                        "  </script>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "</body>\n" +
                        "</html>");
                writer.close();

            }
            org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8");
            org.jsoup.nodes.Element body = doc.select("body").first();

            int id;

            if (body.select("div").last() == null) {
                id = 1;
            } else {
                id = Integer.parseInt(body.getElementsByClass("block").last().attr("id").replace("project", "")) + 1;
            }
            body.append("<div id='project" + id + "' class='block' align=left>");
            org.jsoup.nodes.Element div = doc.select("div").last();

            div.append("<div style=\"border-style: outset\"><h2>Project - " + project_name + "</h2></div><br>");
            div.append("<h3>Total number of classes - " + class_counter + "</h3>");
            div.append("<h3><b>Functional Suitability</b></h3>");
            div.append("<table class=\"table table-striped table-bordered\" style=\"width:100%\">");

            org.jsoup.nodes.Element table = div.select("table").last();
            table.append("<tr>\n<th></th>\n<th>Functional Completeness</th>\n<th>Correctness</th>\n<th>Appropriateness</th>\n</tr>");
            int completeness = class_counter - completeness_error - completeness_warning;
            int correctness = class_counter - correctness_error - correctness_warning;
            int appropriateness = class_counter - appropriateness_error - appropriateness_warning;
            int compl_percent = 100 - (Math.round(completeness_warning * 100 / class_counter) + Math.round(completeness_error * 100 / class_counter));
            int corr_percent = 100 - (Math.round(correctness_warning * 100 / class_counter) + Math.round(correctness_error * 100 / class_counter));
            int appr_percent = 100 - (Math.round(appropriateness_warning * 100 / class_counter) + Math.round(appropriateness_error * 100 / class_counter));

            table.append("   <tr>\n" +
                    "        <th class='success'>Good</th>\n" +
                    "        <td>" + completeness + " (" + compl_percent + "%)</td>\n" +
                    "        <td>" + correctness + " (" + corr_percent + "%)</td>\n" +
                    "        <td>" + appropriateness + " (" + appr_percent + "%)</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class='warning'>Warning</th>\n" +
                    "        <td onClick=toggleFSDescription(" + id + ",'eclemma')>" + completeness_warning + " (" + Math.round(completeness_warning * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=toggleFSDescription(" + id + ",'findbugs')>" + correctness_warning + " (" + Math.round(correctness_warning * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=toggleFSDescription(" + id + ",'findbugs')>" + appropriateness_warning + " (" + Math.round(appropriateness_warning * 100 / class_counter) + "%)</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class='danger'>Critical</th>\n" +
                    "        <td onClick=toggleFSDescription(" + id + ",'eclemma')>" + completeness_error + " (" + Math.round(completeness_error * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=toggleFSDescription(" + id + ",'findbugs')>" + correctness_error + " (" + Math.round(correctness_error * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=toggleFSDescription(" + id + ",'findbugs')>" + appropriateness_error + " (" + Math.round(appropriateness_error * 100 / class_counter) + "%)</td>\n" +
                    "    </tr>");

            div.append("<div id='FSblock" + id + "' style='display: none' onclick=\"this.style.display='none'\">");

            div.append("<h3><b>Maintainability</b></h3>");
            div.append("<table class=\"table table-striped table-bordered\" style=\"width:100%\">");
            table = div.select("table").last();
            table.append("<tr>\n" +
                    "        <th></th>\n" +
                    "        <th>Modularity</th>\n" +
                    "        <th>Reusability</th>\n" +
                    "        <th>Analyzability</th>\n" +
                    "        <th>Modifiability</th>\n" +
                    "        <th>Testability</th>\n" +
                    "    </tr>");

            int modularity = class_counter - modularity_error - modularity_warning;
            int reusability = class_counter - reusability_error - reusability_warning;
            int analyzability = class_counter - analyzability_error - analyzability_warning;
            int modifiability = class_counter - modifiability_error - modifiability_warning;
            int testability = class_counter - testability_error - testability_warning;
            int mod_percent = 100 - (Math.round(modularity_warning * 100 / class_counter) + Math.round(modularity_error * 100 / class_counter));
            int reuse_percent = 100 - (Math.round(reusability_warning * 100 / class_counter) + Math.round(reusability_error * 100 / class_counter));
            int analyze_percent = 100 - (Math.round(analyzability_warning * 100 / class_counter) + Math.round(analyzability_error * 100 / class_counter));
            int modify_percent = 100 - (Math.round(modifiability_warning * 100 / class_counter) + Math.round(modifiability_error * 100 / class_counter));
            int test_percent = 100 - (Math.round(testability_warning * 100 / class_counter) + Math.round(testability_error * 100 / class_counter));

            table.append("   <tr>\n" +
                    "        <th class='success'>Good</th>\n" +
                    "        <td id='p" + id + "_modularity_G'>" + modularity + " (" + mod_percent + "%)</td>\n" +
                    "        <td id='p" + id + "_reusability_G'>" + reusability + " (" + reuse_percent + "%)</td>\n" +
                    "        <td id='p" + id + "_analyzability_G'>" + analyzability + " (" + analyze_percent + "%)</td>\n" +
                    "        <td id='p" + id + "_modifiability_G'>" + modifiability + " (" + modify_percent + "%)</td>\n" +
                    "        <td id='p" + id + "_testability_G'>" + testability + " (" + test_percent + "%)</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class='warning'>Warning</th>\n" +
                    "        <td onClick=set_active("+id+",'Modularity','warning')>" + modularity_warning + " (" + Math.round(modularity_warning * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Reusability','warning')>" + reusability_warning + " (" + Math.round(reusability_warning * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Analyzability','warning')>" + analyzability_warning + " (" + Math.round(analyzability_warning * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Modifiability','warning')>" + modifiability_warning + " (" + Math.round(modifiability_warning * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Testability','warning')>" + testability_warning + " (" + Math.round(testability_warning * 100 / class_counter) + "%)</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th class='danger'>Critical</th>\n" +
                    "        <td onClick=set_active("+id+",'Modularity','error')>" + modularity_error + " (" + Math.round(modularity_error * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Reusability','error')>" + reusability_error + " (" + Math.round(reusability_error * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Analyzability','error')>" + analyzability_error + " (" + Math.round(analyzability_error * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Modifiability','error')>" + modifiability_error + " (" + Math.round(modifiability_error * 100 / class_counter) + "%)</td>\n" +
                    "        <td onClick=set_active("+id+",'Testability','error')>" + testability_error + " (" + Math.round(testability_error * 100 / class_counter) + "%)</td>\n" +
                    "    </tr>");
            div.append("<div id='Main_block" + id + "' style='display: none'>");
            div = div.select("div").last();
            div.append("<button class='btn btn-primary pull-right' style='padding-right: 5%' onclick=\"Main_block"+id+".style.display='none'\">Close</p>");
            div.append(fill_table(id));
            div.append("<div style='display: none'>");
            div = div.select("div").last();

            div.append("<p id='findbugs"+id+"'>For detailed bug description go to <a href='" + project_name + "/FindBugs/" + project_name + ".html'>FindBugs report</a>.</p>");
            div.append("<p id='eclemma"+id+"'>For detailed code coverage description go to <a href='" + project_name + "/EclEmma/index.html'>Coverage report</a>.</p>");

            body.append("<br>");
            PrintWriter printWriter = new PrintWriter(file, "UTF-8");
            printWriter.println(doc.outerHtml());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fill_table(int id){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            File file = new File("report/" + project_name + "/log.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            stringBuilder.append("<table class=\"table table-bordered table-hover table-striped\" width=\"100%\">\n" +
                    "     <colgroup>\n" +
                    "      <col span=\"1\" style=\"width: 40%;\">\n" +
                    "      <col span=\"1\" style=\"width: 60%;\">\n" +
                    "     </colgroup>\n" +
                    "     <thead>\n" +
                    "      <tr>\n" +
                    "       <th align=\"left\">Package</th>\n" +
                    "       <th id=\"table_header_char"+id+"\" align=\"center\">Modularity Warnings</th>\n" +
                    "      </tr>\n" +
                    "     </thead>" +
                    "     <tbody>");

            NodeList packagelist = doc.getElementsByTagName("Package");
            NodeList classlist,metrics;
            Element current;

            for (int i=0;i< packagelist.getLength();i++){
                stringBuilder.append("<tr onclick=\"switch_hidden('pack"+id+"_"+i+"')\"><td>");
                current=(Element) packagelist.item(i);
                stringBuilder.append(current.getAttribute("id"));
                stringBuilder.append("</td><td></td></tr>");
                classlist=packagelist.item(i).getChildNodes();
                for (int j=0;j<classlist.getLength();j++){
                    stringBuilder.append("<tr class='class pack"+id+"_"+i+"' style=\"display: none\" onclick=\"show_metrics('class"+id+"_"+i+"_"+j+"')\"><td style=\"padding-left: 30px\">");
                    stringBuilder.append("&#10157;");
                    current=(Element) classlist.item(j);
                    stringBuilder.append(current.getAttribute("id"));
                    stringBuilder.append("</td><td></td></tr>");
                    metrics=classlist.item(j).getChildNodes().item(0).getChildNodes();//warnings
                    for (int k=0;k<metrics.getLength();k++){
                        current=(Element) metrics.item(k);
                        stringBuilder.append("<tr class='metric pack"+id+"_"+i+" class"+id+"_"+i+"_"+j+" warning "+current.getAttribute("id")+"' style=\"display: none\"><td bgcolor=\"#ffd700\"></td><td>");
                        stringBuilder.append(metrics.item(k).getTextContent());
                        stringBuilder.append("</td></tr>");
                    }
                    metrics=classlist.item(j).getChildNodes().item(1).getChildNodes();//errors
                    for (int k=0;k<metrics.getLength();k++){
                        current=(Element) metrics.item(k);
                        stringBuilder.append("<tr class='metric pack"+id+"_"+i+" class"+id+"_"+i+"_"+j+" error danger "+current.getAttribute("id")+"' style=\"display: none\"><td bgcolor=\"#e84444\"></td><td>");
                        stringBuilder.append(metrics.item(k).getTextContent());
                        stringBuilder.append("</td></tr>");
                    }
                }
            }
            stringBuilder.append("</tbody></table>");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
