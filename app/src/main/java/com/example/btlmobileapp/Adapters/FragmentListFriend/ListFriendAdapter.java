package com.example.btlmobileapp.Adapters.FragmentListFriend;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;

import java.util.List;

public class ListFriendAdapter
        extends RecyclerView.Adapter<ListFriendViewHolder> {

    List<User> list;
    Context context;
    ListFriendItemClickListener listener;

    public ListFriendAdapter(List<User> list,
                             Context context, ListFriendItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListFriendViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {

        Context context
                = parent.getContext();

        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.list_friend_item,
                        parent, false);

        return new ListFriendViewHolder(photoView);
    }

    @Override
    public void
    onBindViewHolder(@NonNull final ListFriendViewHolder viewHolder,
                     final int position) {
        final int index = viewHolder.getAdapterPosition();

        viewHolder.username
                .setText(list.get(position).name);

        viewHolder.phone_number
                .setText(list.get(position).phoneNumber);

        // Bug: Can not load the image by the uri

        String userImageUri = list.get(position).image;
        if (userImageUri != null) {
            viewHolder.avatar_view.setImageURI(Uri.parse(userImageUri));
            viewHolder.avatar_view.invalidate();
        } else
            // Set the Default avatar image.
            viewHolder.avatar_view.setImageResource(R.drawable.default_avatar_icon);

        viewHolder.goto_message_btn.setOnClickListener(view -> listener.handleClick(view, list.get(position).name));
//        viewHolder.view.setOnClickListener(view -> listener.handleClick(list.get(position).name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
