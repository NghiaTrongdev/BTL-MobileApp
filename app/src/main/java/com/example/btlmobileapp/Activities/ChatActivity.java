package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.btlmobileapp.Adapters.ChatAdapter;
import com.example.btlmobileapp.Models.ChatMessage;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivityChatBinding;
import com.example.btlmobileapp.databinding.ActivityMainBinding;
import com.google.android.material.color.utilities.Contrast;
import com.google.firebase.Firebase;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private Firebase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReciverDetails();
        setListener();
    }

    private void loadReciverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        init();
//    }

//    public void init() {
//        binding.buttonSearch.setVisibility(View.VISIBLE);
//        binding.buttonSearch.setOnClickListener(v -> finish());
//    }
}