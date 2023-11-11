package com.japho.campus.center.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.Dashboard;
import com.japho.campus.center.R;
import com.japho.campus.center.SMainActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.tabs.TabLayout;

public class HomeFragment2 extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
 private Context mContext;
    private ImageView close;
    public HomeFragment2() {
        // Required empty public constructor
    }


    public static HomeFragment2 newInstance() {
        HomeFragment2 fragment = new HomeFragment2();

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

View  view=inflater.inflate(R.layout.fragment_home2, container, false);
mContext=view.getContext();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext , Dashboard.class));

            }
        });
        SMainActivity activity = (SMainActivity) view.getContext();


        activity.setSupportActionBar(toolbar);
        activity. getSupportActionBar().setTitle("Socialize");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
       // setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext , Dashboard.class));

            }
        });
        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        setupViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SMainActivity activity = (SMainActivity) mContext;
        ViewPagerAdapter adapter = new ViewPagerAdapter( activity.getSupportFragmentManager());
        adapter.addFrag(new TextsFragment(), "Texts");
        adapter.addFrag(new PhotosFragment(), "Photos");
        adapter.addFrag(new VideosFragment(), "Videos");
        adapter.addFrag(new AudiosFragment(), "Audios");

        //adapter.addFrag(new BlogsFragment(), "Blogs");

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

