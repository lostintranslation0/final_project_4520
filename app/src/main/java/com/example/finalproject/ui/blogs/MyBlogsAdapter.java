package com.example.finalproject.ui.blogs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.ui.news.Article;

import java.util.List;

public class MyBlogsAdapter extends RecyclerView.Adapter<MyBlogsAdapter.BlogViewHolder> {

    private List<Blog> dataList;

    public MyBlogsAdapter(List<Blog> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setDataList(List<Blog> blogs)
    {
        this.dataList = blogs;
        notifyDataSetChanged();
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        // Add your item views here
        private TextView textView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your item views here
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(Blog data) {
            // Bind data to your item views here
            textView.setText("hi");
        }
    }
}

