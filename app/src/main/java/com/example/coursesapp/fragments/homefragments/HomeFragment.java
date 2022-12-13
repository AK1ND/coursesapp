package com.example.coursesapp.fragments.homefragments;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.coursesapp.Course;
import com.example.coursesapp.adapters.CatalogAdapter;
import com.example.coursesapp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding bind;
    private DatabaseReference databaseReference, users;
    private CatalogAdapter adapter;
    private RecyclerView recyclerView;
    private String userID;
    ArrayList<Course> list;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentHomeBinding.inflate(inflater, container, false);
        return bind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserID();

        recyclerView = bind.recyclerViewHome;
        databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                                .child(userID)
                                        .child("UserCourses");
        recyclerView.setHasFixedSize(true);
        bind.recyclerViewHome.setLayoutManager(new LinearLayoutManager(requireContext()));


        bind.scrollCatalogView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                bind.scrollCatalogView.setRefreshing(false);
            }
        });


        loadData();
    }

    private void getUserID(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        users = firebaseDatabase.getReference("Users");

        userID = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).toString();
        String substr = "Users/";
        userID = userID.substring(userID.indexOf(substr) + substr.length());
    }


    private void loadData() {

        list = new ArrayList<>();
        adapter = new CatalogAdapter(requireContext(), list, "HomeFragment");
        recyclerView.setAdapter(adapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Course course = dataSnapshot.getValue(Course.class);
                    list.add(course);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}