package com.example.btlmobileapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendItemClickListener;
import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendAdapter;
import com.example.btlmobileapp.MediaStorage.MediaStoreImage;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentListFriend extends Fragment {
    private RecyclerView friendRecyclerView;
    private List<User> userList;
    private LiveData<List<MediaStoreImage>> avatars;
    private PreferenceManager preferenceManager;
    ListFriendAdapter adapter;
    ListFriendItemClickListener listener = new ListFriendItemClickListener();

    public FragmentListFriend(PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        userList = getData();

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
//        list.add(new User("1", "user1", "0123"));
//        list.add(new User("2", "User2", "0456"));
//        list.add(new User("3", "User3", "0666"));
//        list.add(new User("4", "user4", "0123"));
//        list.add(new User("5", "User5", "0456"));
//        list.add(new User("6", "User6", "0666"));
//        list.add(new User("7", "user7", "0123"));
//        list.add(new User("8", "User8", "0456"));
//        list.add(new User("9", "User9", "0666"));
//        list.add(new User("10", "user10", "0123"));
//        list.add(new User("11", "User11", "0456"));
//        list.add(new User("12", "User12", "0666"));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Get Current User
        DocumentSnapshot currentUser = database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .get().getResult().getDocuments().get(0);

        // Get Friends
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereNotEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .orderBy(Constants.KEY_USER_ID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot doc : documents) {
                            if (IsFriend(doc, currentUser))
                                list.add(new User(doc.getString(Constants.KEY_USER_ID),
                                        doc.getString(Constants.KEY_USER_NAME),
                                        doc.getString(Constants.KEY_PHONE)));
                        }
                    }
                });

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
}