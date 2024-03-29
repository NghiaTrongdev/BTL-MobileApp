package com.example.btlmobileapp.Adapters.FragmentListFriend;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobileapp.R;

public class ListFriendViewHolder
        extends RecyclerView.ViewHolder {
    TextView username;
    TextView phone_number;
    Button goto_message_btn;
    ImageView avatar_view;
    View view;

    ListFriendViewHolder(View itemView)
    {
        super(itemView);
        username
                = (TextView)itemView
                .findViewById(R.id.username);

        phone_number
                = (TextView)itemView
                .findViewById(R.id.phone_number);

        goto_message_btn
                = (Button)itemView.findViewById(R.id.goto_message_btn);

        avatar_view
                = (ImageView)itemView.findViewById(R.id.avatar_view);

        view  = itemView;
    }
}