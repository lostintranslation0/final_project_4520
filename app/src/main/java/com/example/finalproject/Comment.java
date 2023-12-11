package com.example.finalproject;

import java.util.Date;

public class Comment {
    private String user;
    private String comment;

    private Date date;

    public Comment(String user, String comment, Date date) {
        this.user = user;
        this.comment = comment;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
