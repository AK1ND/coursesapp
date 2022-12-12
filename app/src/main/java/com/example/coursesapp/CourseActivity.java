package com.example.coursesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;


import com.example.coursesapp.databinding.ActivityCourseBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class CourseActivity extends AppCompatActivity {

    private ActivityCourseBinding bind;
    private String idCourse = "", parentFragment , userID, TAG = "CourseActivityLog";

    private Task<Uri> storageReference;
    private DatabaseReference users, courses;

    private AlertDialog.Builder builder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        Intent intent = getIntent();
        idCourse = intent.getStringExtra("key");
        parentFragment = intent.getStringExtra("fromFragment");

        initVars();
        imageLoad();
        dataLoad();

        buttonsClicks();

    }


    private void initVars(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        users = firebaseDatabase.getReference("Users");
        courses = firebaseDatabase.getReference("Courses");

        userID = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).toString();
        String substr = "Users/";
        userID = userID.substring(userID.indexOf(substr) + substr.length());



    }

    private void uploadData(){

        Course course = new Course();

        course.setName(bind.caCourseName.getText().toString());
        course.setTheme(bind.caTheme.getText().toString());
        course.setDescription(bind.caDescription.getText().toString());
        course.setId(idCourse);

        users.child(userID).child("UserCourses").child(idCourse).setValue(course);

    }

    private void imageLoad(){
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("coursesimages/"+idCourse)
                .getDownloadUrl().addOnSuccessListener(uri -> {
                    bind.caImageView.setBackground(null);
                    Glide
                            .with(this)
                            .load(uri)
                            .into(bind.caImageView);
                });
    }

    private void dataLoad(){
        courses = courses.child(idCourse);

        if (parentFragment.equals("HomeFragment")){
            bind.buttonDeleteCourse.setVisibility(View.VISIBLE);
        }


        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .child("admin").get().addOnCompleteListener(task -> {
                   if (!task.isSuccessful()){
                       //Error
                   } else if (!(Boolean) task.getResult().getValue()){
                       //Not Admin
                   }
                   else {
                       //Admin
                       if (parentFragment.equals("HomeFragment")){
                           //Come from Home Fragment

                       } else {
                           //Come from CatalogFragment
                           bind.buttonDeleteCourseAdmin.setVisibility(View.VISIBLE);
                       }
                   }
                });

        courses.child("name")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Log.d(TAG, "Error");
                    } else {
                        bind.caCourseName.setText((CharSequence) task.getResult().getValue());
                    }
                });
        courses.child("theme")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Log.d(TAG, "Error");
                    } else {
                        bind.caTheme.setText((CharSequence) task.getResult().getValue());
                    }
                });
        courses.child("description")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Log.d(TAG, "Error");
                    } else {
                        bind.caDescription.setText((CharSequence) task.getResult().getValue());
                    }
                });

    }

    private void goToHomeActivity(){
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void createDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Message")
                .setTitle("Title")
                .setPositiveButton("Accept", (dialogInterface, i) -> {
                    courses.removeValue();
                    goToHomeActivity();
                })
                .setNegativeButton("Decline", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }



    private void buttonsClicks() {
        bind.buttonAddToHome.setOnClickListener(view -> {
            uploadData();
        });

        bind.caButtonClose.setOnClickListener(view -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        bind.buttonDeleteCourse.setOnClickListener(view -> {
            users.child(userID).child("UserCourses").child(idCourse).removeValue();
        });

        bind.buttonDeleteCourseAdmin.setOnClickListener(view -> {
            createDialog();
        });

        bind.buttonDeleteCourseAdmin.setOnLongClickListener(view -> {
            bind.buttonDeleteCourseAdmin.setVisibility(View.GONE);
            return true;
        });
    }



}