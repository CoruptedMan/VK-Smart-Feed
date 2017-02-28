package ru.ifmo.practice.model;

import java.util.ArrayList;

import ru.ifmo.practice.model.attachments.Link;
import ru.ifmo.practice.model.attachments.Page;
import ru.ifmo.practice.model.attachments.Photo;
import ru.ifmo.practice.model.attachments.Video;

public class Note {
    private int                 likesCount;
    private int                 commentsCount;
    private int                 repostsCount;
    private long                id;
    private long                sourceId;
    private long                date;
    private Link                attachedLink;
    private Page                attachedPage;
    private Photo               sourcePhoto;
    private String              sourceName;
    private String              context;
    private String              contextPreview;
    private Account             signer;
    private boolean             userLikes;
    private boolean             canComment;
    private boolean             userReposted;
    private boolean             isAudioAttached;
    private ArrayList<Photo>    attachmentsPhotos;
    private ArrayList<Video>    attachmentsVideos;
    //private ArrayList<Audio>    attachmentsAudios;

    public Note(long pId,
                long pSourceId,
                String pSourceName,
                String pContext,
                String pContextPreview,
                long pDate,
                Photo pSourcePhoto,
                ArrayList<Photo> pAttachmentsPhotos,
                ArrayList<Video> pAttachmentsVideos,
                boolean pIsAudioAttached,
                int pLikesCount,
                boolean pUserLikes,
                int pCommentsCount,
                boolean pCanComment,
                int pRepostsCount,
                boolean pUserReposted) {
        id = pId;
        sourceId = pSourceId;
        sourceName = pSourceName;
        context = pContext;
        contextPreview = pContextPreview;
        date = pDate;
        sourcePhoto = pSourcePhoto;
        attachmentsPhotos = pAttachmentsPhotos;
        attachmentsVideos = pAttachmentsVideos;
        isAudioAttached = pIsAudioAttached;
        likesCount = pLikesCount;
        userLikes = pUserLikes;
        commentsCount = pCommentsCount;
        canComment = pCanComment;
        repostsCount = pRepostsCount;
        userReposted = pUserReposted;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int pLikesCount) {
        likesCount = pLikesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int pCommentsCount) {
        commentsCount = pCommentsCount;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int pRepostsCount) {
        repostsCount = pRepostsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long pSourceId) {
        sourceId = pSourceId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long pDate) {
        date = pDate;
    }

    public Link getAttachedLink() {
        return attachedLink;
    }

    public void setAttachedLink(Link pAttachedLink) {
        attachedLink = pAttachedLink;
    }

    public Page getAttachedPage() {
        return attachedPage;
    }

    public void setAttachedPage(Page pAttachedPage) {
        attachedPage = pAttachedPage;
    }

    public Photo getSourcePhoto() {
        return sourcePhoto;
    }

    public void setSourcePhoto(Photo pSourcePhoto) {
        sourcePhoto = pSourcePhoto;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String pSourceName) {
        sourceName = pSourceName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String pContext) {
        context = pContext;
    }

    public String getContextPreview() {
        return contextPreview;
    }

    public void setContextPreview(String pContextPreview) {
        contextPreview = pContextPreview;
    }

    public Account getSigner() {
        return signer;
    }

    public void setSigner(Account pSigner) {
        signer = pSigner;
    }

    public boolean isUserLikes() {
        return userLikes;
    }

    public void setUserLikes(boolean pUserLikes) {
        userLikes = pUserLikes;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public void setCanComment(boolean pCanComment) {
        canComment = pCanComment;
    }

    public boolean isUserReposted() {
        return userReposted;
    }

    public void setUserReposted(boolean pUserReposted) {
        userReposted = pUserReposted;
    }

    public boolean isAudioAttached() {
        return isAudioAttached;
    }

    public void setAudioAttached(boolean pAudioAttached) {
        isAudioAttached = pAudioAttached;
    }

    public ArrayList<Photo> getAttachmentsPhotos() {
        return attachmentsPhotos;
    }

    public void setAttachmentsPhotos(ArrayList<Photo> pAttachmentsPhotos) {
        attachmentsPhotos = pAttachmentsPhotos;
    }

    public ArrayList<Video> getAttachmentsVideos() {
        return attachmentsVideos;
    }

    public void setAttachmentsVideos(ArrayList<Video> pAttachmentsVideos) {
        attachmentsVideos = pAttachmentsVideos;
    }

    @Override
    public String toString() {
        return "Post id" + id + "\n" +
                "Source id" + sourceId + " " + sourceName + "\n"
                + context + "\nL: " + likesCount + "| C: " + commentsCount + "| R: " +
                repostsCount + "\n"
                + "Photos: " + attachmentsPhotos.size() + "\n"
                + date;
    }
}