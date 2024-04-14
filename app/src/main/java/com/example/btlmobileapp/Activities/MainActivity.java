package com.example.btlmobileapp.Activities;


import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.btlmobileapp.Adapters.SearchResultAdapter;
import com.example.btlmobileapp.Fragments.FragmentHome;
import com.example.btlmobileapp.Fragments.FragmentListFriend;
import com.example.btlmobileapp.Fragments.FragmentProfile;
import com.example.btlmobileapp.Fragments.FragmentSearch;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    private String prevId = "";
    private String prevQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());
        init();
        setListener();

    }

    private void init() {
        binding.inputSearch.setVisibility(View.INVISIBLE);
        binding.buttonBackforSearch.setVisibility(View.INVISIBLE);
        binding.viewBackground.setVisibility(View.INVISIBLE);
        binding.textButton.setAlpha(0.5f);
        binding.textButton.setEnabled(false);
        binding.progess.setVisibility(View.INVISIBLE);
        getAllUser();
    }

    private void setListener() {
        binding.imageHome.setOnClickListener(v -> {
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));

             replaceFragment(new FragmentHome());
        });

        binding.imageAccount.setOnClickListener(v -> {
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            replaceFragment(new FragmentProfile());
        });

        binding.imageListFriend.setOnClickListener(v -> {
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));

            replaceFragment(new FragmentListFriend());
        });

        binding.textSearch.setOnClickListener(v -> {
            binding.inputSearch.setVisibility(View.VISIBLE);

            binding.inputSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.inputSearch, InputMethodManager.SHOW_IMPLICIT);
            }
            // edittext
            binding.clearButton.setVisibility(View.VISIBLE);
            binding.textSearch.setVisibility(View.INVISIBLE);
            binding.buttonBackforSearch.setVisibility(View.VISIBLE);

            binding.buttonSearch.setVisibility(View.INVISIBLE);
//            binding.viewBackground.setVisibility(View.VISIBLE);
        });

        binding.inputSearch.setOnClickListener(v -> {
            binding.textSearch.setVisibility(View.INVISIBLE);
            binding.buttonBackforSearch.setVisibility(View.VISIBLE);
            binding.buttonSearch.setVisibility(View.INVISIBLE);
            binding.viewBackground.setVisibility(View.VISIBLE);
        });
        binding.buttonBackforSearch.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.inputSearch.getWindowToken(), 0);
            }
            binding.viewBackground.setVisibility(View.INVISIBLE);
            binding.buttonBackforSearch.setVisibility(View.INVISIBLE);
            binding.buttonSearch.setVisibility(View.VISIBLE);

            binding.inputSearch.clearFocus();
            binding.textSearch.setVisibility(View.VISIBLE);
            binding.inputSearch.setVisibility(View.INVISIBLE);
            replaceFragment(new FragmentHome());
        });

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textButton.setAlpha(1.0f);
                binding.textButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = binding.inputSearch.getText().toString();
                if(query.equals("")){
                    binding.textButton.setAlpha(0.5f);
                    binding.textButton.setEnabled(false);
                }
            }
        });

        binding.textButton.setOnClickListener(v->{

            String query = binding.inputSearch.getText().toString();
           query(query);
        });
    }
    private boolean checkDifferent(String query){
        if (prevQuery.equals(query)){
            return false;
        } else {
            prevQuery = query;
            return true;
        }
    }

    private void query(String stringQuery) {
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE, stringQuery)
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot x : task.getResult()){
                            User user = new User();
                            user.id = x.getString(Constants.KEY_USER_ID);

                            if (!prevId.equals(user.id)){
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
                                users.add(user);
                            }
                        }
                        if(users.size() >0){
                            isLoading(false);
                            FragmentSearch fragmentSearch = new FragmentSearch();
                            fragmentSearch.onDataReceived(users);
                            replaceFragment(fragmentSearch);

                        }

                    } else {
                        isLoading(false);
                        showToast("Bug here");
                    }
                })
                .addOnFailureListener(v->{
                    isLoading(false);
                });
    }




    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain, fragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getAllUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .orderBy(Constants.KEY_USER_ID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                        if (list.size() >= 2) {
                            for (int i = 0; i < list.size(); i++) {
                                for (int j = i + 1; j < list.size(); j++) {
                                    DocumentSnapshot user1 = list.get(i);
                                    DocumentSnapshot user2 = list.get(j);
                                    checkAddFriend(user1, user2);
                                }
                            }
                        }
                    }
                });
    }

    private CompletableFuture<Boolean> checkrealtionshipIsvalid(DocumentSnapshot user1, DocumentSnapshot user2) {
        String userId1 = user1.getString(Constants.KEY_USER_ID);
        String userId2 = user2.getString(Constants.KEY_USER_ID);

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_SENDER_ID, userId1)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED, userId2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (queryDocumentSnapshots.isEmpty()) {
                            future.complete(true);
                        } else {
                            future.complete(false);
                        }
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });
        return future;
    }

    private void checkAddFriend(DocumentSnapshot user1, DocumentSnapshot user2) {
        checkrealtionshipIsvalid(user1, user2)
                .thenAccept(isValid -> {
                    if (isValid) {
                        AddRelationship(user1, user2);
                    }
                });
    }

    private void AddRelationship(DocumentSnapshot user1, DocumentSnapshot user2) {
        String userId1 = user1.getString(Constants.KEY_USER_ID);
        String userId2 = user2.getString(Constants.KEY_USER_ID);

        String relationshipId = userId1 + userId2;
            FirebaseFirestore database = FirebaseFirestore.getInstance();

            HashMap<String, Object> relationship = new HashMap<>();
            relationship.put(Constants.KEY_RELATION_ID, relationshipId);
            relationship.put(Constants.KEY_RELATION_SENDER_ID, userId1);
            relationship.put(Constants.KEY_RELATION_RECEIVED, userId2);
            relationship.put(Constants.KEY_RELATION_STATUS, "Init");
            database.collection(Constants.KEY_RELATION_COLLECTION).add(relationship)
                    .addOnCompleteListener(task -> {
                        showToast("Relationship added successfully");
                    })
                    .addOnFailureListener(e -> {
                        showToast("Failed to add relationship: " + e.getMessage());
                    });

    }
    private void isLoading(boolean loading) {
        if (loading) {
            binding.textButton.setAlpha(0.5f);
            binding.textButton.setEnabled(false);
            binding.progess.setVisibility(View.VISIBLE);
            binding.layoutMain.setVisibility(View.GONE);
        } else {
            binding.progess.setVisibility(View.INVISIBLE);
            binding.layoutMain.setVisibility(View.VISIBLE);
        }
    }


}