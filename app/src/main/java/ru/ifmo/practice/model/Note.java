package ru.ifmo.practice.model;

import java.util.ArrayList;

import ru.ifmo.practice.model.attachments.Link;

public class Note {
    private long id;
    private long sourceId;
    private String sourceName;
    private String context;
    private String contextPreview;
    private long date;
    private String sourcePhotoUrl;
    private ArrayList<String> attachmentsPhotos;
    private int likesCount;
    private boolean userLikes;
    private int commentsCount;
    private boolean canComment;
    private int repostsCount;
    private boolean userReposted;
    private Link attachedLink;

    public Note(long pId,
                long pSourceId,
                String pSourceName,
                String pContext,
                String pContextPreview,
                long pDate,
                String pSourcePhotoUrl,
                ArrayList<String> pAttachmentsPhotos,
                int pLikesCount,
                boolean pUserLikes,
                int pCommentsCount,
                boolean pCanComment,
                int pRepostsCount,
                boolean pUserReposted) {
        id = pId;
        sourceId = pSourceId;
        sourceName = pSourceName;
        context = pContext;
        contextPreview = pContextPreview;
        date = pDate;
        sourcePhotoUrl = pSourcePhotoUrl;
        attachmentsPhotos = pAttachmentsPhotos;
        likesCount = pLikesCount;
        userLikes = pUserLikes;
        commentsCount = pCommentsCount;
        canComment = pCanComment;
        repostsCount = pRepostsCount;
        userReposted = pUserReposted;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String pContext) {
        context = pContext;
    }

    public long getDate() {
        return date;
    }

    public String getSourcePhotoUrl() {
        return sourcePhotoUrl;
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

    public boolean getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(boolean pUserLikes) {
        userLikes = pUserLikes;
    }

    public boolean getCanComment() {
        return canComment;
    }

    public boolean getUserReposted() {
        return userReposted;
    }

    public String getContextPreview() {
        return contextPreview;
    }

    public Link getAttachedLink() {
        return attachedLink;
    }

    public void setAttachedLink(Link pAttachedLink) {
        attachedLink = pAttachedLink;
    }

    @Override
    public String toString() {
        return "Post id" + id + "\n" +
                "Source id" + sourceId + " " + sourceName + "\n"
                + context + "\nL: " + likesCount + "| C: " + commentsCount + "| R: " +
                repostsCount + "\n"
                + "Photos: " + attachmentsPhotos.size() + "\n"
                + date;
    }
}