package ru.ifmo.practice.model.attachment;

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
    private boolean isLive;

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
                 Photo pPhoto,
                 boolean pLive) {
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
        isLive = pLive;
    }

    public long getOwnerId() {
        return ownerId;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int pDuration) {
        duration = pDuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public Photo getPhoto() {
        return photo;
    }

    public String getPlatform() {
        return platform;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean pLive) {
        isLive = pLive;
    }
}
