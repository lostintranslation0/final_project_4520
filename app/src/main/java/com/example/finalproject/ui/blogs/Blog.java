package com.example.finalproject.ui.blogs;



import android.util.Log;

import com.example.finalproject.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blog implements Serializable {
    private String userWhoCreated;
    private String title;
    private String description;



    private String content;
    private Date date;
    private List<Comment> comments;

    public Blog() { }

    public Blog(String userWhoCreated, String title, String description, String content, Date date) {
        this.userWhoCreated = userWhoCreated;
        this.title = title;
        this.description = description;
        this.content = content;
        this.date = date;
    }
    public Blog(String userWhoCreated, String title, String description, String content, Date date, String jsonComments) {
        this.userWhoCreated = userWhoCreated;
        this.title = title;
        this.description = description;
        this.content = content;
        this.date = date;
        setCommentsFromJsonString(jsonComments);
    }

    public String getUserWhoCreated() {
        return userWhoCreated;
    }

    public void setUserWhoCreated(String userWhoCreated) {
        this.userWhoCreated = userWhoCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setCommentsFromJsonString(String jsonComments)
    {
        Log.v("Setting blog with comment json:", jsonComments);
        Gson gson = new Gson();
        Type commentListType = new TypeToken<List<Comment>>(){}.getType();
        this.comments = gson.fromJson(jsonComments, commentListType);
        Log.v("Setting comments list to: ", this.comments.toString());
    }
}
