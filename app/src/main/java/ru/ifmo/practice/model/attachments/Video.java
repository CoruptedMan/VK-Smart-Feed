package ru.ifmo.practice.model.attachments;

public class Video {
    private long id;
    private long ownerId;
    private long date;
    private int duration;
    private int commentsCount;
    private String title;
    private String description;
    private String photoUrl;
    private String platform;
    private boolean canAdd;

    public Video(long pId, long pOwnerId, long pDate, int pDuration, int pCommentsCount, String pTitle, String pDescription, String pPhotoUrl, String pPlatform, boolean pCanAdd) {
        id = pId;
        ownerId = pOwnerId;
        date = pDate;
        duration = pDuration;
        commentsCount = pCommentsCount;
        title = pTitle;
        description = pDescription;
        photoUrl = pPhotoUrl;
        platform = pPlatform;
        canAdd = pCanAdd;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long pOwnerId) {
        ownerId = pOwnerId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long pDate) {
        date = pDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int pDuration) {
        duration = pDuration;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int pCommentsCount) {
        commentsCount = pCommentsCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String pPhotoUrl) {
        photoUrl = pPhotoUrl;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String pPlatform) {
        platform = pPlatform;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean pCanAdd) {
        canAdd = pCanAdd;
    }
}
