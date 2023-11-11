package com.japho.campus.center.Model;

import java.util.Date;

public class User {
    private String userid;

    private String natid;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String dateofb;
    private String dob;
    private  String mob;
    private String  yob;

    private String tribe;


    private String univ;
    private String county;
    private String consi;
    private String ward;
    private String bio;
    private String image;
    private String status;

    private String approval;
    private Date expiry;

    public User()
    {
         String userid="";

         String natid="";;
         String name="";;
         String email="";;
         String gender="";;
         String age="";;
         String dateofb="";;
         String dob="";;
          String mob="";;
         String  yob="";;

         String tribe="";;


         String univ="";;
         String county="";;
         String consi="";;
         String ward="";;
         String bio="";;
         String image="";;
         String status="";;

         String approval="";;
       expiry=new java.util.Date();
    }


    public User(String userid, String name, String natid,String email,String gender, String age, String dateofb ,String dob, String mob, String yob,String tribe, String univ,String county,String consi,String ward, String bio, String image,String status,String approval ,Date expiry) {
        this.userid = userid;
        this.name = name;
        this.natid=natid;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.dateofb=dateofb;
        this.dob = dob;
        this.mob = mob;
        this.yob = yob;

        this.tribe = tribe;


        this.univ =univ;
        this.county = county;
        this.consi = consi;
        this.ward = ward;
        this.bio=bio;
        this.image = image;
        this.status = status;
        this.approval=approval;
        this.expiry = expiry;
    }

    public String getId()
    {
        return userid;
    }


    public String getName() {
        return name;
    }

    public String getNatid() {
        return natid;
    }
    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
    public String getDateofb() {
        return dateofb;
    }
    public String getDOB() {
        return dob;
    }

    public String getMOB() {
        return mob;
    }

    public String getYOB() {
        return yob;
    }



    public String getTribe() {
        return tribe;
    }





    public String getUniv() {
        return univ;
    }

    public String getCounty() {
        return county;
    }

    public String getConsituency() {
        return consi;
    }

    public String getWard() {
        return ward;
    }

    public String getBio() {
        return bio;
    }

    public String getPhoto() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getApproval() {
        return approval;
    }

    public Date getExpiry()

    {
        return expiry;
    }
}



