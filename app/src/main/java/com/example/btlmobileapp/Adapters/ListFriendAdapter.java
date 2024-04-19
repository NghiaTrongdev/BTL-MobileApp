package com.example.btlmobileapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.databinding.ItemContainerForSearchBinding;

import java.util.List;

public class ListFriendAdapter extends RecyclerView.Adapter<ListFriendAdapter.ListFriendHolder> {
     private final List<User> listUser;

    public ListFriendAdapter(List<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public ListFriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerForSearchBinding binding = ItemContainerForSearchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,false
        );
        return new ListFriendHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFriendHolder holder, int position) {
        holder.setData(listUser.get(position));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class ListFriendHolder extends RecyclerView.ViewHolder{
        ItemContainerForSearchBinding binding;
        public ListFriendHolder(ItemContainerForSearchBinding itemContainerForSearchBinding) {

            super(itemContainerForSearchBinding.getRoot());
            this.binding = itemContainerForSearchBinding;

        }
        void setData(User user){
            binding.imageItem.setImageBitmap(getUserImage(user.image));
            binding.singleName.setText(user.name);
            binding.btnStatus.setVisibility(View.INVISIBLE);
        }
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
