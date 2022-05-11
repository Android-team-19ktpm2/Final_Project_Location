package com.example.map_originnal.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String avatar;
    private String full_name;
    private String dob;
    private String email;
    private String phone;
    private String ocp;
    private String online;
    private String lat_X;
    private String long_Y;
<<<<<<< HEAD
    private String long_Z;
=======
    private String long_;U
>>>>>>> 4b5f3c3d143481ee00a89ab55fdf5748cc24e781

    public User()
    {

    }

    public User(String id, String email, String avatar, String full_name, String phone, String dob, String ocp, String online, String lat_X, String long_Y) {
        this.id = id;
        this.avatar = avatar;
        this.full_name = full_name;
        this.phone = phone;
        this.dob = dob;
        this.email = email;
        this.ocp = ocp;
        this.online = online;
        this.lat_X = lat_X;
        this.long_Y = long_Y;

    }

    public String getOcp() {
        return ocp;
    }

    public void setOcp(String ocp) {
        this.ocp = ocp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getFull_name() {
        return full_name;
    }


    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDob() {
        return dob;
    }


    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void display()
    {
        System.out.println("ID" + id);
        System.out.println("Avatar" + avatar);
        System.out.println("Full name" + full_name);
        System.out.println("Phone" + phone);
        System.out.println("DOB" + dob);
        System.out.println("Email" + email);
    }

    public String getLat_X() {
        return lat_X;
    }

    public void setLat_X(String lat_X) {
        this.lat_X = lat_X;
    }

    public String getLong_Y() {
        return long_Y;
    }

    public void setLong_Y(String long_Y) {
        this.long_Y = long_Y;
    }
}

