package com.unipi.atheodoridis.nfccardapp.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {
    String am;
    String fname;
    String lname;
    String email;
    String cardnum;

    public UserModel() {}

    public UserModel(String am, String fname, String lname, String email, String cardnum){
        this.am=am;
        this.fname=fname;
        this.lname=lname;
        this.email=email;
        this.cardnum=cardnum;
    }


    public String getAM(){
        return am;
    }
    public void setAM(String am){
        this.am=am;
    }
    public String getFname(){
        return fname;
    }
    public void setFname(String fname){
        this.fname=fname;
    }
    public String getLname(){
        return lname;
    }
    public void setLname(String lname){
        this.lname=lname;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getCardnum(){
        return cardnum;
    }
    public void setCardnum(String cardnum){
        this.cardnum=cardnum;
    }
}
