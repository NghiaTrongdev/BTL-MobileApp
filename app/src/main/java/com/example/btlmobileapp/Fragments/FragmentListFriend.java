package com.example.btlmobileapp.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btlmobileapp.Adapters.FragmentListFriend.GetBitMap;
import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendItemClickListener;
import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendAdapter;
import com.example.btlmobileapp.MediaStorage.ImageQueryHelper;
import com.example.btlmobileapp.MediaStorage.MediaStoreImage;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentListFriend extends Fragment {
    private RecyclerView friendRecyclerView;
    private List<User> userList;
    private LiveData<List<MediaStoreImage>> avatars;

    ListFriendAdapter adapter;
    ListFriendItemClickListener listener = new ListFriendItemClickListener();

    public FragmentListFriend() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_friend, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View currentView = getView();
        if (currentView == null)
            return;

        friendRecyclerView
                = (RecyclerView) currentView.findViewById(
                R.id.friend_recycler_view);
        userList = getData();

        adapter = new ListFriendAdapter(
                userList, FragmentListFriend.this.getContext(), listener);
        friendRecyclerView.setAdapter(adapter);

        android.content.Context context = this.getContext();
        if (context != null)
            friendRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    public List<User> getData() {
        List<User> list = new ArrayList<>();

        // Data testing
        list.add(new User("1", "user1", "0123"));
        list.add(new User("2", "User2", "0456"));
        list.add(new User("3", "User3", "0666"));
        list.add(new User("4", "user4", "0123"));
        list.add(new User("5", "User5", "0456"));
        list.add(new User("6", "User6", "0666"));
        list.add(new User("7", "user7", "0123"));
        list.add(new User("8", "User8", "0456"));
        list.add(new User("9", "User9", "0666"));
        list.add(new User("10", "user10", "0123"));
        list.add(new User("11", "User11", "0456"));
        list.add(new User("12", "User12", "0666"));

        return list;
    }
}