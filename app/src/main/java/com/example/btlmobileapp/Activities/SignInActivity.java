package com.example.btlmobileapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;

import com.example.btlmobileapp.Models.LoginInfor;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.databinding.ActivitySignInBinding;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
//            if (isValid()){
//                Login();
//            }
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
        String username = binding.inputUser.toString();
        String password = binding.inputPassword.toString();

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
    private boolean isValid(){
        return false;
    }
}