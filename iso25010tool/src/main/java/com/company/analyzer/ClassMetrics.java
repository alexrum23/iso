package com.company.analyzer;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;

public class ClassMetrics {
    /**
    * For further description follow the SourceMeter manual
    */

    private String name;
    private String package_name;
    private String project;

    /* TODO put threshold into a file*/
    private final int LCOM5_WARNING=2; //Lack of Cohesion in Methods
    private final int LCOM5_ERROR=5;
    private final int CBO_WARNING=3; //Coupling Between Object classes
    private final int CBO_ERROR=6;
    private int CBOI_WARNING=15; //Coupling Between Object classes Inverse
    private int CBOI_ERROR=30;
    private int NII_WARNING=20; //Number of Incoming Invocations
    private int NII_ERROR=30;
    private int NOI_WARNING=30; //Number of Outgoing Invocations
    private int NOI_ERROR=50;
    private float CLLC_WARNING=0.1f; //Clone Logical Line Coverage
    private float CLLC_ERROR=0.15f;
    private int LOC_WARNING=60; //Lines of Code
    private int LOC_ERROR=420;
    private float CD_WARNING=0.2f; //Comment Density
    private float CD_ERROR=0;
    private float AD_WARNING=0.2f; //API Documentation
    private float AD_ERROR=0;
    private int WMC_WARNING=11; // Weighted Methods per Class
    private int WMC_ERROR=34;
    private int TNM_WARNING=6; //Total Number of Methods
    private int TNM_ERROR=14;
    private int NL_WARNING=2; //Nesting Level
    private int NL_ERROR=4;
    private float CODE_COV_WARNING=50;
    private float CODE_COV_ERROR=0;

    private Grade completeness;
    private Grade correctness;
    private Grade appropriateness;
    private Grade modularity;
    private Grade reusability;
    private Grade analyzability;
    private Grade modifiability;
    private Grade testability;
    //private float MI; Maintainability Index


    private void setCompleteness(Grade code_cov) {
        this.completeness=code_cov;
    }

    private void setCorrectness(Grade bugs) {
        this.correctness = bugs;
    }

    private void setAppropriateness(Grade bugs) {
        this.appropriateness = bugs;
    }

    private void setModularity(Grade lcom5, Grade cbo, Grade loc) {
        if (lcom5==Grade.ERROR ||cbo==Grade.ERROR ||loc==Grade.ERROR) this.modularity=Grade.ERROR;
        else if (lcom5==Grade.WARNING ||cbo==Grade.WARNING||loc==Grade.WARNING) this.modularity=Grade.WARNING;
        else this.modularity=Grade.Green;
    }

    private void setReusability(Grade cbo, Grade noi, Grade cd) {
        if (cbo==Grade.ERROR || noi==Grade.ERROR ||cd==Grade.ERROR ) this.reusability = Grade.ERROR;
        else if (cbo==Grade.WARNING || noi==Grade.WARNING ||cd==Grade.WARNING)this.reusability = Grade.WARNING;
        else this.reusability = Grade.Green;
    }

    private void setAnalyzability(Grade lcom5, Grade loc, Grade cd, Grade ad, Grade wmc, Grade cllc) {
        if (lcom5==Grade.ERROR || loc==Grade.ERROR || cd==Grade.ERROR || ad==Grade.ERROR ||cllc==Grade.ERROR ||wmc==Grade.ERROR) this.analyzability = Grade.ERROR;
        else if(lcom5==Grade.WARNING ||loc==Grade.WARNING || cd==Grade.WARNING || ad==Grade.WARNING ||cllc==Grade.WARNING ||wmc==Grade.WARNING)this.analyzability = Grade.WARNING;
        else this.analyzability=Grade.Green;
    }

    private void setModifiability(Grade cboi, Grade nii, Grade cllc, Grade wmc) {
        if (cboi==Grade.ERROR || nii==Grade.ERROR || wmc==Grade.ERROR || cllc==Grade.ERROR) this.modifiability = Grade.ERROR;
        else if(cboi==Grade.WARNING || nii==Grade.WARNING || wmc==Grade.WARNING || cllc==Grade.WARNING) this.modifiability = Grade.WARNING;
        else this.modifiability = Grade.Green;
    }

    private void setTestability(Grade cbo, Grade wmc, Grade tnm, Grade nl) {
        if (cbo==Grade.ERROR || tnm==Grade.ERROR || wmc==Grade.ERROR || nl==Grade.ERROR) this.testability = Grade.ERROR;
        else if (cbo==Grade.WARNING || tnm==Grade.WARNING || wmc==Grade.WARNING || nl==Grade.WARNING) this.testability = Grade.WARNING;
        else this.testability = Grade.Green;
    }

    public ClassMetrics(String name, String project) {
        this.name=name.substring(name.lastIndexOf('.')+1);
        this.package_name=name.substring(0,name.lastIndexOf('.'));
        this.project=project;
    }

    public void calculate(int LCOM5, int CBO, int CBOI, int NII, int NOI, float CLLC, int LOC, float CD, float AD, int WMC, int TNM, int NL, int codecoverage, int bugs, int warnings) {
        Grade lcom5 = LCOM5<LCOM5_WARNING ? Grade.Green : (LCOM5<LCOM5_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("LCOM5", LCOM5, lcom5);
        Grade cbo = CBO<CBO_WARNING ? Grade.Green : (CBO<CBO_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("CBO", CBO, cbo);
        Grade cboi = CBOI<CBOI_WARNING ? Grade.Green : (CBOI<CBOI_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("CBOI", CBOI, cboi);
        Grade nii = NII<NII_WARNING ? Grade.Green : (NII<NII_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("NII", NII, nii);
        Grade noi = NOI<NOI_WARNING ? Grade.Green : (NOI<NOI_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("NOI", NOI, noi);
        Grade cllc = CLLC<CLLC_WARNING ? Grade.Green : (CLLC<CLLC_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("CLLC", CLLC, cllc);
        Grade loc = LOC<LOC_WARNING ? Grade.Green : (LOC<LOC_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("LOC", LOC, loc);
        Grade cd = CD>CD_WARNING ? Grade.Green : (CD>CD_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("CD", CD, cd);
        Grade ad = AD>AD_WARNING ? Grade.Green : (AD>AD_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("AD", AD, ad);
        Grade wmc = WMC<WMC_WARNING ? Grade.Green : (WMC<WMC_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("WMC", WMC, wmc);
        Grade tnm = TNM<TNM_WARNING ? Grade.Green : (TNM<TNM_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("TNM", TNM, tnm);
        Grade nl = NL<NL_WARNING ? Grade.Green : (NL<NL_ERROR ? Grade.WARNING : Grade.ERROR);
        print_log("NL", NL, nl);

        Grade code_cov;
        if (codecoverage==CODE_COV_ERROR)code_cov=Grade.ERROR;
        else if (codecoverage<CODE_COV_WARNING) code_cov=Grade.WARNING;
        else code_cov=Grade.Green;

        Grade bugcount=Grade.Green;

        if (bugs>0) bugcount=Grade.ERROR;
        else if (warnings>0) bugcount=Grade.WARNING;

        setCompleteness(code_cov);
        setCorrectness(bugcount);
        setAppropriateness(bugcount);
        setModularity(lcom5,cbo,loc);
        setReusability(cbo,noi,cd);
        setAnalyzability(lcom5,loc,cd,ad,wmc,cllc);
        setModifiability(cboi,nii,cllc,wmc);
        setTestability(cbo,wmc,tnm,nl);
    }

    public Grade getCompleteness() {
        return completeness;
    }
    public Grade getCorrectness() {
        return correctness;
    }
    public Grade getAppropriateness() {
        return appropriateness;
    }
    public Grade getModularity() {
        return modularity;
    }
    public Grade getReusability() {
        return reusability;
    }
    public Grade getAnalyzability() {
        return analyzability;
    }
    public Grade getModifiability() {
        return modifiability;
    }
    public Grade getTestability() {
        return testability;
    }

    private void print_log(String metric, float value, Grade grade){
        String result="";
        if (grade==Grade.Green) return;
        if (grade==Grade.WARNING){
            switch (metric){
                case "LCOM5":
                    result="Lack of Cohesion: Value " + (int)value + " violates threshold " + LCOM5_WARNING;
                    break;
                case "CBO":
                    result="Coupling Between Object: Value " + (int)value + " violates threshold " + CBO_WARNING;
                    break;
                case "CBOI":
                    result="Coupling Between Object classes Inverse: Value " + (int)value + " violates threshold " + CBOI_WARNING;
                    break;
                case "NII":
                    result="Number of Incoming Invocations: Value " + (int)value + " violates threshold " + NII_WARNING;
                    break;
                case "NOI":
                    result="Number of Outgoing Invocations: Value " + (int)value + " violates threshold " + NOI_WARNING;
                    break;
                case "CLLC":
                    result="Clone Logical Line Coverage: Value " + value + " violates threshold " + CLLC_WARNING;
                    break;
                case "LOC":
                    result="Lines of Code: Value " + (int)value + " violates threshold " + LOC_WARNING;
                    break;
                case "CD":
                    result="Comment Density: Value " + value + " violates threshold " + CD_WARNING;
                    break;
                case "AD":
                    result="API Documentation: Value " + value + " violates threshold " + AD_WARNING;
                    break;
                case "WMC":
                    result="Weighted Methods per Class: Value " + (int)value + " violates threshold " + WMC_WARNING;
                    break;
                case "TNM":
                    result="Total Number of Methods: Value " + (int)value + " violates threshold " + TNM_WARNING;
                    break;
                case "NL":
                    result="Nesting level: Value " + (int)value + " violates threshold " + NL_WARNING;
                    break;
            }
            append_to_log(metric,"Warnings",result);
        }else{
            switch (metric){
                case "LCOM5":
                    result="Lack of Cohesion: Value " + (int)value + " violates threshold " + LCOM5_ERROR;
                    break;
                case "CBO":
                    result="Coupling Between Object: Value " + (int)value + " violates threshold " + CBO_ERROR;
                    break;
                case "CBOI":
                    result="Coupling Between Object classes Inverse: Value " + (int)value + " violates threshold " + CBOI_ERROR;
                    break;
                case "NII":
                    result="Number of Incoming Invocations: Value " + (int)value + " violates threshold " + NII_ERROR;
                    break;
                case "NOI":
                    result="Number of Outgoing Invocations: Value " + (int)value + " violates threshold " + NOI_ERROR;
                    break;
                case "CLLC":
                    result="Clone Logical Line Coverage: Value " + value + " violates threshold " + CLLC_ERROR;
                    break;
                case "LOC":
                    result="Lines of Code: Value " + (int)value + " violates threshold " + LOC_ERROR;
                    break;
                case "CD":
                    result="Comment Density: Absence of comments in this class";
                    break;
                case "AD":
                    result="API Documentation: Absence of documentation in this class";
                    break;
                case "WMC":
                    result="Weighted Methods per Class: Value " + (int)value + " violates threshold " + WMC_ERROR;
                    break;
                case "TNM":
                    result="Total Number of Methods: Value " + (int)value + " violates threshold " + TNM_ERROR;
                    break;
                case "NL":
                    result="Nesting level: Value" + (int)value + " violates threshold " + NL_ERROR;
                    break;
            }
            append_to_log(metric,"Errors",result);
        }
    }

    private void append_to_log(String metric, String error, String message){
        try {
            File file = new File("report/" + project + "/log.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;
            Element packages;
            try {
                doc = dBuilder.parse(file);
                packages=doc.getDocumentElement();
            } catch (Exception e) {
                doc = dBuilder.newDocument();
                packages = doc.createElement("Packages");
                doc.appendChild(packages);
            }

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            Element epackage = (Element) xpath.evaluate("//*[@id='"+package_name+"']", packages, XPathConstants.NODE);

            if (epackage==null){
                epackage=doc.createElement("Package");
                epackage.setAttribute("id",package_name);
                packages.appendChild(epackage);
            }
            /*for (int i=0;i<packages.getLength();i++){
                epackage=(Element) packages.item(i);
                if (epackage.getAttribute("id").equals(package_name)) break;
                if (i==packages.getLength()-1){
                    epackage=doc.createElement("Package");
                    epackage.setAttribute("id",package_name);
                    doc.appendChild(epackage);
                }
            }*/
            Element eclass=(Element) xpath.evaluate("//*[@id='"+name+"']",epackage, XPathConstants.NODE);
            if (eclass==null){
                eclass=doc.createElement("Class");
                eclass.setAttribute("id",name);
                eclass.appendChild(doc.createElement("Warnings"));
                eclass.appendChild(doc.createElement("Errors"));
                epackage.appendChild(eclass);
            }
            Element emetric = doc.createElement("Metric");
            emetric.setAttribute("id",metric);
            emetric.appendChild(doc.createTextNode(message));
            eclass.getElementsByTagName(error).item(0).appendChild(emetric);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
