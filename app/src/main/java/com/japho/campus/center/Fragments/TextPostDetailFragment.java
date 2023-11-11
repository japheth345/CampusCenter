package com.japho.campus.center.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.japho.campus.center.Adapter.ImagesAdapter;
import com.japho.campus.center.Adapter.TextsAdapter;
import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextPostDetailFragment extends Fragment {

    private String postId;
    private RecyclerView recyclerView;
    private TextsAdapter postAdapter;
    private List<Post> postList;

    public TextPostDetailFragment()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getContext()));
        //setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_post_detail, container, false);

        postId = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new TextsAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                String description=dataSnapshot.child("description").getValue(String.class);
                String imageurl=dataSnapshot.child("imageurl").getValue(String.class);
                String postid=dataSnapshot.child("postid").getValue(String.class);
                String publisher=dataSnapshot.child("publisher").getValue(String.class);
                String audience=dataSnapshot.child("audience").getValue(String.class);
                String visible=dataSnapshot.child("visible").getValue(String.class);
                String tribe=dataSnapshot.child("tribe").getValue(String.class);
                String county=dataSnapshot.child("county").getValue(String.class);
                String type=dataSnapshot.child("type").getValue(String.class);
                Date expiry = dataSnapshot.child("exp").getValue(Date.class);
                Post post = new Post(description,imageurl,postid,publisher,audience,visible,tribe,county,type,expiry);
                postList.add(post);

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}