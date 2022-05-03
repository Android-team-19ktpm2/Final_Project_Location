package com.example.map_originnal.model;

import java.io.Serializable;

public class Message implements Serializable {
    private String src;
    private String dst;
    private String content;
    private String sendAt;
    private String location = null;
    public Message()
    {

    }

    public Message(String src, String dst, String content)
    {
        this.src = src;
        this.dst = dst;
        this.content = content;
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }

    public String getContent() {
        return content;
    }

    public String getSendAt() {
        return sendAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
         this.location = location;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSendAt(String sendAt) {
        this.sendAt = sendAt;
    }

    public void display()
    {
        System.out.println(src +" "+ dst +" "+ content);
    }
}
