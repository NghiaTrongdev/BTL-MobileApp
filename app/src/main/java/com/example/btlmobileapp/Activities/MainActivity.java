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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;

import com.example.btlmobileapp.Fragments.FragmentHome;
import com.example.btlmobileapp.Fragments.FragmentListFriend;
import com.example.btlmobileapp.Fragments.FragmentProfile;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


     ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());
        init();
        setListener();
    }
    private void init(){
        binding.inputSearch.setVisibility(View.INVISIBLE);
        binding.buttonBackforSearch.setVisibility(View.INVISIBLE);
        binding.viewBackground.setVisibility(View.INVISIBLE);
    }
    private void setListener(){
        binding.imageHome.setOnClickListener( v->{
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
        });

        binding.imageAccount.setOnClickListener( v->{
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
        });

        binding.imageListFriend.setOnClickListener( v->{
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
        });

        binding.textSearch.setOnClickListener(v -> {
            binding.inputSearch.setVisibility(View.VISIBLE);
            binding.inputSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.inputSearch, InputMethodManager.SHOW_IMPLICIT);
            }
            binding.textSearch.setVisibility(View.INVISIBLE);
            binding.buttonBackforSearch.setVisibility(View.VISIBLE);
            binding.buttonSearch.setVisibility(View.INVISIBLE);
            binding.viewBackground.setVisibility(View.VISIBLE);
        });
        binding.inputSearch.setOnClickListener(v->{
            binding.textSearch.setVisibility(View.INVISIBLE);
            binding.buttonBackforSearch.setVisibility(View.VISIBLE);
            binding.buttonSearch.setVisibility(View.INVISIBLE);
            binding.viewBackground.setVisibility(View.VISIBLE);
        });
        binding.buttonBackforSearch.setOnClickListener(v->{
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
        });



    }
    private void query(String stringQuery){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain,fragment);
        fragmentTransaction.commit();
    }
}