package com.example.btlmobileapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.btlmobileapp.Adapters.SearchResultAdapter;
import com.example.btlmobileapp.Listeners.OnReceivedDataListener;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.databinding.FragmentSearchBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment implements OnReceivedDataListener {
    private List<User> listUser;
    private FragmentSearchBinding binding;
    private SearchResultAdapter adapter;
    private String prevId="";

    public FragmentSearch() {
        // Required empty public constructor
    }
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        testData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

    }
    private void listener(){

    }

    private  void init(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
//        adapter = new SearchResultAdapter(listUser);
//        binding.main.setAdapter(adapter);
//        testQueyData();
//        listUser =  getData();
//        adapter = new SearchResultAdapter(listUser);
//        binding.main.setAdapter(adapter);
        adapter = new SearchResultAdapter(listUser);
        binding.main.setAdapter(adapter);

    }

    private void testQueyData(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
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
                                adapter = new SearchResultAdapter(users);
                                binding.main.setAdapter(adapter);
                        }

                    } else {
                        showToast("Bug here");
                    }
                });
    }
    private void testData(){
        for(int i = 0 ; i < 3; i++){
            User user = new User();
            user.name = "test" + i;
            listUser.add(user);
        }
        adapter = new SearchResultAdapter(listUser);
        binding.main.setAdapter(adapter);
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
    private void loading(Boolean isload) {
        if (isload) {
            binding.progess.setVisibility(View.VISIBLE);
        } else {
            binding.progess.setVisibility(View.INVISIBLE);

        }
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataReceived(List<User> list) {
        listUser = new ArrayList<>();
        listUser = list;

    }
}