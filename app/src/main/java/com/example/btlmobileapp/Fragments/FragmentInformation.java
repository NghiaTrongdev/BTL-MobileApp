package com.example.btlmobileapp.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivityFragmentInformationBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentInformation extends AppCompatActivity {

    private ActivityFragmentInformationBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFragmentInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        setListener();
        renderProfileInformation();
    }

    private void setListener() {
        binding.imageBack.setOnClickListener(v -> finish());
    }

    // Phương thức cập nhật giao diện với thông tin người dùng
    public void renderProfileInformation() {
        binding.titleUsername.setText(preferenceManager.getString(Constants.KEY_USER_NAME));
        binding.profileUserName.setText(preferenceManager.getString(Constants.KEY_USER_NAME));
        binding.profileName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.profileEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        binding.profilePassword.setText(preferenceManager.getString(Constants.KEY_PASSWORD));
    }

    // Phương thức hiển thị thông báo lỗi
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}