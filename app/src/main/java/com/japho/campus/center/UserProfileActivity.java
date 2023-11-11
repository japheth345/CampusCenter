package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import  com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Fragments.AudiosFragment;
import com.japho.campus.center.Fragments.AudiosFragment2;
import com.japho.campus.center.Fragments.HomeFragment2;
import com.japho.campus.center.Fragments.PersonalDetailsFragment;
import com.japho.campus.center.Fragments.PhotosFragment;
import com.japho.campus.center.Fragments.PhotosFragment2;
import com.japho.campus.center.Fragments.TextsFragment;
import com.japho.campus.center.Fragments.TextsFragment2;
import com.japho.campus.center.Fragments.VideosFragment;
import com.japho.campus.center.Fragments.VideosFragment2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView imageView, imageView2,imageView3;
    private ImageView visit_profile;
    private TextView visit_name;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String id;
    String desc;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        imageView = findViewById(R.id.su);
        imageView2 = findViewById(R.id.su2);
        imageView3 = findViewById(R.id.su3);
        visit_profile = findViewById(R.id.visit_profile_image);
        visit_name = findViewById(R.id.visit_user_name);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("userid") && bundle.containsKey("visitor")) {
            id = bundle.getString("userid");
            desc = bundle.getString("visitor");
            if (desc.equals("friend")) {
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                // imageView2.setVisibility(View.INVISIBLE);
                //imageView2.setVisibility(View.INVISIBLE);
            }

            ref = FirebaseDatabase.getInstance().getReference();


            ref.child("Users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {
                    if (teacherSnapshot.exists() && teacherSnapshot.hasChild("name") && teacherSnapshot.hasChild("image")) {
                        String retrieveusername = teacherSnapshot.child("name").getValue().toString();
                        String retrieveuserstatus = teacherSnapshot.child("bio").getValue().toString();
                        String retrieveuserimage = teacherSnapshot.child("image").getValue().toString();


                        visit_name.setText(retrieveusername);

                        Picasso.get().load(retrieveuserimage).into(visit_profile);
                        setupViewPager(viewPager, id, desc);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.equals("friend")) {
                    Intent mainIntent = new Intent(UserProfileActivity.this, SMainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(mainIntent);
                    finish();

                } else {
                    Intent mainIntent = new Intent(UserProfileActivity.this, Dashboard.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(mainIntent);
                    finish();
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.setEdit(1);
                Intent mainIntent = new Intent(UserProfileActivity.this, RegisterNameActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(mainIntent);
                finish();

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(UserProfileActivity.this, LoginActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(mainIntent);
                finish();

            }
        });
    }
    private void setupViewPager(ViewPager viewPager,String id,String desc) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PersonalDetailsFragment(id,desc), "Details");
        adapter.addFrag(new TextsFragment2(id), "Texts");
        adapter.addFrag(new PhotosFragment2(id), "Photos");
        adapter.addFrag(new VideosFragment2(id), "Videos");
        adapter.addFrag(new AudiosFragment2(id), "Audios");



        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
