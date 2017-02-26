package ru.ifmo.practice.model.attachments;

public class Link {
    private String url;
    private String title;
    private String description;
    private String caption;
    private Photo photo;
    private boolean isExternal;
    private int photoType;

    public Link(String pUrl,
                String pTitle,
                String pDescription,
                String pCaption,
                Photo pPhoto,
                boolean pIsExternal,
                int pPhotoType) {
        url = pUrl;
        title = pTitle;
        description = pDescription;
        caption = pCaption;
        photo = pPhoto;
        isExternal = pIsExternal;
        photoType = pPhotoType;
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo pPhoto) {
        photo = pPhoto;
    }

    public boolean isExternal() {
        return isExternal;
    }

    public void setExternal(boolean pExternal) {
        isExternal = pExternal;
    }

    public int getPhotoType() {
        return photoType;
    }

    public void setPhotoType(int pPhotoType) {
        photoType = pPhotoType;
    }
}
