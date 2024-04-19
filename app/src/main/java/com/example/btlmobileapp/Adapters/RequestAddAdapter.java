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

public class RequestAddAdapter extends RecyclerView.Adapter<RequestAddAdapter.RequestHolder> {
    private final List<User> listUser;

    public RequestAddAdapter(List<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerForSearchBinding itemContainerForSearchBinding = ItemContainerForSearchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new  RequestHolder(itemContainerForSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        holder.setData(listUser.get(position));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class RequestHolder extends RecyclerView.ViewHolder{
        ItemContainerForSearchBinding binding;
        RequestHolder(ItemContainerForSearchBinding itemContainerForSearchBinding){
            super(itemContainerForSearchBinding.getRoot());
            binding = itemContainerForSearchBinding;
        }

        void setData(User user){
            binding.imageItem.setImageBitmap(getUserImage(user.image));
            binding.singleName.setText(user.name);
            binding.btnStatus.setText("Chấp nhận");
        }
        void listener(){
            binding.btnStatus.setOnClickListener(v->{
                // Update status realtionship
            });
        }
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
