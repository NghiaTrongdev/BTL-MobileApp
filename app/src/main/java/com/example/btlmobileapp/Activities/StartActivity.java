package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// import com.example.btlmobileapp.R;
import com.example.btlmobileapp.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listener();
    }
    private void listener(){
        binding.buttonLogin.setOnClickListener( v->{
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        });
        binding.buttonSignUp.setOnClickListener( v->{
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
    }
}