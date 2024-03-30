package com.example.btlmobileapp.Models;


import java.io.Serializable;
import java.util.Date;

public class Relationship implements Serializable {
    public String relationshipId,senderId,recievedId,status;
    public Date createAt;
}
