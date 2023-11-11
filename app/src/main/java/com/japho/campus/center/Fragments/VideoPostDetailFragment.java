package com.japho.campus.center.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.japho.campus.center.Adapter.AudiosAdapter;
import com.japho.campus.center.CommentActivity;
import com.japho.campus.center.Common.Extras;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.Dashboard;
import com.japho.campus.center.FollowersActivity;
import com.japho.campus.center.Model.User;
import com.japho.campus.center.R;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japho.campus.center.Adapter.VideosAdapter;
import com.japho.campus.center.R;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.japho.campus.center.Adapter.ImagesAdapter;
import com.japho.campus.center.Adapter.PhotosAdapter;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.R;
import com.japho.campus.center.SMainActivity;
import com.japho.campus.center.UserProfileActivity;
import com.japho.campus.center.chats.ChatActivity;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VideoPostDetailFragment extends Fragment {

    private String postId;
    public ImageView imageProfile;

    SimpleExoPlayerView exoPlayerView;
    public ImageView like;
    public ImageView comment;
    public ImageView chat;
    public ImageView save;
    public ImageView more;

    public TextView username;
    public TextView noOfLikes;
    public TextView author;
    public TextView noOfComments;
    SocialTextView description;
    SimpleExoPlayer exoPlayer;
    private Context mContext;
    private FirebaseUser firebaseUser;
    //private ProgressDialog progressDialog;
    private ImageView imageView,imageView2;
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
        View itemView = inflater.inflate(R.layout.fragment_audio_post_detail, container, false);

        postId = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");

        imageProfile = itemView.findViewById(R.id.image_profile);
        exoPlayerView = itemView.findViewById(R.id.post_image);
        exoPlayerView.setControllerShowTimeoutMs(0);
        exoPlayerView.setControllerHideOnTouch(false);
        like = itemView.findViewById(R.id.like);
        comment = itemView.findViewById(R.id.comment);
        chat = itemView.findViewById(R.id.chat);
        save = itemView.findViewById(R.id.save);
        more = itemView.findViewById(R.id.more);

        username = itemView.findViewById(R.id.username);
        noOfLikes = itemView.findViewById(R.id.no_of_likes);
        imageView2=itemView.findViewById(R.id.addBack);
        noOfComments = itemView.findViewById(R.id.no_of_comments);
        description = itemView.findViewById(R.id.description);
        mContext = itemView.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /*
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

         */
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (exoPlayer != null) {
                    exoPlayer.release();
                    exoPlayer = null;
                }
                //
                // progressDialog.cancel();
                Intent mainIntent = new Intent(getContext(), SMainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(mainIntent);

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String description2=dataSnapshot.child("description").getValue(String.class);
                String imageurl=dataSnapshot.child("imageurl").getValue(String.class);
                String postid=dataSnapshot.child("postid").getValue(String.class);
                String publisher=dataSnapshot.child("publisher").getValue(String.class);
                String audience=dataSnapshot.child("audience").getValue(String.class);
                String visible=dataSnapshot.child("visible").getValue(String.class);
                String tribe=dataSnapshot.child("tribe").getValue(String.class);
                String county=dataSnapshot.child("county").getValue(String.class);
                String type=dataSnapshot.child("type").getValue(String.class);
                Date expiry = dataSnapshot.child("exp").getValue(Date.class);
                Post post = new Post(description2,imageurl,postid,publisher,audience,visible,tribe,county,type,expiry);

                description.setText(post.getDescription());
                Uri uri =  Uri.parse(post.getImageurl());

                try {


                    // BandwidthMeter is used for
                    // getting default bandwidth
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                    // track selector is used to navigate between
                    // video using a default seekbar.
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

                    // we are adding our track selector to exoplayer.
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

                    // we are parsing a video url
                    // and parsing its video uri.
                    //Uri videouri = Uri.parse(uri);

                    // we are creating a variable for datasource factory
                    // and setting its user agent as 'exoplayer_view'
                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                    // we are creating a variable for extractor factory
                    // and setting it to default extractor factory.
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


                    MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);


                    exoPlayerView.setPlayer(exoPlayer);

                    exoPlayer.prepare(mediaSource);


                    exoPlayer.setPlayWhenReady(false);









                }
                catch (Exception e) {
                    // below line is used for
                    // handling our errors.
                    Toast.makeText(mContext, "Error playing the Audio \n"+e.toString(), Toast.LENGTH_LONG).show();

                }
                exoPlayer.addListener(new Player.EventListener() {
                    @Override
                    public void onIsPlayingChanged(boolean isPlaying) {
                        // This method is called when the player's state changes
                        if (isPlaying) {
                            //progressDialog.cancel();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {
                        String UserID = teacherSnapshot.child(NodeNames.USERID).getValue(String.class);
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


                        User user = new User(UserID, name, natid, email, gender, age, dateofb, dob, mob, yob, tribe, hobby, county, consti, ward, bio, photo, status, approval, expiry);
                        if (post.getVisible().equals("Yes")) {
                            imageProfile.setImageResource(R.drawable.default_photo);
                            username.setText("Name Hidden");
                            if(post.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || post.getVisible().equals("Yes"))
                            {
                                chat.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            if (user.getPhoto().equals("default")) {
                                imageProfile.setImageResource(R.drawable.default_photo);
                            } else {
                                Picasso.get().load(user.getPhoto()).placeholder(R.drawable.default_photo).into(imageProfile);
                            }
                            username.setText(user.getName());

                            if(post.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || post.getVisible().equals("Yes"))
                            {
                                chat.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                isLiked(post.getPostid(), like);
                noOfLikes(post.getPostid(), noOfLikes);
                getComments(post.getPostid(), noOfComments);
                isSaved(post.getPostid(), save);

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (like.getTag().equals("like")) {
                            FirebaseDatabase.getInstance().getReference().child("Likes")
                                    .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                            addNotification(post.getPostid(),post.getPublisher(),"Liked your Audio  post","audio");
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Likes")
                                    .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                        }
                    }
                });

                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CommentActivity.class);
                        intent.putExtra("postId", post.getPostid());
                        intent.putExtra("authorId", post.getPublisher());
                        intent.putExtra("type", "audio");
                        mContext.startActivity(intent);
                    }
                });
                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra(Extras.USER_KEY,post.getPublisher());

                        mContext.startActivity(intent);
                    }
                });
                noOfComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CommentActivity.class);
                        intent.putExtra("postId", post.getPostid());
                        intent.putExtra("authorId", post.getPublisher());
                        intent.putExtra("type", "audio");
                        mContext.startActivity(intent);
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (save.getTag().equals("save")) {
                            FirebaseDatabase.getInstance().getReference().child("Saves")
                                    .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Saves")
                                    .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                        }
                    }
                });

                imageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (post.getVisible().equals("Yes"))
                        {
                            Toast.makeText(mContext, "Cant view profile it is hidden by user", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent mainIntent = new Intent(mContext, UserProfileActivity.class);
                            mainIntent.putExtra("userid",post.getPublisher());
                            mainIntent.putExtra("visitor","friend");
                            mContext.startActivity(mainIntent);

                   /* mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                            .edit().putString("profileId", post.getPublisher()).apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment(post.getPublisher())).commit();

                    */
                        }
                    }
                });

                username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (post.getVisible().equals("Yes")) {
                            Toast.makeText(mContext, "Cant view profile it is hidden by user", Toast.LENGTH_LONG).show();
                        } else {
                            Intent mainIntent = new Intent(mContext, UserProfileActivity.class);
                            mainIntent.putExtra("userid",post.getPublisher());
                            mainIntent.putExtra("visitor","friend");
                            mContext.startActivity(mainIntent);
                   /* mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                            .edit().putString("profileId", post.getPublisher()).apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment(post.getPublisher())).commit();

                    */
                        }
                    }});






                noOfLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FollowersActivity.class);
                        intent.putExtra("id", post.getPostid());
                        intent.putExtra("title", "likes");
                        mContext.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return itemView;
    }

    private void isSaved (final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    image.setImageResource(R.drawable.ic_save_black);
                    image.setTag("saved");
                } else {
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfLikes (String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments (String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addNotification(String postId, String publisherId,String message,String type) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", publisherId);
        map.put("text", message);
        map.put("postid", postId);
        map.put("isPost", true);
        map.put("type",type);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisherId).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {

                    Toast.makeText(mContext, "Like successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        // Stop any ongoing activities here
    }
    @Override
    public void onStop() {
        super.onStop();
        // Stop any ongoing activities here
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

}