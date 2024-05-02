package com.example.btlmobileapp.Fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btlmobileapp.Adapters.ListFriendAdapter;
import com.example.btlmobileapp.Adapters.RequestAddAdapter;
import com.example.btlmobileapp.Adapters.SearchResultAdapter;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {
    FragmentHomeBinding binding;
    private RequestAddAdapter requestAddAdapter;

    private PreferenceManager preferenceManager;
    private ListFriendAdapter adapter;
    private List<User> listUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void init(){
        preferenceManager = new PreferenceManager(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        getAllUser();
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
                            adapter = new ListFriendAdapter(listUser);
                            binding.main.setAdapter(adapter);
                        }

                    }
                })
                .addOnFailureListener(v->{
                    isLoading(false);
                });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    private void isLoading(boolean temp){
        if(temp){
            binding.progess.setVisibility(View.VISIBLE);
            binding.main.setVisibility(View.GONE);
        } else {
            binding.progess.setVisibility(View.INVISIBLE);
            binding.main.setVisibility(View.VISIBLE);
        }
    }
}