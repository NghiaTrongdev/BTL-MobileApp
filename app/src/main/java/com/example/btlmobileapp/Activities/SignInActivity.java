package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.btlmobileapp.Models.LoginInfor;
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

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.btnChangeTypePassWord.setBackgroundResource(R.drawable.visibility_fill0_wght400_grad0_opsz24);

        listener();
        setInputAfterRemember();
    }

    private void setInputAfterRemember() {

        // Reading the file back (optional)
//        FileInputStream fIn = null;
//        try {
//            fIn = openFileInput("login.json");
//            InputStreamReader isr = new InputStreamReader(fIn);
//            char[] inputBuffer = new char[500];
//            isr.read(inputBuffer);
//            String readString = new String(inputBuffer);
//            LoginInfor loginInfor = LoginInfor.CreateFromJson(readString);
//            binding.inputUser.setText(loginInfor.getUsername());
//            binding.inputPassword.setText(loginInfor.getPassword());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        try (FileReader reader = new FileReader("login.json")) {
//
//
//            // Sử dụng Gson để phân tích chuỗi JSON thành đối tượng Login
//            Gson gson = new Gson();
//            LoginInfor login = gson.fromJson(reader, LoginInfor.class);
//
//            // Sử dụng thông tin đăng nhập
//            String username = login.getUsername();
//            String password = login.getPassword();
//            // Tiến hành đăng nhập bằng thông tin này
//            binding.inputUser.setText(username);
//            binding.inputPassword.setText(password);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void listener() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonLogin.setOnClickListener(v -> {
            if (isValid()) {
                Login();
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

    }

    private void Login() {
//        isLoading(true);
        String username = binding.inputUser.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .where(Filter.or(
                        Filter.equalTo(Constants.KEY_EMAIL, username),
                        Filter.equalTo(Constants.KEY_PHONE, username)
                ))
                .whereEqualTo(Constants.KEY_PASSWORD, password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null && task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                        // Document snapshot sẽ chứa dữ liệu của collection
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        preferenceManager.putBoolean(Constants.KEY_IS_ONLINE, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_IMAGE));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        isLoading(false);
                        showToast("Failed to login");
                    }
                });

        if (binding.buttonRemember.isChecked())
            rememberLogin();

//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    private void rememberLogin() {
        String username = binding.inputUser.getText().toString();
        String password = binding.inputPassword.getText().toString();

        LoginInfor loginInfor = new LoginInfor(username, password);
        Gson gson = new Gson();
        String json = gson.toJson(loginInfor);

//        try (FileWriter fileWriter = new FileWriter("login.json")) {
//            fileWriter.write(json);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            FileOutputStream fOut = openFileOutput("login.json", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(json); // Write the string to the file
            osw.flush(); // Ensure everything is written out
            osw.close();

            // Reading the file back (optional)
//            FileInputStream fIn = openFileInput("samplefile.txt");
//            InputStreamReader isr = new InputStreamReader(fIn);
//            char[] inputBuffer = new char[TESTSTRING.length()];
//            isr.read(inputBuffer);
//            String readString = new String(inputBuffer);
//            boolean isTheSame = TESTSTRING.equals(readString);
//            Log.i("File Reading stuff", "Success = " + isTheSame);
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

    private boolean isValid() {
        return true;
    }
}