package com.example.map_originnal.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String avatar;
    private String email;
    private String first_name;
    private String last_name;
    private String online;

    public User()
    {

    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public User(String id, String email, String avatar, String first_name, String last_name, String online)
    {
        this.id = id;
        this.email = email;
        this.avatar=avatar;
        this.first_name = first_name;
        this.last_name = last_name;
        this.online = online;
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
        System.out.println(id+" "+ email +" "+avatar);
    }


}

