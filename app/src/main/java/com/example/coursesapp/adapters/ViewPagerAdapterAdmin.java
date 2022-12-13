package com.example.coursesapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coursesapp.fragments.adminfragments.CreateCourseAdminFragment;
import com.example.coursesapp.fragments.adminfragments.UsersListAdminFragment;
import com.example.coursesapp.fragments.homefragments.ProfileFragment;

public class ViewPagerAdapterAdmin extends FragmentStateAdapter {


    public ViewPagerAdapterAdmin(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UsersListAdminFragment();
            case 1:
                return new CreateCourseAdminFragment();
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
