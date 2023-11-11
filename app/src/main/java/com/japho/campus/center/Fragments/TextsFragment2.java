package com.japho.campus.center.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japho.campus.center.Adapter.TextsAdapter;
import com.japho.campus.center.AddAudioActivity;
import com.japho.campus.center.AddTextActivity;
import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.Dashboard;
import com.japho.campus.center.Helper;
import com.japho.campus.center.R;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.japho.campus.center.Adapter.ImagesAdapter;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.R;
import com.japho.campus.center.SMainActivity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextsFragment2 extends Fragment {

    private RecyclerView recyclerViewPosts;
    private TextsAdapter postAdapter;
    private List<Post> postList;

    private List<String> followingList;
    private ProgressDialog progressDialog;
    private TextView textview33;
    String id;
    public TextsFragment2(String id)
    {
       this.id=id;
    }

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
        View view = inflater.inflate(R.layout.fragment_texts2, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        textview33 = view.findViewById(R.id.textView33);

        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new TextsAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);

        followingList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        readPosts();
        // checkFollowingUsers();
    }

    private void checkFollowingUsers() {
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                // readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readPosts() {
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                postList.clear();
                for (DataSnapshot dataSnapshot : dataSnapshot2.getChildren()) {
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String imageurl = dataSnapshot.child("imageurl").getValue(String.class);
                    String postid = dataSnapshot.child("postid").getValue(String.class);
                    String publisher = dataSnapshot.child("publisher").getValue(String.class);
                    String audience = dataSnapshot.child("audience").getValue(String.class);
                    String visible = dataSnapshot.child("visible").getValue(String.class);
                    String tribe = dataSnapshot.child("tribe").getValue(String.class);
                    String county = dataSnapshot.child("county").getValue(String.class);
                    String type = dataSnapshot.child("type").getValue(String.class);
                    Date expiry = dataSnapshot.child("exp").getValue(Date.class);
                    Post post = new Post(description,imageurl,postid,publisher,audience,visible,tribe,county,type,expiry);
                    if (post.getType().equals("text") && post.getPublisher().equals(id) && post.getVisible().equals("No"))
                    {



                                postList.add(post);
                                postAdapter.notifyDataSetChanged();


                  }}
                textview33.setText(postList.size()+"\t Texts");
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                progressDialog.cancel();
                Toast.makeText(getContext(), "Loading failed \n"+databaseError.getMessage() +"\n Try again later", Toast.LENGTH_LONG).show();
            }


        });
    }

}



