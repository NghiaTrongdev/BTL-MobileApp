package com.example.btlmobileapp.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btlmobileapp.Activities.StartActivity;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivityFragmentChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FragmentChangePassword extends AppCompatActivity {

    private ActivityFragmentChangePasswordBinding binding;
    private PreferenceManager preferenceManager;
    private String oldPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFragmentChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        oldPassword = preferenceManager.getString(Constants.KEY_PASSWORD);

        setListeners();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.btnChangePassword.setOnClickListener(v -> changePassword());

        // Current Password
        binding.oldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtoldPassword.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtoldPassword.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = binding.oldPass.getText().toString().trim();
                if (text.isEmpty()) {
                    binding.txtoldPassword.setText("Trường này còn trống");
                } else if (!checkIsvalidPassword(text)) {
                    binding.txtoldPassword.setText("Mật khẩu sai");
                }
            }
        });
        // New Password
        binding.newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtnewPassword.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = binding.newPass.getText().toString().trim();
                if (text.isEmpty()) {
                    binding.txtnewPassword.setText("Trường này còn trống");
                }
            }
        });

        // Confirm Password
        binding.reNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtrenewPassword.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPassStr = binding.newPass.getText().toString().trim();
                String renewPassStr = binding.reNewPass.getText().toString().trim();
                if (renewPassStr.isEmpty()) {
                    binding.txtrenewPassword.setText("Trường này còn trống");
                } else if (!checkIsvalidConfirmPassword(newPassStr, renewPassStr)) {
                    binding.txtrenewPassword.setText("Mật khẩu nhập lại sai");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void changePassword() {
        binding.btnChangePassword.setOnClickListener(v -> {
            String oldPassStr = binding.oldPass.getText().toString().trim();
            String newPassStr = binding.newPass.getText().toString().trim();
            String renewPassStr = binding.reNewPass.getText().toString().trim();
            if (checkIsvalidPassword(oldPassword) && checkIsvalidConfirmPassword(newPassStr, renewPassStr)) {
                updatePassword(newPassStr);

            }
        });
    }

    private boolean checkIsvalidPassword(String currentPassword) {
        if (oldPassword.equals(currentPassword)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIsvalidConfirmPassword(String newPassword, String confirmPassword) {
        if (confirmPassword.equals(newPassword)) {
            return true;
        } else {
            return false;
        }
    }

    private void updatePassword(String newPass) {
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_ID, userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(documentId)
                                    .update(Constants.KEY_PASSWORD, newPass)
                                    .addOnSuccessListener(v -> {
                                        showToast("Success");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d("FailedtoUpdate", "updatePassword: " + e.getMessage());
                                    });

                        } else {
                            showToast("Empty");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("FailToQuery", "updatePassword: " + e.getMessage());
                });
    }

    private void isLoading(boolean load) {
        if (load) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnChangePassword.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnChangePassword.setVisibility(View.VISIBLE);
        }
    }
}