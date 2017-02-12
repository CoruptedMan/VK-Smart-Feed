package ru.ifmo.practice.model;

public class Comment {
    private long id;
    private long authorId;
    private long date;
    private String context;
    private int likesCount;
    private boolean userLikes;

    public Comment(long pId, long pAuthorId, long pDate, String pContext, int pLikesCount, boolean pUserLikes) {
        id = pId;
        authorId = pAuthorId;
        date = pDate;
        context = pContext;
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

    public int getLikesCount() {
        return likesCount;
    }

    public boolean isUserLikes() {
        return userLikes;
    }
}
