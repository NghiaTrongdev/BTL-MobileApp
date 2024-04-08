package com.example.btlmobileapp.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btlmobileapp.Adapters.FragmentListFriend.GetBitMap;
import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendItemClickListener;
import com.example.btlmobileapp.Adapters.FragmentListFriend.ListFriendAdapter;
import com.example.btlmobileapp.MediaStorage.ImageQueryHelper;
import com.example.btlmobileapp.MediaStorage.MediaStoreImage;
import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentListFriend extends Fragment {
    private RecyclerView friendRecyclerView;
    private List<User> userList;
    private LiveData<List<MediaStoreImage>> avatars;

    ListFriendAdapter adapter;
    ListFriendItemClickListener listener = new ListFriendItemClickListener();

    public FragmentListFriend() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        Uri uri = AddImage(bm);

//        if (uri == null) {
//            System.out.println("can not insert the image");
//        } else
//        // Load thumbnail of a specific media item.
//        {
//            try {
//                Bitmap thumbnail =
//                        requireContext().getContentResolver().loadThumbnail(
//                                uri, new Size(640, 480), null);
//                System.out.println(thumbnail.getWidth() + " " + thumbnail.getHeight());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    public Uri AddImage(Bitmap bitmap) {
        ImageQueryHelper imageQueryHelper = new ImageQueryHelper();
        try {
            Uri uri = imageQueryHelper.saveBitmap(requireContext(),
                    bitmap,
                    Bitmap.CompressFormat.JPEG,
                    "JPG", "harryguci.jpg");

            System.out.printf("Saved %s%n", uri);
            return uri;

        } catch (IOException e) {
//            throw new RuntimeException(e);
            return null;
        }
    }

    public boolean AddImage(String url) {
        ImageQueryHelper imageQueryHelper = new ImageQueryHelper();
        try {
            Bitmap bitmap = GetBitMap.get(url, requireContext());
            Uri uri = imageQueryHelper.saveBitmap(requireContext(),
                    bitmap,
                    Bitmap.CompressFormat.JPEG,
                    "JPG", "harryguci.jpg");

            System.out.printf("Saved %s%n", uri.toString());
        } catch (IOException e) {
//            throw new RuntimeException(e);
            return false;
        }
        return true;
    }

    public List<MediaStoreImage> GetMediaStorage() {
        Context application = requireContext();
        List<MediaStoreImage> images = ImageQueryHelper.queryImages(application);

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };
        String selection = MediaStore.Images.Media.DATE_ADDED + " >= ?";
        String[] selectionArgs = {
                String.valueOf(ImageQueryHelper.dateToTimestamp(22, 10, 2008))
        };
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = application.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        // Return a empty array
        if (cursor == null)
            return new ArrayList<>();

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
        int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

        while (cursor.moveToNext()) {
            // Use an ID column from the projection to get
            // a URI representing the media item itself.
            long id = cursor.getLong(idColumn);
            Date dateAdded = new Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)));
            String displayName = cursor.getString(displayNameColumn);

            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
            );

            MediaStoreImage image = new MediaStoreImage(id, displayName, dateAdded, contentUri);
            images.add(image);

            System.out.printf("Added Image: id=%s name=%s date=%s url=%s%n", id, displayName, dateAdded, contentUri);
        }
        cursor.close();
        return images;
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

        // "https://th.bing.com/th/id/OIP.hjQkrBp4SJ5aq_ulIZCl1AHaHc?rs=1&pid=ImgDetMain"
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