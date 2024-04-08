package com.example.btlmobileapp.MediaStorage;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import java.util.Date;
import java.util.Objects;

public class MediaStoreImage {
    private long id;
    private String displayName;
    private Date dateAdded;
    private Uri contentUri;

    public MediaStoreImage(long id, String displayName, Date dateAdded, Uri contentUri) {
        this.id = id;
        this.displayName = displayName;
        this.dateAdded = dateAdded;
        this.contentUri = contentUri;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Uri getContentUri() {
        return contentUri;
    }

    public static final DiffUtil.ItemCallback<MediaStoreImage> DiffCallback =
            new DiffUtil.ItemCallback<MediaStoreImage>() {
                @Override
                public boolean areItemsTheSame(MediaStoreImage oldItem, MediaStoreImage newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull MediaStoreImage oldItem, @NonNull MediaStoreImage newItem) {
                    return Objects.equals(oldItem, newItem);
                }
            };
}
