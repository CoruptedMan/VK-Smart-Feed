package ru.ifmo.practice.model.attachments;

import ru.ifmo.practice.model.Attachment;

public class Link extends Attachment{
    private String url;
    private String title;
    private String description;
    private String caption;
    private String photoUrl;

    public Link(String pUrl, String pTitle, String pDescription, String pCaption, String pPhotoUrl) {
        url = pUrl;
        title = pTitle;
        description = pDescription;
        caption = pCaption;
        photoUrl = pPhotoUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String pCaption) {
        caption = pCaption;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String pPhotoUrl) {
        photoUrl = pPhotoUrl;
    }
}
