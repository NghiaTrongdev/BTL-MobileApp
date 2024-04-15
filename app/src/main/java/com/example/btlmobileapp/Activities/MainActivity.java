package com.example.btlmobileapp.Activities;


import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    private String prevId = "";

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


    }

    private void setListener() {
        binding.imageHome.setOnClickListener(v -> {
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.icon_selected_tint));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            replaceFragment(new FragmentHome());
        });

        binding.imageAccount.setOnClickListener(v -> {
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.icon_selected_tint));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            replaceFragment(new FragmentProfile());
        });

        binding.imageListFriend.setOnClickListener(v -> {
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.icon_selected_tint));

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

        //chat
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UserActivity.class)));

        binding.textButton.setOnClickListener(v->{
            actionSearch();
        });

    }
//    private void testSearch(){
//        replaceFragment(new FragmentSearch());
//    }
    private void actionSearch(){
        String query = binding.inputSearch.getText().toString();
        query(query);
//        testSendData();
    }
    private void query(String stringQuery) {
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
                            FragmentSearch fragmentSearch = new FragmentSearch();
                            replaceFragment(fragmentSearch);
                            fragmentSearch.onDataReceived(users);
                        }

                    } else {
                        showToast("Bug here");
                    }
                });
    }



    private void isLoading(boolean loading) {

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain, fragment);
        fragmentTransaction.commit();
    }
    private void testSendData(){
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
        FragmentSearch fragmentSearch = new FragmentSearch();
        replaceFragment(fragmentSearch);
        fragmentSearch.onDataReceived(list);
    }


    //update tokens
//    private void updateToken(String token) {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference =
//                database.collection (Constants.KEY_COLLECTION_USERS).document (
//                        preferenceManager.getString(Constants.KEY_USER_ID)
//                );
//        documentReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnFailureListener(e -> showToast("Unable to update token"));
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}