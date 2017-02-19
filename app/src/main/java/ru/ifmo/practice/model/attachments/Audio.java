package ru.ifmo.practice.model.attachments;

public class Audio {
    private long id;
    private long ownerId;
    private String artist;
    private String title;
    private int duration;  // in seconds
    private long date;
    private String url;


    public Audio(long pId, long pOwnerId, String pArtist, String pTitle, int pDuration, long pDate, String pUrl) {
        id = pId;
        ownerId = pOwnerId;
        artist = pArtist;
        title = pTitle;
        duration = pDuration;
        date = pDate;
        url = pUrl;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String pArtist) {
        artist = pArtist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int pDuration) {
        duration = pDuration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long pDate) {
        date = pDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
    }
}
