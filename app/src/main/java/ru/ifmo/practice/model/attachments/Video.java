package ru.ifmo.practice.model.attachments;

public class Video {
    private long id;
    private long ownerId;
    private long date;
    private int duration;
    private int commentsCount;
    private int viewsCount;
    private String title;
    private String description;
    private String platform;
    private String accessKey;
    private Photo photo;

    public Video(long pId,
                 long pOwnerId,
                 long pDate,
                 int pDuration,
                 int pCommentsCount,
                 int pViewsCount,
                 String pTitle,
                 String pDescription,
                 String pPlatform,
                 String pAccessKey,
                 Photo pPhoto) {
        id = pId;
        ownerId = pOwnerId;
        date = pDate;
        duration = pDuration;
        commentsCount = pCommentsCount;
        viewsCount = pViewsCount;
        title = pTitle;
        description = pDescription;
        platform = pPlatform;
        accessKey = pAccessKey;
        photo = pPhoto;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long pOwnerId) {
        ownerId = pOwnerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int pViewsCount) {
        viewsCount = pViewsCount;
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo pPhoto) {
        photo = pPhoto;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String pPlatform) {
        platform = pPlatform;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String pAccessKey) {
        accessKey = pAccessKey;
    }
}
