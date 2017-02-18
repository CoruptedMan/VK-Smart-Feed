package ru.ifmo.practice.model.attachments;

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
    private String url;

    public Photo(long pId, long pOwnerId, long pAlbumId, String pUrl, String pDate, int pWidth, int
            pHeight) {
        id = pId;
        ownerId = pOwnerId;
        albumId = pAlbumId;
        url = pUrl;
        date = pDate;
        width = pWidth;
        height = pHeight;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
    }
}
