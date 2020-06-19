package com.atharvainfo.manthan.Model;

public class UserData {

    private String userphone,useremail,userpass, firstname, lastname, gender, classname, schoolname,village,taluka,district,statename,pincode,studentid,student_image;

    public UserData(String userphone, String useremail, String userpass, String firstname, String lastname, String gender, String classname,
                    String schoolname, String village, String studentid, String student_image){
        this.userphone = userphone;
        this.useremail = useremail;
        this.userpass = userpass;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.classname = classname;
        this.schoolname = schoolname;
        this.village = village;
        this.studentid = studentid;
        this.student_image = student_image;
    }

    public String getUserphone(){return userphone;}

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseremail(){ return useremail;}

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpass() {return userpass;}

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }
    public String getFirstname(){return firstname;}

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {return lastname;}

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getGender(){return gender;}

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getClassname() {return classname;}

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSchoolname(){return schoolname;}

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
    public String getVillage(){return village;}

    public void setVillage(String village) {
        this.village = village;
    }
    public String getStudentid(){return studentid;}

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudent_image(){return student_image;}

    public void setStudent_image(String student_image) {
        this.student_image = student_image;
    }
}
