package com.example.coursesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursesapp.HomeActivity;
import com.example.coursesapp.R;
import com.example.coursesapp.User;
import com.example.coursesapp.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding bind;

    private FirebaseAuth fbAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference users;


    private ProgressDialog progressDialog;

    private String email = "", password = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentSignUpBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return bind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fbAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://courses-app-7fc2b-default-rtdb.europe-west1.firebasedatabase.app");
        users = firebaseDatabase.getReference("Users");

        progressDialog();

        buttonsClicks();
    }


    private void progressDialog() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account");
        progressDialog.setCanceledOnTouchOutside(false);
    }


    private void buttonsClicks() {
        bind.textLogInSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogInFragment()).commit();
            }
        });

        bind.imgButtonCloseSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
            }
        });

        bind.buttonLogInFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        email = bind.etEmailSignUpFragment.getText().toString().trim();
        password = bind.etPasswordSighUpFragment.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            bind.etEmailSignUpFragment.setError("Invalid email format");
        } else if (TextUtils.isEmpty(password)) {
            bind.etPasswordSighUpFragment.setHint("Enter password");
        } else if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password 6+ sm", Toast.LENGTH_SHORT).show();
        } else {
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {

        progressDialog.show();
        fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();

                        User user = new User();
                        user.setName(bind.etNameSignUpFragment.getText().toString());
//                        user.setEmail(bind.etEmailSignUpFragment.getText().toString());

                        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user);


                        Toast.makeText(requireContext(), "Account created " + email, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(requireActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}