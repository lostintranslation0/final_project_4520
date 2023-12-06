package com.example.finalproject.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.ArticleDetailActivity;
import com.example.finalproject.R;
import com.example.finalproject.api.NewsApiService;
import com.example.finalproject.databinding.FragmentNewsBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private RecyclerView myRecyclerView;
    private MyNewsAdapter adapter;
    private NewsApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView
        myRecyclerView = binding.articlesRecyclerView;
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyNewsAdapter(new ArrayList<>(), this::onArticleClicked);
        myRecyclerView.setAdapter(adapter);

        // Initialize Retrofit for top stories
        Retrofit retrofitTopStories = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/svc/topstories/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofitTopStories.create(NewsApiService.class);
        fetchData();

        return root;
    }

    private void onArticleClicked(Article article) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("ARTICLE_DATA", article); // Make sure Article is Serializable or Parcelable
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the ActionBar title
        if (getActivity() != null) {
            getActivity().setTitle("News");
        }
    }

    private void fetchData() {
        apiService.getTopStories("home", "R8rM0UVXsNLLLjtVZqUi149Kyl8Yfmez").enqueue(new Callback<TopStoriesResponse>() {
            @Override
            public void onResponse(Call<TopStoriesResponse> call, Response<TopStoriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getResults();
                    adapter.setDataList(articles);
                } else {
                    Log.e("NewsFragment", "fetchData - Response not successful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TopStoriesResponse> call, Throwable t) {
                Log.e("NewsFragment", "fetchData - Network call failed: " + t.getMessage(), t);
            }
        });
    }


    private void fetchPopularArticles() {
        Retrofit retrofitPopularArticles = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/svc/mostpopular/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService apiService = retrofitPopularArticles.create(NewsApiService.class);
        apiService.getEmailedArticles(7, "R8rM0UVXsNLLLjtVZqUi149Kyl8Yfmez").enqueue(new Callback<PopularArticlesResponse>() {
            @Override
            public void onResponse(Call<PopularArticlesResponse> call, Response<PopularArticlesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setDataList(response.body().getResults());
                } else {
                    Log.e("NewsFragment", "fetchPopularArticles - Response not successful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PopularArticlesResponse> call, Throwable t) {
                Log.e("NewsFragment", "fetchPopularArticles - Network call failed: " + t.getMessage(), t);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchPopularArticles) {
            fetchPopularArticles();
            return true;
        }
        else if (id == R.id.searchTopStories) {
            fetchData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
