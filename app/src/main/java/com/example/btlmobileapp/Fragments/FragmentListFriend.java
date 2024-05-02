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


import com.example.btlmobileapp.Adapters.ListFriendAdapter;
import com.example.btlmobileapp.Adapters.RequestAddAdapter;
import com.example.btlmobileapp.Models.Relationship;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivityMainBinding;
import com.example.btlmobileapp.databinding.FragmentListFriendBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FragmentListFriend extends Fragment {

    private List<User> listTemp;
    private List<User> userList;
    private List<User> listUser;
    private List<User> requestUserList;
    private RequestAddAdapter requestAddAdapter;
    private List<String> listUserId;
    private PreferenceManager preferenceManager;
    private ListFriendAdapter adapter;

    private FragmentListFriendBinding binding;

;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
//        getRequestFriendData()
//                .thenAccept(isvalid->{
//                    if (isvalid){
//                        for (String x : listUserId){
//                            getRequestById(x);
//                        }
//
//                    }
//                });
        getAllRequests();
    }
    private void getAllRequests() {
        isLoading(true);
        listUser = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED, preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot listSnaps = task.getResult();
                        List<Task<User>> userTasks = new ArrayList<>(); // Danh sách tạm thời các tác vụ người dùng
                        for (QueryDocumentSnapshot document : listSnaps) {
                            String senderId = document.getString(Constants.KEY_RELATION_SENDER_ID);
                            Task<User> userTask = getUserDetails(database, senderId);
                            userTasks.add(userTask);
                        }

                        // Chạy tất cả các tác vụ người dùng
                        Tasks.whenAllComplete(userTasks)
                                .addOnCompleteListener(completeTask -> {
                                    for (Task<User> x : userTasks) {
                                        if (x.isSuccessful()) {
                                            User user = x.getResult();
                                            if (user != null) {
                                                listUser.add(user);
                                            }
                                        } else {
                                            // Xử lý lỗi nếu cần
                                        }
                                    }

                                    // Cập nhật giao diện nếu danh sách người dùng không rỗng
                                    if (!listUser.isEmpty()) {
                                        isLoading(false);
                                        adapter = new ListFriendAdapter(requestUserList);
                                        binding.main.setAdapter(adapter);
                                    }
                                });
                    } else {
                        // Xử lý lỗi nếu cần
                    }
                });
    }

    private Task<User> getUserDetails( String userId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_ID, userId)
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        QuerySnapshot userSnapshot = userTask.getResult();
                        if (!userSnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = userSnapshot.getDocuments().get(0);
                            User user = new User();
                            user.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            user.id = documentSnapshot.getString(Constants.KEY_USER_ID);
                            user.name = documentSnapshot.getString(Constants.KEY_NAME);
                            user.phoneNumber = documentSnapshot.getString(Constants.KEY_PHONE);
                            taskCompletionSource.setResult(user);
                        } else {
                            taskCompletionSource.setException(new Exception("User not found"));
                        }
                    } else {
                        taskCompletionSource.setException(userTask.getException());
                    }
                });
        return taskCompletionSource.getTask();
    }


    private Task<User> getUserDetails(FirebaseFirestore database, String userId) {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_ID, userId)
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        QuerySnapshot userSnapshot = userTask.getResult();
                        if (!userSnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = userSnapshot.getDocuments().get(0);
                            User user = new User();
                            user.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            user.id = documentSnapshot.getString(Constants.KEY_USER_ID);
                            user.name = documentSnapshot.getString(Constants.KEY_NAME);
                            user.phoneNumber = documentSnapshot.getString(Constants.KEY_PHONE);
                            taskCompletionSource.setResult(user);
                        } else {
                            taskCompletionSource.setException(new Exception("User not found"));
                        }
                    } else {
                        taskCompletionSource.setException(userTask.getException());
                    }
                });
        return taskCompletionSource.getTask();
    }


    private void getAllUser(){
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereNotEqualTo(Constants.KEY_USER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        listUser = new ArrayList<>();
                        for (QueryDocumentSnapshot x : task.getResult()){
                            User user = new User();
                            user.id = x.getString(Constants.KEY_USER_ID);

                            user.bio = x.getString(Constants.KEY_BIO);
                            user.name = x.getString(Constants.KEY_NAME);
                            user.image = x.getString(Constants.KEY_IMAGE);
                            user.createAt = x.getDate(Constants.KEY_CREATE_DATE);
                            user.dateofBirth = x.getDate(Constants.KEY_DATE_OF_BIRTH);

                            user.email = x.getString(Constants.KEY_EMAIL);
                            user.passWord = x.getString(Constants.KEY_PASSWORD);

                            long temp = x.getLong(Constants.KEY_ROLE);
                            user.role = (int) temp;
                            user.phoneNumber = x.getString(Constants.KEY_PHONE);
                            listUser.add(user);
                        }
                        if (listUser.size() > 0){
                            isLoading(false);
                            requestAddAdapter = new RequestAddAdapter(listUser);
                            binding.main.setAdapter(requestAddAdapter);
                        }

                    }
                })
                .addOnFailureListener(v->{
                    isLoading(false);
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
                        QuerySnapshot listSnaps = task.getResult();
                        for (QueryDocumentSnapshot x : listSnaps){

                        }

                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("TestQueryError ",e.getMessage());
                    future.complete(false);
                });
        return future;

    }
//    private void getRequestById(String id){
//        isLoading(true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        userList = new ArrayList<>();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .whereEqualTo(Constants.KEY_USER_ID,id)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.getResult() != null && task.isSuccessful() && task.getResult().getDocuments().size() > 0)
//                    {
//                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                        User user = new User();
//                        user.name = documentSnapshot.getString(Constants.KEY_NAME);
//                        user.image = documentSnapshot.getString(Constants.KEY_IMAGE);
//                        user.phoneNumber = documentSnapshot.getString(Constants.KEY_PHONE);
//                        if (userList.size() == listUserId.size()) { // Kiểm tra xem đã lấy đủ thông tin cho tất cả các userId chưa
//                            requestAddAdapter = new RequestAddAdapter(userList);
//                            binding.layoutMain.setAdapter(requestAddAdapter);
//                            isLoading(false);
//                        }
//                    }
//                })
//                .addOnFailureListener(e ->{
//                    isLoading(false);
//
//                    showToast(e.getMessage());
//                });
//    }
private void isLoading(boolean temp){
    if(temp){
        binding.progess.setVisibility(View.VISIBLE);
        binding.main.setVisibility(View.GONE);
    } else {
        binding.progess.setVisibility(View.INVISIBLE);
        binding.main.setVisibility(View.VISIBLE);
    }
}
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}