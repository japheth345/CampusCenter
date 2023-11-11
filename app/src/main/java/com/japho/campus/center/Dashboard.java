package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Model.ConnectivityUtils;
import com.japho.campus.center.chats.ChatFragment;
import com.japho.campus.center.findfriends.FindFriendsFragment;
import com.japho.campus.center.requests.RequestsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.View;
public class Dashboard extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btsoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        tabLayout = findViewById(R.id.tabMain);
        viewPager = findViewById(R.id.vpMain);
        btsoc = findViewById(R.id.social);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        DatabaseReference databaseReferenceUsers = FirebaseDatabase.getInstance().getReference()
                .child(NodeNames.USERS).child(firebaseAuth.getCurrentUser().getUid());

        databaseReferenceUsers.child(NodeNames.ONLINE).setValue("true");
        databaseReferenceUsers.child(NodeNames.ONLINE).onDisconnect().setValue("false");

        setViewPager();
 btsoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = ConnectivityUtils.isInternetConnected(getApplicationContext());
                if (isConnected) {
                    Intent mainIntent = new Intent(Dashboard.this, SMainActivity.class);
                    // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    Snackbar.make(Dashboard.this, v, "You are not connected to internet",  5000).show();
                }
            }
        });

    }

    class Adapter extends FragmentPagerAdapter{

        public Adapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {

                case 0:
                    ChatFragment chatFragment = new ChatFragment();
                    return  chatFragment;
              /*  case 1:
                    RequestsFragment requestsFragment = new RequestsFragment();
                    return  requestsFragment;
                case 2:
                    FindFriendsFragment findFriendsFragment = new FindFriendsFragment();
                    return  findFriendsFragment;

               */


            }
            return null;
        }

        @Override
        public int getCount() {
            return tabLayout.getTabCount();
        }
    }


    private void setViewPager(){

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_chat));
        //tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_requests));
       // tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_findfriends));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Adapter  adapter = new Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
         return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         int id = item.getItemId();

         if(id==R.id.mnuProfile)
         {
             Intent mainIntent = new Intent(Dashboard.this, UserProfileActivity.class);
             mainIntent.putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
             mainIntent.putExtra("visitor","me");
            startActivity(mainIntent);
         }

         return super.onOptionsItemSelected(item);
     }



    private  boolean doubleBackPressed = false;

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        if(tabLayout.getSelectedTabPosition()>0)
        {
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }
        else
        {
            if(doubleBackPressed)
            {
                finishAffinity();
            }
            else
            {
                doubleBackPressed=true;
                Toast.makeText(this, R.string.press_back_to_exit, Toast.LENGTH_SHORT).show();
                //delay
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPressed=false;
                    }
                }, 2000);

            }
        }
    }
}