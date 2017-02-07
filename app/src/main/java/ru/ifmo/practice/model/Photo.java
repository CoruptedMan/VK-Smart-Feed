package ru.ifmo.practice.model;

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
    private String thumbnailUrl;
    private String[] urls;

    public Photo(long pId,
                 long pAlbumId,
                 long pOwnerId,
                 long pUserId,
                 long pPostId,
                 int pWidth,
                 int pHeight,
                 int pLikesCount,
                 boolean pUserLikes,
                 int pCommentsCount,
                 boolean pCanComment,
                 int pRepostsCount,
                 boolean pCanRepost,
                 String pDescriptionText,
                 String pDate,
                 String pAccessKey,
                 String pThumbnailUrl,
                 String[] pUrls) {
        id = pId;
        albumId = pAlbumId;
        ownerId = pOwnerId;
        userId = pUserId;
        postId = pPostId;
        width = pWidth;
        height = pHeight;
        likesCount = pLikesCount;
        userLikes = pUserLikes;
        commentsCount = pCommentsCount;
        canComment = pCanComment;
        repostsCount = pRepostsCount;
        canRepost = pCanRepost;
        descriptionText = pDescriptionText;
        date = pDate;
        accessKey = pAccessKey;
        thumbnailUrl = pThumbnailUrl;
        urls = pUrls;
    }

    public Photo(String pThumbnailUrl) {
        thumbnailUrl = pThumbnailUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long pAlbumId) {
        albumId = pAlbumId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long pOwnerId) {
        ownerId = pOwnerId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long pPostId) {
        postId = pPostId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int pWidth) {
        width = pWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int pHeight) {
        height = pHeight;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String pDescriptionText) {
        descriptionText = pDescriptionText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String pDate) {
        date = pDate;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String pAccessKey) {
        accessKey = pAccessKey;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] pUrls) {
        urls = pUrls;
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

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int pCommentsCount) {
        commentsCount = pCommentsCount;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public void setCanComment(boolean pCanComment) {
        canComment = pCanComment;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int pRepostsCount) {
        repostsCount = pRepostsCount;
    }

    public boolean isCanRepost() {
        return canRepost;
    }

    public void setCanRepost(boolean pCanRepost) {
        canRepost = pCanRepost;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long pUserId) {
        userId = pUserId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
