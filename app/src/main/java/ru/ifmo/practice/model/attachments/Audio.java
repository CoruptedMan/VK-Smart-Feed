package ru.ifmo.practice.model.attachments;

public class Audio {
    private long id;
    private String artist;
    private String title;
    private int duration;  // in seconds
    private String url;


    public Audio(long pId, String pArtist, String pTitle, int pDuration, String pUrl) {
        id = pId;
        artist = pArtist;
        title = pTitle;
        duration = pDuration;
        url = pUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
    }
}
