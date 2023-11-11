package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.japho.campus.center.Fragments.AudiosFragment;
import  com.japho.campus.center.Fragments.HomeFragment;
import com.japho.campus.center.Fragments.HomeFragment2;
import com.japho.campus.center.Fragments.NotificationFragment;
import com.japho.campus.center.Fragments.PhotosFragment;
import  com.japho.campus.center.Fragments.ProfileFragment;
import  com.japho.campus.center.Fragments.SearchFragment;
import com.japho.campus.center.Fragments.TextsFragment;
import com.japho.campus.center.Fragments.VideosFragment;

public class SMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    private BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smain);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomSheetDialog = new BottomSheetDialog(SMainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.postfilesoptions, null);
        view.findViewById(R.id.lTexts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {bottomSheetDialog.dismiss();
                startActivity(new Intent(SMainActivity.this , AddTextActivity.class));
                finish();
            }
        });
        view.findViewById(R.id.lPhotos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(SMainActivity.this , AddPhotoActivity.class));
                finish();
            }
        });
        view.findViewById(R.id.lVideos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(SMainActivity.this , AddVideoActivity.class));
                finish();
            }
        });
        view.findViewById(R.id.lAudios).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(SMainActivity.this , AddAudioActivity.class));
                finish();
            }
        });
        /*view.findViewById(R.id.lBlogs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(SMainActivity.this , AddEbookActivity.class));
                finish();
            }
        });

         */

view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();
               // selectorFragment = new HomeFragment2();
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();

            }
        });
        bottomSheetDialog.setContentView(view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home :
                        selectorFragment = new TextsFragment();
                        break;

                    case R.id.nav_search :
                        selectorFragment = new PhotosFragment();
                        break;

                    case R.id.nav_add :
                        selectorFragment =new VideosFragment();
                       /* //startActivity(new Intent(SMainActivity.this , PostActivity.class));
                        if (ActivityCompat.checkSelfPermission(SMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            if (bottomSheetDialog != null)
                                bottomSheetDialog.show();

                        */


                        break;

                    case R.id.nav_heart :
                        selectorFragment = new NotificationFragment();
                        break;

                    case R.id.nav_profile :
                        selectorFragment = new AudiosFragment();
                        //selectorFragment = new ProfileFragment(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        break;
                }

                if (selectorFragment != null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();

                }
                else
                {
                    selectorFragment = new NotificationFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }

                return  true;

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new NotificationFragment()).commit();
       /* Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment(profileId)).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment2()).commit();
        }

        */
    }
}
