package com.example.finalproject.ui.blogs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.User;
import com.example.finalproject.ui.news.Article;
import com.example.finalproject.ui.news.MyNewsAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyBlogsAdapter extends RecyclerView.Adapter<MyBlogsAdapter.BlogViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Blog blog);
    }
    private List<Blog> dataList;

    public MyBlogsAdapter(List<Blog> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
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
        holder.bind(data, listener);
        holder.loadProfileImage(data.getUserWhoCreated());
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
        private TextView titleTV;
        private TextView authorTV;
        private TextView descTV;
        private TextView dateTV;
        private ImageView profileImageView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.blogImageProfile);
            titleTV = itemView.findViewById(R.id.blogTitleTextView);
            authorTV = itemView.findViewById(R.id.blogAuthorTextView);
            descTV = itemView.findViewById(R.id.blogDescTextView);
            dateTV = itemView.findViewById(R.id.blogDateTextView);
        }

        public void bind(Blog b, OnItemClickListener listener) {
            titleTV.setText(b.getTitle());
            authorTV.setText(b.getUserWhoCreated());
            descTV.setText(b.getDescription());
            dateTV.setText(b.getDate().toString());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(b);
                }
            });
        }
        public void loadProfileImage(String username) {
            FirebaseFirestore.getInstance().collection("users")
                    .document(username)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                if (user != null && user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                    Glide.with(itemView.getContext())
                                            .load(user.getImageUrl())
                                            .placeholder(R.drawable.default_profile_image)
                                            .error(R.drawable.default_profile_image)
                                            .into(profileImageView);
                                } else {
                                    profileImageView.setImageResource(R.drawable.default_profile_image);
                                }
                            }
                        } else {
                            Log.e("MyBlogsAdapter", "Error getting user data: ", task.getException());
                        }
                    });
        }
    }
}