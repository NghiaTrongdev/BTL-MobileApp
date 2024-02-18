package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.btlmobileapp.R;
import com.example.btlmobileapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    private void listener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v->{
            if (isValid()){
                signUp();
            }
        });
    }
    private boolean isValid(){
        return false;
    }
    private void signUp(){

    }
}