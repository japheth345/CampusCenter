package com.japho.campus.center.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.japho.campus.center.Adapter.ImagesAdapter;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private ImagesAdapter postAdapter;
    private List<Post> postList;

    private List<String> followingList;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getContext()));
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new ImagesAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);

        followingList = new ArrayList<>();

        checkFollowingUsers();

        return view;
    }

    private void checkFollowingUsers() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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
                    if (post.getAudience().equals("Your Followers"))
                    {
                        for (String id : followingList)
                        {
                            if (post.getPublisher().equals(id))
                            {
                                postList.add(post);
                                postAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    else if (post.getAudience().equals("All Universities"))
                    {
                        postList.add(post);
                        postAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                String hobby = teacherSnapshot.child(NodeNames.UNIV).getValue(String.class);
                                if (post.getAudience().equals(hobby))
                                {
                                    postList.add(post);
                                    postAdapter.notifyDataSetChanged();
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
