package com.example.btlmobileapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.databinding.ItemContainerForSearchBinding;

import java.util.ArrayList;
import java.util.List;

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
        holder.setData(listUser.get(position));
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
}
