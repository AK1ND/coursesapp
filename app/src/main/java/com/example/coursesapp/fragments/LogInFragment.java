package com.example.coursesapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.coursesapp.R;

import org.w3c.dom.Text;

public class LogInFragment extends Fragment {

    TextView buttonReg;
    ImageButton buttonClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonReg = requireActivity().findViewById(R.id.text_signUp_log_in_fragment);
        buttonClose = requireActivity().findViewById(R.id.img_button_log_in_fragment);


        buttonsClicks();

    }


    private void buttonsClicks(){
        View.OnClickListener clickRegButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpFragment()).commit();

            }
        };

        View.OnClickListener clickCloseButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

            }
        };

        buttonClose.setOnClickListener(clickCloseButton);
        buttonReg.setOnClickListener(clickRegButton);
    }

}