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

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
