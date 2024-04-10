package com.example.btlmobileapp.Listeners;

import com.example.btlmobileapp.Models.User;

import java.util.List;

public interface OnReceivedDataListener {
    void onDataReceived(List<User> list);
}
