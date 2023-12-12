package com.example.finalproject.ui.blogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.HomeActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentBlogsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BlogsFragment extends Fragment {

    private FragmentBlogsBinding binding;

    RecyclerView myRecyclerView;
    MyBlogsAdapter adapter;

    Button createBlogButton;
    String currUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBlogsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        currUser = ((HomeActivity)getActivity()).currUser;

        createBlogButton = root.findViewById(R.id.createBlogButton);
        myRecyclerView = root.findViewById(R.id.blogRecyclerView);
        setupRecyclerView();

        createBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getContext(), CreateBlogActivity.class );
                intent.putExtra("USERNAME", currUser);
                startActivity(intent);
            }
        });

        return root;
    }

    private void onBlogClicked(Blog blog) {
        Intent intent = new Intent(getActivity(), BlogDetailActivity.class);
        intent.putExtra("USERNAME", currUser);
        intent.putExtra("BLOG_DATA", blog); // Make sure Article is Serializable or Parcelable
        startActivity(intent);
    }

    private void setupRecyclerView()
    {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyBlogsAdapter(new ArrayList<>(), this::onBlogClicked);
        myRecyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("blogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<Blog> resultBlogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.v("in explore querying database for data" , document.getId() + " => " + document.getData());
                        String title = (String)document.get("title");
                        String description = (String)document.get("description");
                        String content = (String)document.get("content");
                        String userWhoCreated = (String)document.get("userWhoCreated");
                        Timestamp date = (Timestamp)document.get("date");
                        String jsonComments = (String)document.get("comments");

                        Blog b = new Blog(userWhoCreated, title, description, content, date.toDate(), jsonComments);
                        resultBlogs.add(b);
                    }
                    adapter.setDataList(resultBlogs);
                }
            }});
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}