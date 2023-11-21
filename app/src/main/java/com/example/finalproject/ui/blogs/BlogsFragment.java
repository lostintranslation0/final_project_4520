package com.example.finalproject.ui.blogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentBlogsBinding;

import java.util.ArrayList;
import java.util.List;

public class BlogsFragment extends Fragment {

    private FragmentBlogsBinding binding;

    RecyclerView myRecyclerView;
    MyBlogsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BlogsViewModel blogsViewModel =
                new ViewModelProvider(this).get(BlogsViewModel.class);

        binding = FragmentBlogsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // TODO: Get blogs from database
        List<Blog> userBlogs = new ArrayList<>();

        myRecyclerView = root.findViewById(R.id.blogRecyclerView);
        adapter = new MyBlogsAdapter(userBlogs);
        myRecyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}