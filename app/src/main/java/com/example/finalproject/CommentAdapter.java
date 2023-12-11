package com.example.finalproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> dataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Comment comment);
    }

    public CommentAdapter(List<Comment> dataList) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment data = dataList.get(position);
        holder.bind(data, listener);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<Comment> comments) {
        this.dataList = comments;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView userTV, dateTV, contentTV;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userTV = itemView.findViewById(R.id.followerNameTV);
            dateTV = itemView.findViewById(R.id.commentDateTextView);
            contentTV = itemView.findViewById(R.id.commentContentTextView);
        }

        public void bind(Comment data, OnItemClickListener listener) {
            userTV.setText(data.getUser());
            dateTV.setText(data.getDate().toString());
            contentTV.setText(data.getComment());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(data);
                }
            });
        }
    }
}

