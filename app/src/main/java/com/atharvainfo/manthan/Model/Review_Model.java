package com.atharvainfo.manthan.Model;

public class Review_Model {
    private String quetion_id,selected_id,selected_answer,question,qpart1, qpart2, qpart3, qpart4, qpart5,op1,op2,op3,op4,answer
            ,question_image,question_image1,question_image2, question_image3, question_image4,op1_image,op2_image,op3_image,op4_image,questionno;

    public Review_Model(String quetion_id, String question,String qpart1, String qpart2, String qpart3, String qpart4, String qpart5, String op1
            ,String op2,String op3,String op4,String answer,
                        String question_image,String question_image1, String question_image2, String question_image3, String question_image4,
                        String op1_image,String op2_image,String op3_image,String op4_image,String questionno,String selected_id,String selected_answer) {
        this.quetion_id = quetion_id;
        this.question = question;
        this.qpart1 = qpart1;
        this.qpart2= qpart2;
        this.qpart3 = qpart3;
        this.qpart4 =qpart4;
        this.qpart5 =qpart5;
        this.op1 = op1;
        this.op2 = op2;
        this.op3= op3;
        this.op4 = op4;
        this.answer = answer;
        this.question_image = question_image;
        this.question_image1 = question_image1;
        this.question_image2 = question_image2;
        this.question_image3 = question_image3;
        this.question_image4 = question_image4;
        this.op1_image =  op1_image;
        this.op2_image =  op2_image;
        this.op3_image= op3_image;
        this.op4_image = op4_image;
        this.questionno = questionno;
        this.selected_id = selected_id;
        this.selected_answer = selected_answer;
    }

    public String getQuetion_id() {
        return quetion_id;
    }

    public void setQuetion_id(String quetion_id) {
        this.quetion_id = quetion_id;
    }

    public String getSelected_id() {
        return selected_id;
    }

    public void setSelected_id(String selected_id) {
        this.selected_id = selected_id;
    }

    public String getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(String selected_answer) {
        this.selected_answer = selected_answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setQpart1(String qpart1){
        this.qpart1 = qpart1;
    }

    public String getQpart1(){
        return  qpart1;
    }

    public String getQpart2(){
        return qpart2;
    }

    public void setQpart2(String qpart2) {
        this.qpart2 = qpart2;
    }
    public String getQpart3(){
        return qpart3;
    }

    public void setQpart3(String qpart3) {
        this.qpart3 = qpart3;
    }
    public String getQpart4(){
        return qpart4;
    }

    public void setQpart4(String qpart4) {
        this.qpart4 = qpart4;
    }

    public String getQpart5(){
        return qpart5;
    }

    public void setQpart5(String qpart5) {
        this.qpart5 = qpart5;
    }


    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }

    public String getQuestion_image1(){ return question_image1;}

    public void setQuestion_image1(String question_image1) {
        this.question_image1 = question_image1;
    }
    public String getQuestion_image2() { return question_image2; }

    public void setQuestion_image2(String question_image2) {
        this.question_image2 = question_image2;
    }
    public String getQuestion_image3() { return question_image3; }

    public void setQuestion_image3(String question_image3) {
        this.question_image3 = question_image3;
    }
    public String getQuestion_image4() {return question_image4; }

    public void setQuestion_image4(String question_image4) {
        this.question_image4 = question_image4;
    }


    public String getOp1_image() {
        return op1_image;
    }

    public void setOp1_image(String op1_image) {
        this.op1_image = op1_image;
    }

    public String getOp2_image() {
        return op2_image;
    }

    public void setOp2_image(String op2_image) {
        this.op2_image = op2_image;
    }

    public String getOp3_image() {
        return op3_image;
    }

    public void setOp3_image(String op3_image) {
        this.op3_image = op3_image;
    }

    public String getOp4_image() {
        return op4_image;
    }

    public void setOp4_image(String op4_image) {
        this.op4_image = op4_image;
    }

    public  String getQuestionno() {return questionno;}

    public void setQuestionno(String questionno) {
        this.questionno = questionno;
    }
}
