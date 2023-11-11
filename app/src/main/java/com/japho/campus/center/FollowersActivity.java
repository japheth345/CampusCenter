package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.Adapter.UserAdapter;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Model.User;
//import com.japho.campus.center.Model.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FollowersActivity extends AppCompatActivity {

    private  String id;
    private String title;
    private List<String> idList;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(this, mUsers, false);
        recyclerView.setAdapter(userAdapter);

        idList = new ArrayList<>();

        switch (title) {
            case "followers" :
                getFollowers();
                break;

            case "followings":
                getFollowings();
                break;

            case "likes":
                getLikes();
                break;
        }
    }

    private void getFollowers() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add((snapshot.getKey()));
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFollowings() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add((snapshot.getKey()));
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getLikes() {

        FirebaseDatabase.getInstance().getReference().child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add((snapshot.getKey()));
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showUsers() {

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    String UserID =teacherSnapshot.child(NodeNames.USERID).getValue(String.class);
                    String name = teacherSnapshot.child(NodeNames.NAME).getValue(String.class);
                    String natid = teacherSnapshot.child(NodeNames.NATID).getValue(String.class);
                    String email = teacherSnapshot.child(NodeNames.EMAIL).getValue(String.class);
                    String gender = teacherSnapshot.child(NodeNames.GENDER).getValue(String.class);
                    String age = teacherSnapshot.child(NodeNames.AGE).getValue(String.class);
                    String dateofb = teacherSnapshot.child(NodeNames.DATEOB).getValue(String.class);
                    String dob = teacherSnapshot.child(NodeNames.DOB).getValue(String.class);
                    String mob = teacherSnapshot.child(NodeNames.MOB).getValue(String.class);
                    String yob = teacherSnapshot.child(NodeNames.YOB).getValue(String.class);

                    String tribe = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                    String hobby = teacherSnapshot.child(NodeNames.UNIV).getValue(String.class);
                    String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                    String consti = teacherSnapshot.child(NodeNames.CONSTITUENCY).getValue(String.class);
                    String ward = teacherSnapshot.child(NodeNames.WARD).getValue(String.class);

                    String bio = teacherSnapshot.child(NodeNames.BIO).getValue(String.class);
                    String photo = teacherSnapshot.child(NodeNames.PHOTO).getValue(String.class);
                    String status = teacherSnapshot.child(NodeNames.ONLINE).getValue(String.class);
                    String approval = teacherSnapshot.child(NodeNames.APPROVAL).getValue(String.class);
                 Date expiry = teacherSnapshot.child(NodeNames.EXPIRY).getValue(Date.class);


                    User user = new User(UserID, name,natid, email, gender, age, dateofb, dob, mob, yob,  tribe,  hobby, county, consti, ward, bio, photo, status,approval, expiry);

                    for (String id : idList) {
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }
                }
                Log.d("list f", mUsers.toString());
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
