package com.example.btlmobileapp.Models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String id,name,phoneNumber,passWord,image,email,userName,bio;
    public int role;
    public Date dateofBirth,lastLogin,createAt;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public User(String id, String name, String phone, String image) {
        this(id, name, phone);
        this.image = image;
    }
}
