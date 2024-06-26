package com.example.btlmobileapp.Models;

import com.google.gson.Gson;

public class LoginInfor
{
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LoginInfor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static LoginInfor CreateFromJson(String json) {
        return new Gson().fromJson(json, LoginInfor.class);
    }

}
