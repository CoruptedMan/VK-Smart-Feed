package ru.ifmo.practice.model;

public class Wall {
    private long id;
    private long sourceId;
    private String sourceName;
    private String context;
    private String date;
    private String photoUrl;
    private int likesCount;
    private boolean userLikes;
    private boolean canLike;
    private int commentsCount;
    private boolean canComment;
    private int repostsCount;
    private boolean userReposted;
    private boolean canRepost;

    public Wall(long pId,
                long pSourceId,
                String pSourceName,
                String pContext,
                String pDate,
                String pPhotoUrl,
                int pLikesCount,
                boolean pUserLikes,
                boolean pCanLike,
                int pCommentsCount,
                boolean pCanComment,
                int pRepostsCount,
                boolean pUserReposted,
                boolean pCanRepost) {
        id = pId;
        sourceId = pSourceId;
        sourceName = pSourceName;
        context = pContext;
        date = pDate;
        photoUrl = pPhotoUrl;
        likesCount = pLikesCount;
        userLikes = pUserLikes;
        canLike = pCanLike;
        commentsCount = pCommentsCount;
        canComment = pCanComment;
        repostsCount = pRepostsCount;
        userReposted = pUserReposted;
        canRepost = pCanRepost;
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

    public void setSourceId(long pSourceId) {
        sourceId = pSourceId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String pDate) {
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

    public boolean getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(boolean pUserLikes) {
        userLikes = pUserLikes;
    }

    public boolean getCanLike() {
        return canLike;
    }

    public void setCanLike(boolean pCanLike) {
        canLike = pCanLike;
    }

    public boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(boolean pCanComment) {
        canComment = pCanComment;
    }

    public boolean getUserReposted() {
        return userReposted;
    }

    public void setUserReposted(boolean pUserReposted) {
        userReposted = pUserReposted;
    }

    public boolean getCanRepost() {
        return canRepost;
    }

    public void setCanRepost(boolean pCanRepost) {
        canRepost = pCanRepost;
    }

    @Override
    public String toString() {
        return "Post id" + id + "\n" +
                "Source id" + sourceId + " " + sourceName + "\n"
                + context + "\nL: " + likesCount + "| C: " + commentsCount + "| R: " +
                repostsCount + "\n" + date;
    }


}