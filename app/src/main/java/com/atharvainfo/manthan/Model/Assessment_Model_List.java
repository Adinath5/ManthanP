package com.atharvainfo.manthan.Model;

public class Assessment_Model_List {

    private String student_id,exam_id,examdate,exid,noofqt,rightans,wrongans,solveqt,excompleted;

    public Assessment_Model_List(String student_id, String exam_id, String examdate,String exid ,String noofqt,String rightans, String wrongans, String solveqt, String excompleted) {
        this.student_id = student_id;
        this.exam_id = exam_id;
        this.examdate = examdate;
        this.exid = exid;
        this.noofqt = noofqt;
        this.rightans= rightans;
        this.wrongans = wrongans;
        this.solveqt = solveqt;
        this.excompleted = excompleted;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getExamdate() {
        return examdate;
    }

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getExid() {
        return exid;
    }

    public void setExid(String completed) {
        this.exid = exid;
    }

    public String getNoofqt() {
        return noofqt;
    }

    public void setNoofqt(String score) {
        this.noofqt = noofqt;
    }

    public String getRightans() {
        return rightans;
    }

    public void setRightans(String status) {
        this.rightans = rightans;
    }

    public String getWrongans() {return wrongans;}

    public void setWrongans(String wrongans) {
        this.wrongans = wrongans;
    }

    public String getSolveqt(){ return solveqt;}

    public void setSolveqt(String solveqt) {
        this.solveqt = solveqt;
    }

    public String getExcompleted(){ return excompleted;}

    public void setExcompleted(String excompleted) {
        this.excompleted = excompleted;
    }
}
