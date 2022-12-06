package com.example.coursesapp.fragments.adminfragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursesapp.Course;
import com.example.coursesapp.databinding.FragmentCreateCourseBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;


public class CreateCourseAdminFragment extends Fragment {


    private final String TAG = "CreateCourseLog";
    private FragmentCreateCourseBinding bind;
    private ActivityResultLauncher<String> getPhoto;
    private StorageReference storageReference;
    private DatabaseReference courses;
    private Uri imageUri;

    private String theme = "";
    private String idCourse = "";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind = FragmentCreateCourseBinding.inflate(inflater, container, false);

        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    imageUri = result;
                    bind.imgviewCourse.setImageURI(result);
                    bind.imgviewCourse.setBackground(null);
                }
        );

        return bind.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initVars();

        buttonsClicks();
    }

    private void buttonsClicks() {
        bind.buttonAddCourse.setOnClickListener(view -> {
            createCourse();
        });

        bind.imgviewCourse.setOnClickListener(view -> {
            getPhoto.launch("image/*");
        });
    }

    private Boolean checkFillFields() {
        String courseName = bind.etCourseName.getText().toString().trim();
        theme = bind.spinner.getSelectedItem().toString();
        String description = bind.etDescription.getText().toString().trim();
        idCourse = theme + Calendar.getInstance().getTimeInMillis();


        if (TextUtils.isEmpty(courseName) || TextUtils.isEmpty(description)) {
            return false;
        } else {
            return true;
        }

    }


    private void initVars() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        courses = firebaseDatabase.getReference("Courses");

    }

    private void uploadImg() {
        if (imageUri != null) {
            storageReference.putFile(imageUri);
        } else {
            Log.d(TAG, "initVars: IMG NULL");
        }
    }


    private void uploadData() {


        Course course = new Course();

        course.setName(bind.etCourseName.getText().toString());
        course.setId(idCourse);
        course.setTheme(bind.spinner.getSelectedItem().toString());
        course.setDescription(bind.etDescription.getText().toString());

        courses.child(idCourse)
                .setValue(course);


    }


    private void createCourse() {

        theme = bind.spinner.getSelectedItem().toString();
        idCourse = theme + Calendar.getInstance().getTimeInMillis();

        storageReference = FirebaseStorage.getInstance().getReference().child("coursesimages/" + idCourse);

        if (checkFillFields()) {
            uploadImg();
            uploadData();
        } else {
            Toast.makeText(requireContext(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
        }

    }

}