package com.example.coursesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.coursesapp.data.User;
import com.example.coursesapp.databinding.ActivityEditUserBinding;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class EditUserActivity extends AppCompatActivity {

    private ActivityEditUserBinding bind;
    private String  userID, parentFragment, TAG = "EditUserActivityLog";
    private Boolean admin;

    private Task<Uri> storageReference;
    private DatabaseReference users;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityEditUserBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        Intent intent = getIntent();
        userID = intent.getStringExtra("key");
        parentFragment = intent.getStringExtra("fromFragment");

        initVars();
        imageLoad();
        dataLoad();

        buttonsClicks();
    }

    private void initVars(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        users = firebaseDatabase.getReference("Users");

        userID = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).toString();
        String substr = "Users/";
        userID = userID.substring(userID.indexOf(substr) + substr.length());
    }

    private void imageLoad(){
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("userprofile/"+userID)
                .getDownloadUrl().addOnSuccessListener(uri -> {
                    bind.euImageView.setBackground(null);
                    Glide
                            .with(this)
                            .load(uri)
                            .into(bind.euImageView);
                });
    }

    private void setUserEdit(){
        User user = new User();
        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("name");
        String name =  bind.edName.getText().toString();
        String email =  bind.edEmail.getText().toString();
        boolean key = false;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            bind.edEmail.setError("Invalid email format");
        } else if (TextUtils.isEmpty(name)) {
            bind.edName.setError("Enter name");
        } else {
            createEditDialog();
            user.setName(name);
            user.setAdmin(admin);
            user.setEmail(email);
            users.setValue(user);
            bind.tvEdname.setText(name);
            bind.tvEdemail.setText(email);
            startActivity(new Intent(this, HomeActivity.class));
            Toast.makeText(getApplicationContext(), "Profile edited", Toast.LENGTH_SHORT).show();
        }
    }

    private void dataLoad(){
        users = users.child(userID);

        users.child("admin")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error");
                    } else if (!((Boolean) task.getResult().getValue())) {
                        Log.d(TAG, "It's not admin");
                    } else {
                        admin = (Boolean) task.getResult().getValue();
                    }
                });


        users.child("name")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Log.d(TAG, "Error");
                    } else {
                        bind.edName.setText((CharSequence) task.getResult().getValue());
                        bind.tvEdname.setText((CharSequence) task.getResult().getValue());
                    }
                });
        users.child("email")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Log.d(TAG, "Error");
                    } else {
                        bind.edEmail.setText((CharSequence) task.getResult().getValue());
                        bind.tvEdemail.setText((CharSequence) task.getResult().getValue());
                    }
                });
    }

    private void goToHomeActivity(){
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void createEditDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to edit your profile?")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    setUserEdit();
                    dialogInterface.cancel();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void buttonsClicks() {
        bind.buttonSave.setOnClickListener(view -> {
            createEditDialog();
        });

        bind.euImageView.setOnClickListener(view -> imageLoad());

        bind.edButtonClose.setOnClickListener(view -> {
            startActivity(new Intent(this, HomeActivity.class));
        });


    }




}