package com.atharvainfo.manthan.Model;

public class Exam_Model_List {

    private String exam_id,duration,passmark,exam_type,exam_fee,exam_medium,class_name,
            exam_status,quetions,exam_testname,exam_papername,terms;

    public Exam_Model_List(String exam_id, String duration, String passmark, String exam_fee,String exam_medium,String class_name,String exam_status
                                ,String quetions, String exam_testname, String exam_papername, String terms, String exam_type) {
        this.exam_id = exam_id;
        this.duration = duration;
        this.passmark = passmark;
        this.exam_fee = exam_fee;
        this.exam_medium = exam_medium;
        this.class_name = class_name;
        this.exam_status = exam_status;
        this.quetions= quetions;
        this.exam_testname = exam_testname;
        this.exam_papername = exam_papername;
        this.terms = terms;
        this.exam_type = exam_type;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPassmark() {
        return passmark;
    }

    public void setPassmark(String passmark) {
        this.passmark = passmark;
    }

    public String getExam_fee() {
        return exam_fee;
    }

    public void setExam_fee(String exam_fee) {
        this.exam_fee = exam_fee;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getExam_status() {
        return exam_status;
    }

    public void setExam_status(String exam_status) {
        this.exam_status = exam_status;
    }

    public String getQuetions() {
        return quetions;
    }

    public void setQuetions(String quetions) {
        this.quetions = quetions;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getExam_type() {return  exam_type;}

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public String getExam_medium() {return exam_medium; }

    public void setExam_medium(String exam_medium) {
        this.exam_medium = exam_medium;
    }

    public String getExam_testname() { return exam_testname; }

    public void setExam_testname(String exam_testname) {
        this.exam_testname = exam_testname;
    }

    public String getExam_papername() { return exam_papername; }

    public void setExam_papername(String exam_papername) {
        this.exam_papername = exam_papername;
    }
}
