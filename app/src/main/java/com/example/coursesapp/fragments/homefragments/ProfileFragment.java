package com.example.coursesapp.fragments.homefragments;

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

import com.bumptech.glide.Glide;
import com.example.coursesapp.HomeActivity;
import com.example.coursesapp.MainActivity;
import com.example.coursesapp.User;
import com.example.coursesapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private final String TAG = "profileFragmentLog";
    private FragmentProfileBinding bind;
    private ActivityResultLauncher<String> getPhoto;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference users;
    private Uri imageUri;

    private Boolean admin;
    private HomeActivity homeActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind = FragmentProfileBinding.inflate(inflater, container, false);

        homeActivity = (HomeActivity) getActivity();
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

        storageReference = FirebaseStorage.getInstance().getReference().child("userprofile/" + userID);





    }

    private void setUserName(){
        User user = new User();
        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("name");
        String name =  bind.etName.getText().toString();
        user.setName(name);
        user.setAdmin(admin);
        user.setEmail(bind.tvEmail.getText().toString());
        users.setValue(user);
        bind.tvName.setText(name);
    }


    private void buttonsClicks() {
        bind.textLogout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            startActivity(new Intent(requireActivity(), MainActivity.class));
        });

        bind.imageProfile.setOnClickListener(view -> getPhoto.launch("image/*"));

        bind.buttonAdminPanel.setOnClickListener(view -> {
            homeActivity.initViewPagerAdmin();
        });

        bind.etName.setOnLongClickListener(view -> {
            setUserName();
            return true;
        });
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
                        bind.etName.setText((CharSequence) task.getResult().getValue());
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


        users.child("admin")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error");
                    } else if (!((Boolean) task.getResult().getValue())) {
                        Log.d(TAG, "It's not admin");
                    } else {
                        admin = (Boolean) task.getResult().getValue();
                        bind.buttonAdminPanel.setVisibility(View.VISIBLE);
                    }
                });

        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Glide.with(requireContext())
                                .load(uri)
                                .into(bind.imageProfile)
                );
    }

}