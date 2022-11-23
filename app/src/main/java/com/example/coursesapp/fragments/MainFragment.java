package com.example.coursesapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursesapp.R;


public class MainFragment extends Fragment {

    Button buttonLogIn;
    TextView buttonSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLogIn = requireView().findViewById(R.id.button_logIn);
        buttonSignUp = requireActivity().findViewById(R.id.button_SignUp);

        regLogNavigation();

    }



        private void regLogNavigation(){
        View.OnClickListener clickLogInButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogInFragment()).commit();

               }
        };

        View.OnClickListener clickSignUpButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpFragment()).commit();

            }
        };

        buttonLogIn.setOnClickListener(clickLogInButton);
        buttonSignUp.setOnClickListener(clickSignUpButton);

    }


}