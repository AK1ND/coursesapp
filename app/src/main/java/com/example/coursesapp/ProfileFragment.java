package com.example.coursesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursesapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    String TAG = "profileFragmentLog";
    ActivityResultLauncher<String> getPhoto;
    private FragmentProfileBinding bind;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference users;
    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind = FragmentProfileBinding.inflate(inflater, container, false);
        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    imageUri = result;
                    bind.imageProfile.setImageURI(result);
                    uploadImg();

                }
        );
        return bind.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initVars();


        getData();
        buttonsClicks();
    }

    private void initVars() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        users = firebaseDatabase.getReference("Users");


        String userID = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).toString();
        String substr = "Users/";
        userID = userID.substring(userID.indexOf(substr) + substr.length());
        Log.d(TAG, "initVars:" + userID);
        storageReference = FirebaseStorage.getInstance().getReference().child(userID);

    }


    private void buttonsClicks() {
        bind.textLogout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            startActivity(new Intent(requireActivity(), MainActivity.class));
        });

        bind.imageProfile.setOnClickListener(view -> getPhoto.launch("image/*"));
    }


    private void uploadImg() {
        if (imageUri != null) {
            storageReference.putFile(imageUri);
        } else {
            Log.d(TAG, "initVars: IMG NULL");
        }
    }


    private void getData() {

        users = users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());


        users.child("name")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {

                        Log.d(TAG, "Error");

                    } else {
                        bind.tvName.setText((CharSequence) task.getResult().getValue());
                    }
                });
        users.child("email")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error");

                    } else {
                        bind.tvEmail.setText((CharSequence) task.getResult().getValue());
                    }
                });

        storageReference.getBytes(512 * 512)
                .addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bind.imageProfile.setImageBitmap(bitmap);
                });
    }

}