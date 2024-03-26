package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        listener();
        setInputAfterRemember();
    }
    private void setInputAfterRemember(){
                try (FileReader reader = new FileReader("login.json")) {
            // Sử dụng Gson để phân tích chuỗi JSON thành đối tượng Login
            Gson gson = new Gson();
            LoginInfor login = gson.fromJson(reader, LoginInfor.class );

            // Sử dụng thông tin đăng nhập
            String username = login.getUsername();
            String password = login.getPassword();
            // Tiến hành đăng nhập bằng thông tin này
            binding.inputUser.setText(username);
            binding.inputPassword.setText(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void listener(){
        binding.imageBack.setOnClickListener(v->onBackPressed());
        binding.buttonLogin.setOnClickListener(v->{
            if (isValid()){
                Login();
            }
        });
        binding.textChangeType.setOnClickListener(v->{
            if(binding.textChangeType.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD){
                binding.textChangeType.setText("Ẩn");
                binding.textChangeType.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                binding.textChangeType.setText("Hiện");
                binding.textChangeType.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

    }
    private void Login(){
//        isLoading(true);
//        String username = binding.inputUser.getText().toString().trim();
//        String password = binding.inputPassword.getText().toString().trim();
//
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .where(Filter .or(
//                    Filter.equalTo(Constants.KEY_EMAIL , username),
//                        Filter.equalTo(Constants.KEY_PHONE,username)
//                ))
//                .whereEqualTo(Constants.KEY_PASSWORD,password)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if(task.getResult() != null && task.isSuccessful() && task.getResult().getDocuments().size() >0 ){
//                        // Document snapshot sẽ chứa dữ liệu của collection
//                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//
//                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
//                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
//                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
//                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_IMAGE));
//
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    } else {
//                        isLoading(false);
//                        showToast("Failed to login");
//                    }
//                });

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
    }
    private void rememberLogin(){
        String username = binding.inputUser.toString();
        String password = binding.inputPassword.toString();

        LoginInfor loginInfor = new LoginInfor(username,password);
        Gson gson = new Gson();
        String json = gson.toJson(loginInfor);

        try(FileWriter fileWriter= new FileWriter("login.json")) {
                fileWriter.write(json);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void isLoading(boolean loading){
        if(loading){
            binding.buttonLogin.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    private boolean isValid(){
        return true;
    }
}