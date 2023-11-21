package com.example.finalproject.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.blogs.Blog;
import com.example.finalproject.ui.blogs.MyBlogsAdapter;

import java.util.List;

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ArticleViewHolder> {

    private List<Article> dataList;

    public MyNewsAdapter(List<Article> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNewsAdapter.ArticleViewHolder holder, int position) {
        Article data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<Article> articles)
    {
        this.dataList = articles;
        notifyDataSetChanged();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        // Add your item views here
        private TextView textView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your item views here
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(Article data) {
            // Bind data to your item views here
            textView.setText("hi");
        }
    }
}