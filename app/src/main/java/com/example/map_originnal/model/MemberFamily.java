package com.example.map_originnal.model;

public class MemberFamily {


    private String ten;
    private String img;


    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MemberFamily() {
    }

    public MemberFamily(String ten, String img) {
        this.ten = ten;
        this.img = img;
    }
}
