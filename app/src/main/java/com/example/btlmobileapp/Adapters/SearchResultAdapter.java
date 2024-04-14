package com.example.btlmobileapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobileapp.Activities.MainActivity;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.databinding.ItemContainerForSearchBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.UserViewHolder> {
    private final List<User> listUser;

    public SearchResultAdapter(List<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerForSearchBinding itemContainerForSearchBinding = ItemContainerForSearchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );


        return new UserViewHolder(itemContainerForSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = listUser.get(position);
        holder.setData(currentUser);
        String testId = "user003";
        getRelationshipStatus(testId,currentUser.id)
                .thenAccept(status -> {
                    if (status.equals("Init")){
                        holder.binding.btnStatus.setHint("Kết bạn");
                        holder.binding.btnStatus.setOnClickListener(v->{
                            holder.binding.btnStatus.setHint("Đã gửi");
                            holder.binding.btnStatus.setEnabled(true);
                            holder.binding.btnStatus.setAlpha(0.5f);
                        updateRelationshipStatus(testId, currentUser.id,0);
                        });
                    } else if (status.equals("request")) {
                        holder.binding.btnStatus.setHint("Đã gửi");
                        holder.binding.btnStatus.setOnClickListener(v->{
                            holder.binding.btnStatus.setHint("Kết bạn");
                            holder.binding.btnStatus.setEnabled(true);
                            holder.binding.btnStatus.setAlpha(0.5f);
                            updateRelationshipStatus(testId, currentUser.id, 1);
                        });

                    } else if (status.equals("friend")){
                        holder.binding.btnStatus.setVisibility(View.VISIBLE);
                    }
                });
    }


    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerForSearchBinding binding;
        UserViewHolder(ItemContainerForSearchBinding itemContainerForSearchBinding){
            super(itemContainerForSearchBinding.getRoot());
            binding = itemContainerForSearchBinding;
        }
        void setData(User user){
            binding.singleName.setText(user.name);
            binding.imageItem.setImageBitmap(getUserImage(user.image));
        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    private CompletableFuture<String> getRelationshipStatus(String userId1, String userId2) {
        CompletableFuture<String> future = new CompletableFuture<>();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_SENDER_ID, userId1)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED, userId2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String status = documentSnapshot.getString(Constants.KEY_RELATION_STATUS);
                            Log.d("TestStatus", "getRelationshipStatus: "+status);
                            future.complete(status);
                        } else {
                            future.complete("No Relationship"); // or any default status
                        }
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }

    private void updateRelationshipStatus(String userId1, String userId2 , int type) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_RELATION_COLLECTION)
                .whereEqualTo(Constants.KEY_RELATION_SENDER_ID, userId1)
                .whereEqualTo(Constants.KEY_RELATION_RECEIVED, userId2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            if (type ==0){
                                database.collection(Constants.KEY_RELATION_COLLECTION)
                                        .document(documentId)
                                        .update(Constants.KEY_RELATION_STATUS, "request");
                            } else {
                                database.collection(Constants.KEY_RELATION_COLLECTION)
                                        .document(documentId)
                                        .update(Constants.KEY_RELATION_STATUS, "Init");
                            }
                        }
                    }
                });
    }

}
