package ru.ifmo.practice.model;

import java.util.Date;

public class Wall {
    private long id;
    private String sourceName;
    private String context;
    private Date date;
    private String photoUrl;
    private int likesCount;
    private int commentsCount;
    private int repostsCount;

    public Wall(long pId, String pSourceName, String pContext, Date pDate, String pPhotoUrl, int
            pLikesCount, int pCommentsCount, int pRepostsCount) {
        id = pId;
        sourceName = pSourceName;
        context = pContext;
        date = pDate;
        photoUrl = pPhotoUrl;
        likesCount = pLikesCount;
        commentsCount = pCommentsCount;
        repostsCount = pRepostsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String pSourceName) {
        sourceName = pSourceName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String pContext) {
        context = pContext;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date pDate) {
        date = pDate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String pPhotoUrl) {
        photoUrl = pPhotoUrl;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int pLikesCount) {
        likesCount = pLikesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int pCommentsCount) {
        commentsCount = pCommentsCount;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int pRepostsCount) {
        repostsCount = pRepostsCount;
    }

    @Override
    public String toString() {
        return "id" + id + "| " + sourceName + "\n" + context +
                "\nat " + date;
    }
}