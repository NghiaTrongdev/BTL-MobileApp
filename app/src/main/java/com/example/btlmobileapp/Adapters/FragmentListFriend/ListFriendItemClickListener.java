package com.example.btlmobileapp.Adapters.FragmentListFriend;

import android.content.Intent;
import android.view.View;

import com.example.btlmobileapp.Activities.ChatActivity;

public class ListFriendItemClickListener {
    public void handleClick(View currentView, int userId) {
        // Todo: Navigate to the chatting activity (By friend's id).
        System.out.println("The friend's id = " + userId);

        Intent intent = new Intent(currentView.getContext(), ChatActivity.class);
        currentView.getContext().startActivity(intent);
    }

    public void handleClick(View currentView, String username) {
        // Todo: Navigate to the chatting activity (By friend's username).
        System.out.println("The friend's username = " + username);

        Intent intent = new Intent(currentView.getContext(), ChatActivity.class);
        currentView.getContext().startActivity(intent);
    }
}