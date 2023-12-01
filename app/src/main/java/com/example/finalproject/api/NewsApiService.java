package com.example.finalproject.api;

import com.example.finalproject.ui.news.TopStoriesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("{section}.json")
    Call<TopStoriesResponse> getTopStories(@Path("section") String section, @Query("api-key") String apiKey);
}
