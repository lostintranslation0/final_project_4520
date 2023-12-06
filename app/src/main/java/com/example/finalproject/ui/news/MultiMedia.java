package com.example.finalproject.ui.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MultiMedia implements Serializable {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
