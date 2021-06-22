package com.android.diaryapp.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class PostMessage implements Serializable {
    public  String id;
    public  String tittle;
    public  String content;
    public  Date date;
    public  Time time;
    public  int background;

    public PostMessage()
    {
    }
    public PostMessage(String id, String tittle, String content, Date date, Time time, int background) {
        this.id = id;
        this.tittle = tittle;
        this.content = content;
        this.date = date;
        this.time = time;
        this.background = background;
    }

    public String getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getBackground() {
        return background;
    }
}
