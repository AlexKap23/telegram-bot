package com.alexandros.teleram.bot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userinfo")
public class UserInfo {

    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String afm;
    @Indexed(unique = true)
    private String amka;
    @Indexed(unique = true)
    private String mobilePhone;
    @Indexed(unique = true)
    private String email;


    public UserInfo(String id, String name, String afm, String amka, String mobilePhone,String email) {
        this.id = id;
        this.name = name;
        this.afm = afm;
        this.amka = amka;
        this.mobilePhone = mobilePhone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Id:" + this.id + " \n" + "Name: " + this.name + " \n" + "AFM: " + this.afm + " \n" + "AMKA: " + this.amka + " \n" + "Mobile: "
            + this.mobilePhone + " \n" + "Email: " + this.email + " \n" + "\n\n";
    }
}
