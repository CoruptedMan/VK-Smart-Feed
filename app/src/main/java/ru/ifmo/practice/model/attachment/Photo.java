package ru.ifmo.practice.model.attachment;

import android.graphics.Bitmap;

import java.util.concurrent.ExecutionException;

public class Photo {

    private long id;
    private long albumId;
    private long ownerId;
    private long userId;
    private long postId;
    private int width;
    private int height;
    private int likesCount;
    private boolean userLikes;
    private int commentsCount;
    private boolean canComment;
    private int repostsCount;
    private boolean canRepost;
    private String descriptionText;
    private String date;
    private String accessKey;
    private String photoUrl;
    private String text;
    private Bitmap imageBitmap;

    public Photo(long pId,
                 long pOwnerId,
                 long pAlbumId,
                 String pPhotoUrl,
                 String pDate,
                 String pText,
                 int pWidth,
                 int pHeight) {
        id = pId;
        ownerId = pOwnerId;
        albumId = pAlbumId;
        photoUrl = pPhotoUrl;
        date = pDate;
        text = pText;
        width = pWidth;
        height = pHeight;
    }

    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setPhotoUrl(String pPhotoUrl) throws ExecutionException, InterruptedException {
        photoUrl = pPhotoUrl;
    }

    public void setImageBitmap(Bitmap pImageBitmap) {
        imageBitmap = pImageBitmap;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
