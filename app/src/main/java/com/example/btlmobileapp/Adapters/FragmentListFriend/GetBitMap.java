package com.example.btlmobileapp.Adapters.FragmentListFriend;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class GetBitMap {
    public static void getBitmapUrl(String imageUrl, ImageView imageView)
    {
        Picasso.get().load(imageUrl).into(imageView);
    }

    public static Bitmap get(String imageUrl, Context context) throws IOException {
        Uri imageUri = Uri.parse(imageUrl);
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    }
}
