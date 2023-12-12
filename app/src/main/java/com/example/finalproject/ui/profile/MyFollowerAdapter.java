package com.example.finalproject.ui.profile;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.ui.news.Article;
import com.example.finalproject.ui.news.MyNewsAdapter;

import java.util.List;

public class MyFollowerAdapter extends RecyclerView.Adapter<MyFollowerAdapter.FollowerViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String follower);
    }
    private List<String> dataList;

    public MyFollowerAdapter(List<String> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item, parent, false);
        return new MyFollowerAdapter.FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerViewHolder holder, int position) {
        String data = dataList.get(position);
        holder.bind(data, listener);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setDataList(List<String> followers)
    {
        this.dataList = followers;
        notifyDataSetChanged();
    }


    public static class FollowerViewHolder extends RecyclerView.ViewHolder {

        // Add your item views here
        private TextView followerTV;
        public FollowerViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your item views here
            followerTV = itemView.findViewById(R.id.followerNameTV);
        }

        public void bind(String s, OnItemClickListener listener) {
            // Bind data to your item views here
            followerTV.setText(s);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(s);
                }
            });
        }
    }
}

