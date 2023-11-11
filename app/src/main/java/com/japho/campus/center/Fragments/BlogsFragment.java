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
import com.japho.campus.center.Adapter.EbooksAdapter;
import com.japho.campus.center.Adapter.ImagesAdapter;
import com.japho.campus.center.Adapter.PhotosAdapter;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BlogsFragment extends Fragment {
    private RecyclerView recyclerViewPosts;
    private EbooksAdapter postAdapter;
    private List<Post> postList;

    private List<String> followingList;
    public BlogsFragment() {
        // Required empty public constructor
    }


    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_blogs, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new EbooksAdapter(getContext(), postList);
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
                    if (post.getType().equals("ebook")) {

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
                        } else if (post.getAudience().equals("All Universities")) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {

                                postList.add(post);
                                postAdapter.notifyDataSetChanged();

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
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {


                                    String univ = teacherSnapshot.child(NodeNames.UNIV).getValue(String.class);
                                    if (post.getAudience().equals(univ)) {
                                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        if (post.getTribe().equals("All Tribes") && post.getCounty().equals("All Counties")) {

                                            postList.add(post);
                                            postAdapter.notifyDataSetChanged();

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


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}