package com.example.btlmobileapp.Models;

import java.io.Serializable;

public class User implements Serializable {
    public String id, name, phone, password, image, email;

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
