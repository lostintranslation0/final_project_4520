package com.example.finalproject;

import com.example.finalproject.ui.blogs.Blog;

import java.util.List;

public class User {
    private String email;
    private String username;
    private int age;
    private List<String> followers;

    private List<String> following;

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    private String imageUrl;
    public User() {}

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getFollowing() { return following; }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
