package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.btlmobileapp.R;
import com.example.btlmobileapp.databinding.ActivityChatBinding;
import com.example.btlmobileapp.databinding.ActivityMainBinding;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    public void init() {
//        binding.buttonSearch.setVisibility(View.VISIBLE);
//        binding.buttonSearch.setOnClickListener(v -> finish());
//        binding.btnBack.setVisibility(View.VISIBLE);
//        binding.btnBack.setOnClickListener(v -> finish());
    }
}