package ru.ifmo.practice.model;

import ru.ifmo.practice.model.attachment.Photo;

public class Comment {
    private long id;
    private long authorId;
    private long date;
    private String authorName;
    private String context;
    private Photo authorPhoto;
    private int likesCount;
    private boolean userLikes;

    public Comment(long pId, long pAuthorId, long pDate, String pAuthorName, String pContext,
                   Photo pAuthorPhoto, int pLikesCount, boolean pUserLikes) {
        id = pId;
        authorId = pAuthorId;
        date = pDate;
        authorName = pAuthorName;
        context = pContext;
        authorPhoto = pAuthorPhoto;
        likesCount = pLikesCount;
        userLikes = pUserLikes;
    }

    public long getId() {
        return id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public long getDate() {
        return date;
    }

    public String getContext() {
        return context;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Photo getAuthorPhoto() {
        return authorPhoto;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int pLikesCount) {
        likesCount = pLikesCount;
    }

    public boolean isUserLikes() {
        return userLikes;
    }

    public void setUserLikes(boolean pUserLikes) {
        userLikes = pUserLikes;
    }
}
