package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView imageView1;
    TextView textView,textView2;
    Animation leftAnimation, rightAnimation;
    private final int SPLASH_DISPLAY_LENGTH = 10000;
    ArrayList<Place> places=new ArrayList<>();
    private DatabaseReference RootRef;
    FirebaseAuth authRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        imageView1 = findViewById(R.id.imageView1);
        textView=findViewById(R.id.tv);
        textView2=findViewById(R.id.tv2);

        leftAnimation = AnimationUtils.loadAnimation(this, R.anim.left_animation);
       rightAnimation = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        imageView1.setAnimation(rightAnimation);
       textView2.setAnimation(leftAnimation);
        textView.setAnimation(leftAnimation);


      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);



    }



}