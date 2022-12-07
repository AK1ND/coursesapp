package com.example.coursesapp.fragments.homefragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.example.coursesapp.databinding.FragmentCatalogBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class CatalogFragment extends Fragment {


    private FragmentCatalogBinding bind;
    private DatabaseReference databaseReference, user;
    private CatalogAdapter adapter;
    private RecyclerView recyclerView;
    private boolean admin;
    ArrayList<Course> list;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentCatalogBinding.inflate(inflater, container, false);
        return bind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = bind.recyclerViewCatalog;
        checkAdmin();


        databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        recyclerView.setHasFixedSize(true);
        bind.recyclerViewCatalog.setLayoutManager(new LinearLayoutManager(requireContext()));


        bind.scrollView.setOnRefreshListener(() -> {
            loadData();
            bind.scrollView.setRefreshing(false);
        });


        loadData();
    }

    private void checkAdmin(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        user = firebaseDatabase.getReference("Users");
        user = user.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        user.child("admin")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        admin = false;
                        Log.d("ADMINCHECK", String.valueOf(admin));
                    } else if (!((Boolean) task.getResult().getValue())) {
                        admin = false;
                        Log.d("ADMINCHECK", String.valueOf(admin));
                    } else {
                        admin = true;
                        Log.d("ADMINCHECK", String.valueOf(admin));
                    }
                });
    }


    private void loadData() {

        list = new ArrayList<>();
        adapter = new CatalogAdapter(requireContext(), list, "CatalogFragment");
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