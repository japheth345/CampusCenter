package com.japho.campus.center.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
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

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.Adapter.ImagesAdapter;
import com.japho.campus.center.Adapter.PhotosAdapter;
import com.japho.campus.center.AddPhotoActivity;
import com.japho.campus.center.AddTextActivity;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.Dashboard;
import com.japho.campus.center.Helper;
import com.japho.campus.center.LoginActivity;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.R;
import com.japho.campus.center.RegisterNameActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PhotosFragment extends Fragment {
    private RecyclerView recyclerViewPosts;
    private PhotosAdapter postAdapter;
    private List<Post> postList;
    SearchView searchView ;
    private List<Post> postList2;
    private List<String> followingList;
    private ProgressDialog progressDialog;
    private ImageView imageView,imageView2;
   // private TextView textview;
    String univ = "All Universities";
    String univ3 ="All Universities";
    String[] univs;
    Dialog dialog;
    public PhotosFragment() {
        // Required empty public constructor
    }


    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();

        return fragment;
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
        View view =inflater.inflate(R.layout.fragment_photos, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        imageView=view.findViewById(R.id.addPost);
        imageView2=view.findViewById(R.id.addBack);
        //textview = view.findViewById(R.id.textView);
        univs = Helper.getUniversities2();
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PhotosAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);
        postList2 = new ArrayList<>();
        searchView =view.findViewById(R.id.search);
        followingList = new ArrayList<>();
        progressDialog=new ProgressDialog(getContext());
       // checkFollowingUsers();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                VerifyUserExistence();

              /*  Intent mainIntent = new Intent(getContext(), AddPhotoActivity.class);
                // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(mainIntent);

               */

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(getContext(), Dashboard.class);
                // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(mainIntent);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            // Responsible for displaying all possible string from the list based on each additionnal character input made by user
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                progressDialog.setTitle("Searching");
                progressDialog.setMessage("please wait, While we get results for you");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                postList.clear();
                try {

                    for (Post list :postList2) {


                        final String desc = list.getDescription().toLowerCase();
                        final String aud = list.getAudience().toLowerCase();
                        final String tribe = list.getTribe().toLowerCase();
                        final String county = list.getCounty().toLowerCase();

                        if (newText.contains(desc)   || desc.contains(newText) || newText.contains(aud) || aud.contains(newText) || newText.contains(tribe) || tribe.contains(newText) || newText.contains(county) || county.contains(newText) || newText.contains(county))
                        {
                            if (postList.indexOf(list) < 0)
                            {
                                postList.add(list);
                            }

                        }


                       /* if (newText.contains(aud) || aud.contains(newText) || ){
                            if (postList.indexOf(list) < 0)
                            {
                                postList.add(list);
                            }
                        }

                        if (newText.contains(tribe) || tribe.contains(newText)) {
                            if (postList.indexOf(list) < 0)
                            {
                                postList.add(list);
                            }
                        }

                        if (newText.contains(county) || county.contains(newText)) {
                            if (postList.indexOf(list) < 0)
                            {
                                postList.add(list);
                            }
                        }



                        */




                    }
                    progressDialog.cancel();
                    postAdapter.notifyDataSetChanged(); //notify the adapter that the dataset was changed
                    return true;
                }
                catch (Exception ex)
                {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), "Sorry an error has occurred try again", Toast.LENGTH_LONG).show();
                }
                return true;
            }});

       /* textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner5);

                // set custom height and width
                dialog.getWindow().setLayout(650, 800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();
                TextView tv = dialog.findViewById(R.id.tv);
                tv.setText("Select University");
                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, univs);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        textview.setText(adapter.getItem(position));
                        univ = adapter.getItem(position);

                        // Dismiss dialog
                        dialog.dismiss();
                        readPosts2(univ);

                    }
                });
            }
        });

        */

        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        //checkFollowingUsers();
        readPosts();
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
                readPosts();
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
                postList2.clear();
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
                    if (post.getType().equals("photo")) {
     /*
                        if (post.getAudience().equals("Your Followers")) {
                            for (String id : followingList) {

                                if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {
                                    if (post.getPublisher().equals(id)) {
                                        postList.add(post);
                                        postAdapter.notifyDataSetChanged();
                                    }
                                } else if (post.getTribe().equals("All Tribes") && post.getCounty() != "All Counties") {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                            String hobby = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                            if (post.getCounty().equals(hobby)) {
                                                postList.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else if (post.getTribe() != "All Tribes" && post.getCounty().equals("All Counties")) {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                            String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                                            if (post.getTribe().equals(hobby)) {
                                                postList.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else if (post.getTribe() != "All Tribes" && post.getCounty() != "All Counties") {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                            String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);
                                            String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                            if (post.getTribe().equals(hobby) && post.getCounty().equals(county)) {
                                                postList.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                        */
                        if (post.getAudience().equals("All Universities")) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {

                                postList.add(post);
                                postList2.add(post);
                                postAdapter.notifyDataSetChanged();

                            } else if (post.getTribe().equals("All Tribes") && post.getCounty() != "All Counties") {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                        String hobby = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                        if (post.getCounty().equals(hobby)) {
                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else if (post.getTribe() != "All Tribes" && post.getCounty().equals("All Counties")) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                        String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                                        if (post.getTribe().equals(hobby)) {
                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else if (post.getTribe() != "All Tribes" && post.getCounty() != "All Counties") {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                        String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);
                                        String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                        if (post.getTribe().equals(hobby) && post.getCounty().equals(county)) {
                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                    String univ = teacherSnapshot.child(NodeNames.UNIV).getValue(String.class);
                                    if (post.getAudience().equals(univ)) {
                                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {

                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();

                                        } else if (post.getTribe().equals("All Tribes") && post.getCounty() != "All Counties") {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                                    String hobby = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                                    if (post.getCounty().equals(hobby)) {
                                                        postList.add(post);
                                                        postList2.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        } else if (post.getTribe() != "All Tribes" && post.getCounty().equals("All Counties")) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                                    String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                                                    if (post.getTribe().equals(hobby)) {
                                                        postList.add(post);
                                                        postList2.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        } else if (post.getTribe() != "All Tribes" && post.getCounty() != "All Counties") {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                                    String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);
                                                    String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                                    if (post.getTribe().equals(hobby) && post.getCounty().equals(county)) {
                                                        postList.add(post);
                                                        postList2.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });


                        }
                    }
                }
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

    private void readPosts2() {
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                postList.clear();
                postList2.clear();
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
                    if (post.getType().equals("photo")) {
     /*
                        if (post.getAudience().equals("Your Followers")) {
                            for (String id : followingList) {

                                if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {
                                    if (post.getPublisher().equals(id)) {
                                        postList.add(post);
                                        postAdapter.notifyDataSetChanged();
                                    }
                                } else if (post.getTribe().equals("All Tribes") && post.getCounty() != "All Counties") {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                            String hobby = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                            if (post.getCounty().equals(hobby)) {
                                                postList.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else if (post.getTribe() != "All Tribes" && post.getCounty().equals("All Counties")) {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                            String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                                            if (post.getTribe().equals(hobby)) {
                                                postList.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else if (post.getTribe() != "All Tribes" && post.getCounty() != "All Counties") {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                            String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);
                                            String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                            if (post.getTribe().equals(hobby) && post.getCounty().equals(county)) {
                                                postList.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                        */
                        if (post.getAudience().equals("All Universities")) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {

                                postList.add(post);
                                postList2.add(post);
                                postAdapter.notifyDataSetChanged();

                            } else if (post.getTribe().equals("All Tribes") && post.getCounty() != "All Counties") {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                        String hobby = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                        if (post.getCounty().equals(hobby)) {
                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else if (post.getTribe() != "All Tribes" && post.getCounty().equals("All Counties")) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                        String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                                        if (post.getTribe().equals(hobby)) {
                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else if (post.getTribe() != "All Tribes" && post.getCounty() != "All Counties") {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                        String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);
                                        String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                        if (post.getTribe().equals(hobby) && post.getCounty().equals(county)) {
                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                    String univ = teacherSnapshot.child(NodeNames.UNIV).getValue(String.class);
                                    if (post.getAudience().equals(univ)) {
                                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {

                                            postList.add(post);
                                            postList2.add(post);
                                            postAdapter.notifyDataSetChanged();

                                        } else if (post.getTribe().equals("All Tribes") && post.getCounty() != "All Counties") {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                                    String hobby = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                                    if (post.getCounty().equals(hobby)) {
                                                        postList.add(post);
                                                        postList2.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        } else if (post.getTribe() != "All Tribes" && post.getCounty().equals("All Counties")) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                                    String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                                                    if (post.getTribe().equals(hobby)) {
                                                        postList.add(post);
                                                        postList2.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        } else if (post.getTribe() != "All Tribes" && post.getCounty() != "All Counties") {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                                    String hobby = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);
                                                    String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                                                    if (post.getTribe().equals(hobby) && post.getCounty().equals(county)) {
                                                        postList.add(post);
                                                        postList2.add(post);
                                                        postAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });


                        }
                    }
                }
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
    private void VerifyUserExistence() {
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference();
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RootRef2.child("Users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(NodeNames.NAME).exists() && dataSnapshot.child(NodeNames.GENDER).exists() && dataSnapshot.child(NodeNames.AGE).exists()  && dataSnapshot.child(NodeNames.TRIBE).exists()  &&  dataSnapshot.child(NodeNames.UNIV).exists() && dataSnapshot.child(NodeNames.COUNTY).exists() && dataSnapshot.child(NodeNames.CONSTITUENCY).exists() && dataSnapshot.child(NodeNames.WARD).exists() && dataSnapshot.child(NodeNames.BIO).exists() && dataSnapshot.child(NodeNames.PHOTO).exists()   &&  dataSnapshot.child(NodeNames.APPROVAL).exists() && dataSnapshot.child(NodeNames.EXPIRY).exists() && dataSnapshot.child(NodeNames.NATID).exists()) {
                    progressDialog.cancel();
                    String approval = dataSnapshot.child(NodeNames.APPROVAL).getValue(String.class);
                    Date d1 = dataSnapshot.child(NodeNames.EXPIRY).getValue(Date.class);
                    Date d2=new java.util.Date();
                    long difference_In_Time = d1.getTime() - d2.getTime();

                    double exp = (difference_In_Time / (1000 * 60 * 60 * 24));

                    if (approval.equals(NodeNames.APPROVAL_PENDING) && exp  > 0) {




                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(d2.getTime());
                        Toast.makeText(getContext(), "INFO ! \t Your free posting trial will expire on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                        Intent mainIntent = new Intent(getContext(), AddPhotoActivity.class);
                         mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                         startActivity(mainIntent);



                    }
                    else if (approval.equals(NodeNames.APPROVAL_PENDING) && exp  <= 0)
                    {

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(d2.getTime());
                        Toast.makeText(getContext(), "EXPIRY! \t Your free posting trial  expired on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                        showExpiryDialog();

                    }


                    else if (exp > 0 && exp <= 5 && approval.equals(NodeNames.APPROVAL_ACTIVE))
                    {

                        //showExpiryDialog();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(d2.getTime());
                        Toast.makeText(getContext(), "ALERT! \t Your  posting subscription will expire on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                        Intent mainIntent = new Intent(getContext(), AddPhotoActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(mainIntent);

                    }
                    else if (exp <= 0 && approval.equals(NodeNames.APPROVAL_ACTIVE))
                    {


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(d2.getTime());
                        Toast.makeText(getContext(), "EXPIRY ! \t Your  posting subscription expired on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                        showExpiryDialog();
                    }



                }
                else
                {
                    progressDialog.cancel();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setCancelable(true);
                    builder1.setTitle("ALERT");
                    builder1.setMessage("Your Profile is incomplete. \n Complete your profile");

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                    Intent mainIntent = new Intent(getContext(), RegisterNameActivity.class);


                                    startActivity(mainIntent);

                                }
                            }).setNegativeButton(
                            "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(getContext(), "An Error has occurred kindly Try Again", Toast.LENGTH_LONG).show();

            }
        });
    }


    private void showExpiryDialog(){
        String title="ACTIVATE";
        String message="Sorry,your account subscription expired.Subscribe inorder to enjoy this service.We charge Ksh 100 only per month for this service. \n \n" +
                "HOW TO SUBSCRIBE \n\n" +
                "1.Send subscription  fee of ksh 100 to MPesa number 0707807623 (Japheth Nyakundi Nyarandi). \n " +
                "2.Send us the screen shot of the MPesa payment message and your phone number through our WhatsApp number +254707807623. \n \n" +
                " 3.Admin Japheth Nyakundi  will activate your account within 2 hrs.\n \n " +
                "For any queries kindly  WhatsApp admin +254707807623 ";
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("Alert !")
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        /*Intent mainIntent = new Intent(SignUpActivity.this, LaunchingActivity.class);
                        SignUpActivity.this.startActivity(mainIntent);
                        SignUpActivity.this.finish();

                         */
                    } })
                .build();
        android.view.View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        TextView tv2 = (TextView)view.findViewById(R.id.tv);



        tv2.setText(title);


        messageText.setText(message);

        dialog.show();
    }
}