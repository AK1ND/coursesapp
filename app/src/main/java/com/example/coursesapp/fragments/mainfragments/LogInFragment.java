package com.example.coursesapp.fragments.mainfragments;

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
import com.example.coursesapp.databinding.FragmentLogInBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInFragment extends Fragment {

    private FragmentLogInBinding bind;

    private String email = "", password = "";

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind = FragmentLogInBinding.inflate(inflater, container, false);
        return bind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);

        checkUser();

        buttonsClicks();

    }


    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(requireContext(), HomeActivity.class));
        }
    }


    private void buttonsClicks() {
        bind.textSignUpLogInFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpFragment()).commit();

            }
        });

        bind.imgButtonLogInFragment.setOnClickListener(new View.OnClickListener() {
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

        email = bind.etEmailLogInFragment.getText().toString().trim();
        password = bind.etPasswordLogInFragment.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            bind.etEmailLogInFragment.setError("Invalid email format");
        } else if (TextUtils.isEmpty(password)) {
            bind.etPasswordLogInFragment.setHint("Enter password");
        } else if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password 6+ sm", Toast.LENGTH_SHORT).show();
        } else {
            firebaseLogIn();
        }

    }

    private void firebaseLogIn() {

        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String email = firebaseUser.getEmail();
                Toast.makeText(requireContext(), "LoggedIn\n" + email, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireActivity(), HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
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