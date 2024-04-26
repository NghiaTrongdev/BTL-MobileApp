package com.example.btlmobileapp.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.btlmobileapp.Utilities.Constants;

public class LoginSuccessNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("MyAction"))
        {
            Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show();
        }
    }
}
