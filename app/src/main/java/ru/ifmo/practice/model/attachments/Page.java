package ru.ifmo.practice.model.attachments;

public class Page {
    private long id;
    private String title;
    private String viewUrl;

    public Page(long pId, String pTitle, String pViewUrl) {
        id = pId;
        title = pTitle;
        viewUrl = pViewUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String pViewUrl) {
        viewUrl = pViewUrl;
    }
}
