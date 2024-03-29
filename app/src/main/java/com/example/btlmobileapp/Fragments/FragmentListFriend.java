package com.example.btlmobileapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendItemClickListener;
import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendAdapter;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListFriend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListFriend extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView friendRecyclerView;
    private List<User> userList;

    ListFriendAdapter adapter;
    ListFriendItemClickListener listener = new ListFriendItemClickListener();

    public FragmentListFriend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListFriend.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListFriend newInstance(String param1, String param2) {
        FragmentListFriend fragment = new FragmentListFriend();
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
        userList = getData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_friend, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View currentView = getView();
        if (currentView == null)
            return;

        friendRecyclerView
                = (RecyclerView) currentView.findViewById(
                R.id.friend_recycler_view);

        adapter = new ListFriendAdapter(
                userList, FragmentListFriend.this.getContext(), listener);
        friendRecyclerView.setAdapter(adapter);

        android.content.Context context = this.getContext();
        if (context != null)
            friendRecyclerView.setLayoutManager(new LinearLayoutManager(context));
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
}