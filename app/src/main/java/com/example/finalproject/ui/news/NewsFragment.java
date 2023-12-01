package com.example.finalproject.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.api.NewsApiService;
import com.example.finalproject.databinding.FragmentNewsBinding;

import java.util.ArrayList;

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
        adapter = new MyNewsAdapter(new ArrayList<>()); // Initially empty list
        myRecyclerView.setAdapter(adapter);

        // Initialize Retrofit and fetch data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/svc/topstories/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NewsApiService.class);
        fetchData();

        return root;
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
                    adapter.setDataList(response.body().getResults());
                } else {
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<TopStoriesResponse> call, Throwable t) {
                // Handle network failure
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_news, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
