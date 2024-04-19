package com.example.btlmobileapp.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

public class FragmentListFriend extends Fragment {
    private RecyclerView friendRecyclerView;
    private List<User> listTemp;
    private List<User> userList;
    private List<User> requestUserList;

    private PreferenceManager preferenceManager;


    private FragmentListFriendBinding binding;

    private String tab = "friend";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferenceManager = new PreferenceManager(getContext());
        listTemp = new ArrayList<>();
        binding = FragmentListFriendBinding.inflate(getLayoutInflater(),container,false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        View currentView = getView();
        if (currentView == null)
            return;

    }



    public List<User> getRequests() {
        List<User> list = new ArrayList<>();

        return list;
    }

    private boolean IsFriend(DocumentSnapshot user1, DocumentSnapshot user2) {
        String userId1 = user1.getString(Constants.KEY_USER_ID);
        String userId2 = user2.getString(Constants.KEY_USER_ID);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        return !database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_SENDER_ID, userId1)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED, userId2)
                .get().getResult().isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void getListFriendData(){
        String constUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        showToast("UserId " + constUserId);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED,constUserId)
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()){
                        QuerySnapshot listSnaps = task.getResult();
                        for (QueryDocumentSnapshot x : listSnaps){
                            User user = new User();
                            user.name = x.getString(Constants.KEY_NAME);
                            user.id = x.getString(Constants.KEY_USER_ID);
                            listTemp.add(user);
                        }
                        if(listTemp.size() > 0){

                            Log.d("TestQuery ",""+ listTemp.size());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("TestQueryError ",e.getMessage());
                });

    }
    private void isLoading(boolean temp){
        if(temp){
            binding.progress.setVisibility(View.VISIBLE);
            binding.layoutMain.setVisibility(View.GONE);
        } else {
            binding.progress.setVisibility(View.INVISIBLE);
            binding.layoutMain.setVisibility(View.VISIBLE);
        }
    }
}