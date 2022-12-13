package com.example.coursesapp.fragments.adminfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.User;
import com.example.coursesapp.adapters.UsersListAdminAdapter;
import com.example.coursesapp.databinding.FragmentUsersListAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UsersListAdminFragment extends Fragment {

    private FragmentUsersListAdminBinding bind;
    private DatabaseReference databaseReference, user;
    private UsersListAdminAdapter adapter;
    private RecyclerView recyclerView;
    private boolean admin;
    ArrayList<User> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentUsersListAdminBinding.inflate(inflater, container, false);
        return bind.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = bind.recyclerViewUsers;


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        bind.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(requireContext()));


        bind.scrollUsersView.setOnRefreshListener(() -> {
            loadData();
            bind.scrollUsersView.setRefreshing(false);
        });

        loadData();
    }

    private void loadData() {

        list = new ArrayList<>();
        adapter = new UsersListAdminAdapter(requireContext(), list, "UsersListAdminFragment");
        recyclerView.setAdapter(adapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}