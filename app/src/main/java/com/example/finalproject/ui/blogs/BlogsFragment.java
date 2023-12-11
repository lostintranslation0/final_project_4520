package com.example.finalproject.ui.blogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.CreateBlogActivity;
import com.example.finalproject.FirebaseAuthentication;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBlogsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String currUser = ((HomeActivity)getActivity()).currUser;

        createBlogButton = root.findViewById(R.id.createBlogButton);
        myRecyclerView = root.findViewById(R.id.blogRecyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyBlogsAdapter(new ArrayList<>());
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
                        List<String> comments = (List<String>)document.get("comments");

                        Blog b = new Blog(userWhoCreated, title, description, content, date.toDate(), comments);
                        resultBlogs.add(b);
                    }
                    adapter.setDataList(resultBlogs);
                }
        }});

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}