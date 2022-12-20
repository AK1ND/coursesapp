package com.example.coursesapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coursesapp.fragments.adminfragments.CreateCourseAdminFragment;
import com.example.coursesapp.fragments.adminfragments.UsersListAdminFragment;

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
            default:
                return new CreateCourseAdminFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
