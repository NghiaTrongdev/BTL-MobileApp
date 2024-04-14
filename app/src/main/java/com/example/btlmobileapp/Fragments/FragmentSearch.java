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
        adapter = new SearchResultAdapter(listUser,getContext());
        binding.main.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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