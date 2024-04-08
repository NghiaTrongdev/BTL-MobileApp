package com.example.btlmobileapp.MediaStorage;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImageQueryHelper {
    private static final String TAG = "ImageQueryHelper";

    public static List<MediaStoreImage> queryImages(Context application) {
        List<MediaStoreImage> images = new ArrayList<>();
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };
        String selection = MediaStore.Images.Media.DATE_ADDED + " >= ?";
        String[] selectionArgs = {
                String.valueOf(dateToTimestamp(22, 10, 2008))
        };
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = application.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

            Log.i(TAG, "Found " + cursor.getCount() + " images");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                Date dateModified = new Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)));
                String displayName = cursor.getString(displayNameColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                );

                MediaStoreImage image = new MediaStoreImage(id, displayName, dateModified, contentUri);
                images.add(image);
            }

            cursor.close();
        }

        return images;
    }

//    public Uri saveImage(@NonNull final Context context, @NonNull final Bitmap bitmap) {
//        ContentResolver resolver = context.getContentResolver();
//        Uri imageCollection;
//
//        imageCollection = MediaStore.Audio.Media
//                .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
//
//        // Publish a new song.
//        ContentValues newImageDetail = new ContentValues();
//        newImageDetail.put(MediaStore.Audio.Media.DISPLAY_NAME,
//                "Harryguci.jpg");
//
//        // Uri myFavoriteImageUri = resolver.insert(imageCollection, newImageDetail);
//
//        return resolver.insert(imageCollection, newImageDetail);
//    }

    @NonNull
    public Uri saveBitmap(@NonNull final Context context,
                          @NonNull final Bitmap bitmap,
                          @NonNull final Bitmap.CompressFormat format,
                          @NonNull final String mimeType,
                          @NonNull final String displayName) throws IOException {

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

        final ContentResolver resolver = context.getContentResolver();
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, values);

            if (uri == null)
                throw new IOException("Failed to create new MediaStore record.");

            try (final OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null)
                    throw new IOException("Failed to open output stream.");

                if (!bitmap.compress(format, 95, stream))
                    throw new IOException("Failed to save bitmap.");
            }

            return uri;
        } catch (IOException e) {

            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        }
    }

    public static long dateToTimestamp(int day, int month, int year) {
        // Implement your date-to-timestamp logic here
        // For example, return System.currentTimeMillis() for the current time
        return 0;
    }
}
