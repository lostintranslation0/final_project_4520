package com.example.finalproject.ui.blogs;



import com.example.finalproject.Comment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blog {
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
        this.comments = new ArrayList<>();
    }
    public Blog(String userWhoCreated, String title, String description, String content, Date date, List<String> comments) {
        this.userWhoCreated = userWhoCreated;
        this.title = title;
        this.description = description;
        this.content = content;
        this.date = date;
        setCommentsFromJsonStrings(comments);
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

    public void setCommentsFromJsonStrings(List<String> jsons)
    {
        Gson gson = new Gson();
        List<Comment> comments = new ArrayList<>();
        for (String s : jsons)
        {
            Comment c = gson.fromJson(s, Comment.class);
            comments.add(c);
        }
        this.comments = comments;
    }
}
