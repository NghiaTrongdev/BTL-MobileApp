package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.btlmobileapp.Models.LoginInfor;
import com.example.btlmobileapp.Notifications.LoginSuccessNotification;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivitySignInBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.util.concurrent.CompletableFuture;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    private LoginSuccessNotification loginSuccessNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.btnChangeTypePassWord.setBackgroundResource(R.drawable.visibility_fill0_wght400_grad0_opsz24);

        listener();


    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(loginSuccessNotification);
//    }

    private void listener() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonLogin.setOnClickListener(v -> {
            String phone = binding.inputPhone.getText().toString().trim();
            String password = binding.inputPassword.getText().toString().trim();
            if(checkPhone(phone) && checkPassword(password))
            {

                Login();
            } else {
                showToast("Failed to login");
            }

        });
        binding.btnChangeTypePassWord.setOnClickListener(v -> {
            if (binding.inputPassword.getTransformationMethod() != null) {
                binding.inputPassword.setTransformationMethod(null);
                binding.btnChangeTypePassWord.setBackgroundResource(R.drawable.visibility_off_fill0_wght400_grad0_opsz24);
            } else {
                binding.inputPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.btnChangeTypePassWord.setBackgroundResource(R.drawable.visibility_fill0_wght400_grad0_opsz24);
            }
        });
        binding.inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtPhone.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtPhone.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = binding.inputPhone.getText().toString().trim();
                if (phone.isEmpty()){
                    binding.txtPhone.setText("Trường này còn trống");
                }
                 else if (!checkPhone(phone))
                {
                    binding.txtPhone.setText("Số điện thoại không hợp lệ");
                } else {
                    binding.txtPhone.setText("");
                }
            }
        });
        binding.inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtPassword.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtPassword.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.inputPassword.getText().toString().trim();
                if (password.isEmpty()){
                    binding.txtPassword.setText("Trường này còn trống");
                } else if (!checkPassword(password))
                {
                    binding.txtPassword.setText("Mật khẩu phải từ 6 kí tự trở lên");
                } else {
                    binding.txtPassword.setText("");
                }
            }
        });

    }

    private void Login() {
        isLoading(true);
        String phone = binding.inputPhone.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE,phone)
                .whereEqualTo(Constants.KEY_PASSWORD, password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null && task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                        // Document snapshot sẽ chứa dữ liệu của collection
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

//                        preferenceManager.putBoolean(Constants.KEY_IS_ONLINE, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getString(Constants.KEY_USER_ID));
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        preferenceManager.putString(Constants.KEY_PASSWORD,documentSnapshot.getString(Constants.KEY_PASSWORD));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sendBroadcast(new Intent(this,LoginSuccessNotification.class).setAction("MyAction"));
                        startActivity(intent);
                    } else {
                        isLoading(false);
                        showToast("Failed to login");
                    }
                });
    }

    private void rememberLogin() {
        String username = binding.inputPhone.getText().toString();
        String password = binding.inputPassword.getText().toString();

        LoginInfor loginInfor = new LoginInfor(username, password);
        Gson gson = new Gson();
        String json = gson.toJson(loginInfor);



        try {
            FileOutputStream fOut = openFileOutput("login.json", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(json); // Write the string to the file
            osw.flush(); // Ensure everything is written out
            osw.close();


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void isLoading(boolean loading) {
        if (loading) {
            binding.buttonLogin.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    private boolean checkPhone(String phone) {
        if(phone.isEmpty())
        {
            return false;
        } else if (!phone.matches("(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b")) {
            return false;
        }

        return true;
    }
    private boolean checkPassword(String password){
        if (password.length() > 5){
            return true;
        }
        return false;
    }
}