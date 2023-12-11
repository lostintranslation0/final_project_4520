package com.example.finalproject;

import com.example.finalproject.ui.blogs.Blog;

import java.util.List;

public class User {
    private String username;
    private String password;
    private int age;
    private String imageUrl;

    public List<Blog> getUserBlogs() {
        return null;
    }

    public List<String> getUserFollowers() {
        return null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
