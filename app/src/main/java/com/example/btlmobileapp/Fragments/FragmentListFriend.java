package com.example.btlmobileapp.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.btlmobileapp.Adapters.RequestAddAdapter;
import com.example.btlmobileapp.Models.Relationship;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivityMainBinding;
import com.example.btlmobileapp.databinding.FragmentListFriendBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FragmentListFriend extends Fragment {
    private RecyclerView friendRecyclerView;
    private List<User> listTemp;
    private List<User> userList;
    private List<User> requestUserList;
    private RequestAddAdapter requestAddAdapter;
    private List<String> listUserId;
    private PreferenceManager preferenceManager;


    private FragmentListFriendBinding binding;

    private String tab = "friend";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferenceManager = new PreferenceManager(getContext());
        binding = FragmentListFriendBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    private void listener(){
        binding.btnTypFriends.setOnClickListener( v->{
            binding.btnTypeRequests.setAlpha(0.5f);
            binding.btnTypeRequests.setEnabled(false);

            binding.btnTypFriends.setAlpha(1f);
            binding.btnTypFriends.setEnabled(true);
            getRequestFriendData().thenAccept(isSuccesful -> {
                if (isSuccesful){
                    getRequestById();

                }
            });

        });
        binding.btnTypeRequests.setOnClickListener( v ->{
            binding.btnTypeRequests.setAlpha(1f);
            binding.btnTypeRequests.setEnabled(true);

            binding.btnTypFriends.setAlpha(0.5f);
            binding.btnTypFriends.setEnabled(false);
        });
    }


    private CompletableFuture<Boolean> getRequestFriendData(){
        String receivedId = preferenceManager.getString(Constants.KEY_USER_ID);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED,receivedId)
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()){
                        listUserId = new ArrayList<>();
                        QuerySnapshot listSnaps = task.getResult();
                        for (QueryDocumentSnapshot x : listSnaps){
                            listUserId.add(x.getString(Constants.KEY_RELATION_RECEIVED));
                        }
                        if (listUserId.size()>0){
                            showToast("Size" + listUserId.size());
                            future.complete(true);
                        }else {
                            future.complete(false);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("TestQueryError ",e.getMessage());
                    future.complete(false);
                });
        return future;

    }
    private void getRequestById(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        QuerySnapshot listSnaps = task.getResult();
                        for (QueryDocumentSnapshot x : listSnaps){
                            User user = new User();
                            user.id = x.getString(Constants.KEY_USER_ID);
                            if(listUserId.contains(user.id)){
                                user.name = x.getString(Constants.KEY_NAME);
                                user.image = x.getString(Constants.KEY_IMAGE);
                                userList.add(user);
                            }
                        }
                        if(userList.size()>0){
                            requestAddAdapter = new RequestAddAdapter(userList);
                            binding.layoutMain.setAdapter(requestAddAdapter);
                        }
                    }
                })
                .addOnFailureListener(e ->{
                    showToast(e.getMessage());
                });
    }
//    private CompletableFuture<Boolean> getFriendData(){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//
//    }


    private void isLoading(boolean temp){
        if(temp){
            binding.progress.setVisibility(View.VISIBLE);
            binding.layoutMain.setVisibility(View.GONE);
        } else {
            binding.progress.setVisibility(View.INVISIBLE);
            binding.layoutMain.setVisibility(View.VISIBLE);
        }
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}