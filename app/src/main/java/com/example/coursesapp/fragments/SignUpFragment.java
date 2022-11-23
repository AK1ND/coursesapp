package com.example.coursesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursesapp.HomeActivity;
import com.example.coursesapp.MainActivity;
import com.example.coursesapp.R;
import com.example.coursesapp.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.temporal.Temporal;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding bind;

    private FirebaseAuth fbAuth;

    private ProgressDialog progressDialog;

    private String email = "", password="";

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

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account");
        progressDialog.setCanceledOnTouchOutside(false);

        buttonsClicks();
    }


    private void buttonsClicks(){
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

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            bind.etEmailSignUpFragment.setError("Invalid email format");
        }
        else if (TextUtils.isEmpty(password)){
            bind.etPasswordSighUpFragment.setText("Enter password");
        }
        else if (password.length()<6){
            bind.etPasswordSighUpFragment.setText("Password must be more than 6 characters");
        }
        else {
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

                        FirebaseUser fbUser = fbAuth.getCurrentUser();
                        String email = fbUser.getEmail();
                        Toast.makeText(requireContext(), "Account created"+email, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(requireActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}