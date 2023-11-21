package com.example.finalproject.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentNewsBinding;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    RecyclerView myRecyclerView;
    MyNewsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TODO: Get articles from API call (async)?
        List<Article> articles = new ArrayList<>();

        myRecyclerView = root.findViewById(R.id.articlesRecyclerView);
        adapter = new MyNewsAdapter(articles);
        myRecyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}