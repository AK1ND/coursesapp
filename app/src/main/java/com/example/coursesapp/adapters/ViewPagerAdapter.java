package com.example.coursesapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coursesapp.fragments.homefragments.CatalogFragment;
import com.example.coursesapp.fragments.homefragments.HomeFragment;
import com.example.coursesapp.fragments.homefragments.NotificationsFragment;
import com.example.coursesapp.fragments.homefragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new CatalogFragment();
            case 2:
                return new NotificationsFragment();
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
