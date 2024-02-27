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

import com.example.btlmobileapp.Fragments.FragmentHome;
import com.example.btlmobileapp.Fragments.FragmentListFriend;
import com.example.btlmobileapp.Fragments.FragmentProfile;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


     ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());

        setListener();
    }

    private void setListener(){
        binding.layoutHome.setOnClickListener(v ->{
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));

            binding.textHome.setVisibility(View.VISIBLE);
            binding.textAccount.setVisibility(View.GONE);
            binding.textListFriend.setVisibility(View.GONE);

            binding.textHome.setTextColor(getResources().getColor(R.color.purple));

            binding.layoutHome.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_nav_select));
            binding.layoutAccount.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            binding.layoutListFriend.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

            replaceFragment(new FragmentHome());


            ScaleAnimation scaleAnimation  = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            binding.layoutHome.startAnimation(scaleAnimation);


        });
        binding.layoutAccount.setOnClickListener(v->{
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));

            binding.textHome.setVisibility(View.GONE);
            binding.textAccount.setVisibility(View.VISIBLE);
            binding.textListFriend.setVisibility(View.GONE);

            binding.textAccount.setTextColor(getResources().getColor(R.color.purple));

            binding.layoutAccount.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_nav_select));
            binding.layoutHome.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            binding.layoutListFriend.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

            replaceFragment(new FragmentProfile());


            ScaleAnimation scaleAnimation  = new ScaleAnimation(1.0f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            binding.layoutAccount.startAnimation(scaleAnimation);


        });
        binding.layoutListFriend.setOnClickListener(v->{
            binding.imageHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageAccount.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue_background));
            binding.imageListFriend.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple));

            binding.textHome.setVisibility(View.GONE);
            binding.textAccount.setVisibility(View.GONE);
            binding.textListFriend.setVisibility(View.VISIBLE);

            binding.textListFriend.setTextColor(getResources().getColor(R.color.purple));

            binding.layoutListFriend.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_nav_select));
            binding.layoutHome.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            binding.layoutAccount.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

            replaceFragment(new FragmentListFriend());


            ScaleAnimation scaleAnimation  = new ScaleAnimation(1.0f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            binding.layoutListFriend.startAnimation(scaleAnimation);

        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain,fragment);
        fragmentTransaction.commit();
    }
}