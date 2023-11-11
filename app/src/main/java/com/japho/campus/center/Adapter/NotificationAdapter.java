package com.japho.campus.center.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Fragments.AudioPostDetailFragment;
import com.japho.campus.center.Fragments.EbookPostDetailFragment;
import com.japho.campus.center.Fragments.PhotoPostDetailFragment;
import com.japho.campus.center.Fragments.TextPostDetailFragment;
import com.japho.campus.center.Fragments.VideoPostDetailFragment;
import com.japho.campus.center.Model.Post;
import com.japho.campus.center.Model.User;
import com.japho.campus.center.Fragments.PostDetailFragment;
import com.japho.campus.center.Fragments.ProfileFragment;
import com.japho.campus.center.Model.Notification;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import com.japho.campus.center.R;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Notification notification = mNotifications.get(position);

        getUser(holder.imageProfile, holder.username, notification.getUserid());
        holder.comment.setText(notification.getText());

        if (notification.isIsPost()) {
            holder.postImage.setVisibility(View.VISIBLE);
            getPostImage(holder.postImage, notification.getPostid());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isIsPost())
                {
                    if(notification.getType().equals("text"))
                    {
                        mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                                .edit().putString("postid", notification.getPostid()).apply();

                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragment_container, new TextPostDetailFragment()).commit();
                    }
                    else if(notification.getType().equals("photo"))
                    {
                        mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                                .edit().putString("postid", notification.getPostid()).apply();

                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragment_container, new PhotoPostDetailFragment()).commit();
                    }
                    else if(notification.getType().equals("video"))
                    {
                        mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                                .edit().putString("postid", notification.getPostid()).apply();

                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragment_container, new VideoPostDetailFragment()).commit();
                    }
                    else if(notification.getType().equals("audio"))
                    {
                        mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                                .edit().putString("postid", notification.getPostid()).apply();

                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragment_container, new AudioPostDetailFragment()).commit();
                    }
                    else if(notification.getType().equals("ebook"))
                    {
                        mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                                .edit().putString("postid", notification.getPostid()).apply();

                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragment_container, new EbookPostDetailFragment()).commit();
                    }
                }

                    else
                {
                    mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                            .edit().putString("profileId", notification.getUserid()).apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.fragment_container, new ProfileFragment(notification.getUserid())).commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageProfile;
        public ImageView postImage;
        public TextView username;
        public TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    private void getPostImage(final ImageView imageView, String postId) {
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String description=dataSnapshot.child("description").getValue(String.class);
                String imageurl=dataSnapshot.child("imageurl").getValue(String.class);
                String postid=dataSnapshot.child("postid").getValue(String.class);
                String publisher=dataSnapshot.child("publisher").getValue(String.class);
                String audience=dataSnapshot.child("audience").getValue(String.class);
                String visible=dataSnapshot.child("visible").getValue(String.class);
                String tribe=dataSnapshot.child("tribe").getValue(String.class);
                String county=dataSnapshot.child("county").getValue(String.class);
                String type=dataSnapshot.child("type").getValue(String.class);
                Date expiry = dataSnapshot.child(NodeNames.EXPIRY).getValue(Date.class);
                Post post = new Post(description,imageurl,postid,publisher,audience,visible,tribe,county,type,expiry);
                if(post.getType().equals("text"))
                {
                    Picasso.get().load(post.getImageurl()).placeholder(R.drawable.texts).into(imageView);
                }
               else if(post.getType().equals("video"))
                {
                    Picasso.get().load(post.getImageurl()).placeholder(R.drawable.videos).into(imageView);
                }
                if(post.getType().equals("audio"))
                {
                    Picasso.get().load(post.getImageurl()).placeholder(R.drawable.audios).into(imageView);
                }
                if(post.getType().equals("ebook"))
                {
                    Picasso.get().load(post.getImageurl()).placeholder(R.drawable.blogs).into(imageView);
                }
                if(post.getType().equals("photo"))
                {
                    Picasso.get().load(post.getImageurl()).placeholder(R.drawable.images).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUser(final ImageView imageView, final TextView textView, String userId) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {
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
                if (user.getPhoto().equals("default")) {
                    imageView.setImageResource(R.drawable.default_photo);
                } else {
                    Picasso.get().load(user.getPhoto()).into(imageView);
                }
                textView.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
