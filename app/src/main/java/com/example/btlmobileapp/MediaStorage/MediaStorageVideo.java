package com.example.btlmobileapp.MediaStorage;

// Need the READ_EXTERNAL_STORAGE permission if accessing video files that your
// app didn't create.

import android.net.Uri;

// Container for information about each video.
class MediaStorageVideo {
    private final Uri uri;
    private final String name;
    private final int duration;
    private final int size;

    public MediaStorageVideo(Uri uri, String name, int duration, int size) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
    }
}
//    List<MediaStorageVideo> videoList = new ArrayList<MediaStorageVideo>();
//
//    Uri collection;
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//        } else {
//        collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        }
//
//        String[] projection = new String[] {
//        MediaStore.Video.Media._ID,
//        MediaStore.Video.Media.DISPLAY_NAME,
//        MediaStore.Video.Media.DURATION,
//        MediaStore.Video.Media.SIZE
//        };
//        String selection = MediaStore.Video.Media.DURATION +
//        " >= ?";
//        String[] selectionArgs = new String[] {
//        String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
//        };
//        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";
//
//        try (Cursor cursor = getApplicationContext().getContentResolver().query(
//        collection,
//        projection,
//        selection,
//        selectionArgs,
//        sortOrder
//        )) {
//        // Cache column indices.
//        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
//        int nameColumn =
//        cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
//        int durationColumn =
//        cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
//        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
//
//        while (cursor.moveToNext()) {
//        // Get values of columns for a given video.
//        long id = cursor.getLong(idColumn);
//        String name = cursor.getString(nameColumn);
//        int duration = cursor.getInt(durationColumn);
//        int size = cursor.getInt(sizeColumn);
//
//        Uri contentUri = ContentUris.withAppendedId(
//        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
//
//        // Stores column values and the contentUri in a local object
//        // that represents the media file.
//        videoList.add(new Video(contentUri, name, duration, size));
//        }
//        }