package com.example.btlmobileapp.Models;


import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String id,name,phoneNumber,passWord,image,email,userName,bio;
    public int role;
    public Date dateofBirth,lastLogin,createAt;
}
