package com.japho.campus.center.Adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.japho.campus.center.Common.Extras;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Dashboard;
import com.japho.campus.center.Fragments.AudioPostDetailFragment;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.Model.User;
import com.japho.campus.center.CommentActivity;
import com.japho.campus.center.FollowersActivity;
import com.japho.campus.center.Fragments.PostDetailFragment;
import com.japho.campus.center.Fragments.ProfileFragment;
import com.japho.campus.center.R;
import com.japho.campus.center.UserProfileActivity;
import com.japho.campus.center.chats.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
public class AudiosAdapter extends RecyclerView.Adapter<AudiosAdapter.Viewholder> {

    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    private SimpleExoPlayer exoPlayer2;
    public AudiosAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.audios_layout, parent, false);

        return new AudiosAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        final Post post = mPosts.get(position);

        holder.description.setText(post.getDescription());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                            .edit().putString("postid", post.getPostid()).apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.fragment_container, new AudioPostDetailFragment()).commit();
                }});

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
                    holder.imageProfile.setImageResource(R.drawable.default_photo);
                    holder.username.setText("Name Hidden");
                    if(post.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || post.getVisible().equals("Yes"))
                    {
                        holder.chat.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (user.getPhoto().equals("default")) {
                        holder.imageProfile.setImageResource(R.drawable.default_photo);
                    } else {
                        Picasso.get().load(user.getPhoto()).placeholder(R.drawable.default_photo).into(holder.imageProfile);
                    }
                    holder.username.setText(user.getName());

                    if(post.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || post.getVisible().equals("Yes"))
                    {
                        holder.chat.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.noOfLikes);
        getComments(post.getPostid(), holder.noOfComments);
        isSaved(post.getPostid(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                    addNotification(post.getPostid(),post.getPublisher(),"Liked your Audio  post","audio");
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                intent.putExtra("type", "audio");
                mContext.startActivity(intent);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Extras.USER_KEY,post.getPublisher());

                mContext.startActivity(intent);
            }
        });
        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                intent.putExtra("type", "audio");
                mContext.startActivity(intent);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                }
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
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

        holder.username.setOnClickListener(new View.OnClickListener() {
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






        holder.noOfLikes.setOnClickListener(new View.OnClickListener() {
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
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;


        public ImageView like;
        public ImageView comment;
        public ImageView chat;
        public ImageView save;
        public ImageView more;
        private Button play;
        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        SocialTextView description;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            play =itemView.findViewById(R.id.play);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            chat = itemView.findViewById(R.id.chat);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);

            noOfComments = itemView.findViewById(R.id.no_of_comments);
            description = itemView.findViewById(R.id.description);

        }

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



}