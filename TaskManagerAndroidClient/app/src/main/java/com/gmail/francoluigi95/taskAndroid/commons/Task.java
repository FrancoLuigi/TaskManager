package com.gmail.francoluigi95.taskAndroid.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    public Task(String title, String text, Date date) {
        this.title = title;
        this.text = text;
        this.date = date;
        state = "NoResponsabile";
        user = null;


    }

    public String getTitle() {
        return title;
    }


    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }


    public Date getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String toString() {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyy");

        return "Titolo: " + title + ", " + "testo: " + text + ", " + "data: " + sd.format(date) + ", " + "stato: " + state + ", " + "user: " + user + ";";
    }

    private String title, text;

    private String user;
    private Date date;
    private String state;


}
