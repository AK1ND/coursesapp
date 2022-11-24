package com.example.coursesapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursesapp.databinding.ActivityHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {


    private ActivityHomeBinding bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        bind.viewPager2.setAdapter(adapter);

        new TabLayoutMediator(bind.tabs, bind.viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    }
                }).attach();

        bind.tabs.getTabAt(0).setIcon(R.drawable.home_24);
        bind.tabs.getTabAt(1).setIcon(R.drawable.catalog_24);
        bind.tabs.getTabAt(2).setIcon(R.drawable.notifications_24);
        bind.tabs.getTabAt(3).setIcon(R.drawable.profile_24);


//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_homeActivity, new HomeFragment()).commit();


    }
}