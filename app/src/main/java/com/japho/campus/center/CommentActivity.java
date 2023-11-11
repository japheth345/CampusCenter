package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.japho.campus.center.Adapter.CommentAdapter;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Model.User;
import com.japho.campus.center.R;
import com.japho.campus.center.Model.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private EditText addComment;
    private CircleImageView imageProfile;
    private TextView post;

    private String postId;
    private String authorId;

    FirebaseUser fUser;
 private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authorId = intent.getStringExtra("authorId");
         type = intent.getStringExtra("type");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList, postId);

        recyclerView.setAdapter(commentAdapter);

        addComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserImage();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addComment.getText().toString())) {
                    Toast.makeText(CommentActivity.this, "No comment added!", Toast.LENGTH_SHORT).show();
                } else {
                    putComment();
                }
            }
        });

        getComment();
    }

    private void getComment() {

        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void putComment() {

        HashMap<String, Object> map = new HashMap<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

        String id = ref.push().getKey();

        map.put("id", id);
        map.put("comment", addComment.getText().toString());
        map.put("publisher", fUser.getUid());

        addComment.setText("");

        ref.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    if(type.equals("text"))
                    {
                        addNotification(postId, authorId, "Commented on your text post", type);
                    }
                   else if(type.equals("photo"))
                    {
                        addNotification(postId, authorId, "Commented on your photo post", type);
                    }
                    else if(type.equals("video"))
                    {
                        addNotification(postId, authorId, "Commented on your video post", type);
                    }
                    else if(type.equals("audio"))
                    {
                        addNotification(postId, authorId, "Commented on your audio post", type);
                    }
                    else if(type.equals("ebook"))
                    {
                        addNotification(postId, authorId, "Commented on your ebook post", type);
                    }
                } else {
                    Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getUserImage() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
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
                    imageProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getPhoto()).into(imageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void addNotification(String postId, String publisherId,String message,String type)
    {
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

                    Toast.makeText(CommentActivity.this, "Comment added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
