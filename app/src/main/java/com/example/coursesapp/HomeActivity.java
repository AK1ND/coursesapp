package com.example.coursesapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursesapp.Adapters.ViewPagerAdapter;
import com.example.coursesapp.Adapters.ViewPagerAdapterAdmin;
import com.example.coursesapp.databinding.ActivityHomeBinding;

import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {


    private ActivityHomeBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initViewPager();
    }


    public void initViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        bind.viewPager2.setAdapter(adapter);

        new TabLayoutMediator(bind.tabs, bind.viewPager2,
                (tab, position) -> {
                }).attach();

        bind.tabs.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        bind.tabs.getTabAt(1).setIcon(R.drawable.ic_dashboard_black_24dp);
        bind.tabs.getTabAt(2).setIcon(R.drawable.profile_24);
    }

    public void initViewPagerAdmin() {
        ViewPagerAdapterAdmin adapter = new ViewPagerAdapterAdmin(this);

        bind.viewPager2.setAdapter(adapter);

        new TabLayoutMediator(bind.tabs, bind.viewPager2,
                (tab, position) -> {
                }).attach();

        bind.tabs.getTabAt(1).setIcon(R.drawable.ic_dashboard_black_24dp);
        bind.tabs.getTabAt(0).setIcon(R.drawable.profile_24);

    }
}