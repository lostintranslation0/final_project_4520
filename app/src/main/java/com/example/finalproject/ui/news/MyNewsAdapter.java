package com.example.finalproject.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ArticleViewHolder> {

    private List<Article> dataList;

    public MyNewsAdapter(List<Article> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<Article> articles) {
        this.dataList = articles;
        notifyDataSetChanged();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView newsTitle, newsSection, newsDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsSection = itemView.findViewById(R.id.newsSection);
            newsDate = itemView.findViewById(R.id.newsDate);
        }

        public void bind(Article data) {
            newsTitle.setText(data.getTitle());
            newsSection.setText(data.getSection());
            newsDate.setText(data.getPublished_date());
        }
    }
}
