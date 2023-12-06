package com.example.finalproject.ui.news;
import android.util.Log;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    private String title;
    private String section;
    private String published_date;
    @SerializedName("multimedia")
    private List<MultiMedia> multimedia;
    @SerializedName("abstract")
    private String abstractText;


    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getPublished_date() {
        return published_date;
    }

    public String getAbstract() {
        return abstractText;
    }

    public String getImageUrl() {
        if (multimedia != null && !multimedia.isEmpty()) {
            return multimedia.get(0).getUrl();
        }
        return null;
    }
}
