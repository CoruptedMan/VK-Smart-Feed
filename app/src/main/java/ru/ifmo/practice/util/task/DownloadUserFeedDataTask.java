package ru.ifmo.practice.util.task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.ifmo.practice.FeedActivity;
import ru.ifmo.practice.model.Account;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.attachments.Audio;
import ru.ifmo.practice.model.attachments.Link;
import ru.ifmo.practice.model.attachments.Page;
import ru.ifmo.practice.model.attachments.Photo;
import ru.ifmo.practice.model.attachments.Video;

import static com.vk.sdk.VKUIHelper.getApplicationContext;
import static ru.ifmo.practice.util.AppConsts.MIN_NOTES_COUNT;

public class DownloadUserFeedDataTask extends AsyncTask<VKRequest, Void, ArrayList<Note>> {
    private JSONObject mResponse;
    private FeedActivity mActivity;
    private String mStartFrom;

    public DownloadUserFeedDataTask(FeedActivity pActivity) {
        mActivity = pActivity;
        mStartFrom = mActivity.getStartFrom();
    }

    @Override
    protected ArrayList<Note> doInBackground(VKRequest... params) {
        ArrayList<Note> results = new ArrayList<>();
        params[0].executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    mResponse = response.json.getJSONObject("response");
                    mStartFrom = mResponse.get("next_from").toString();
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast
                        .LENGTH_LONG).show();
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Toast.makeText(getApplicationContext(), "Attempt Failed!", Toast
                        .LENGTH_LONG).show();
            }
        });
        try {
            for (int index = 0; index < MIN_NOTES_COUNT; index++) {
                System.out.println(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .toString(2));
                long sourceId = Math.abs(Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .get("source_id")
                        .toString()));
                long id = Math.abs(Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .get("post_id")
                        .toString()));
                long signerId = -100;
                if (mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .has("signer_id")) {
                    signerId = Integer.parseInt(mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .get("signer_id")
                            .toString());
                }
                String sourceName = "";
                Photo sourcePhoto = new Photo();
                int groupCount = mResponse.getJSONArray("groups").length();
                for (int i = 0; i < groupCount; i++) {
                    int groupId = Integer.parseInt(mResponse
                            .getJSONArray("groups")
                            .getJSONObject(i)
                            .get("id")
                            .toString());
                    String groupName = mResponse
                            .getJSONArray("groups")
                            .getJSONObject(i)
                            .get("name")
                            .toString();
                    String avatarUrl;
                    if (groupId == sourceId) {
                        sourceName = groupName;
                        avatarUrl = mResponse
                                .getJSONArray("groups")
                                .getJSONObject(i)
                                .get("photo_100")
                                .toString();
                        try {
                            sourcePhoto.setPhotoUrl(avatarUrl);
                        } catch (ExecutionException | InterruptedException pE) {
                            pE.printStackTrace();
                        }
                    }
                }
                Account signer = null;
                int userCount = mResponse.getJSONArray("profiles").length();
                for (int i = 0; i < userCount; i++) {
                    int userId = Integer.parseInt(mResponse
                            .getJSONArray("profiles")
                            .getJSONObject(i)
                            .get("id")
                            .toString());
                    String userFirstName = mResponse
                            .getJSONArray("profiles")
                            .getJSONObject(i)
                            .get("first_name")
                            .toString();
                    String userLastName = mResponse
                            .getJSONArray("profiles")
                            .getJSONObject(i)
                            .get("last_name")
                            .toString();
                    String avatarUrl = "";
                    if (userId == sourceId) {
                        sourceName = userFirstName + " " + userLastName;
                        avatarUrl = mResponse
                                .getJSONArray("profiles")
                                .getJSONObject(i)
                                .get("photo_100")
                                .toString();
                        try {
                            sourcePhoto.setPhotoUrl(avatarUrl);
                        } catch (ExecutionException | InterruptedException pE) {
                            pE.printStackTrace();
                        }
                    }
                    if (userId == signerId) {
                        signer = new Account(userId,
                                userFirstName,
                                userLastName,
                                avatarUrl);
                    }
                }

                String context = mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .get("text")
                        .toString();
                String contextPreview;
                if (context.length() > 200) {
                    contextPreview = context.substring(0, 200) + "...";
                } else {
                    contextPreview = "";
                }
                long date = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .get("date")
                        .toString());

                int i = 0;
                ArrayList<Photo> attachmentsPhotos = new ArrayList<>();
                ArrayList<Audio> attachmentsAudios = new ArrayList<>();
                ArrayList<Video> attachmentsVideos = new ArrayList<>();
                Link link = null;
                Page page = null;
                if (mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .optJSONArray("attachments") != null) {
                    while (mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .getJSONArray("attachments")
                            .optJSONObject(i) != null) {
                        if (mResponse
                                .getJSONArray("items")
                                .getJSONObject(index)
                                .getJSONArray("attachments")
                                .getJSONObject(i)
                                .get("type")
                                .toString()
                                .equals("photo")) {
                            long photoId = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("id").toString());
                            long photoOwnerId = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("owner_id").toString());
                            long photoAlbumId = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("album_id").toString());
                            String photoDate = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("date").toString();
                            String photoText = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("text").toString();
                            int photoWidth = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("width").toString());
                            int photoHeight = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("height").toString());
                            String photoUrl;
                            if (photoWidth > 807) {
                                photoUrl = mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("photo")
                                        .get("photo_1280").toString();
                            }
                            else if (photoWidth > 604) {
                                photoUrl = mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("photo")
                                        .get("photo_807").toString();
                            }
                            else if (photoWidth > 130) {
                                photoUrl = mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("photo")
                                        .get("photo_604").toString();
                            }
                            else if (photoWidth > 75) {
                                photoUrl = mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("photo")
                                        .get("photo_130").toString();
                            }
                            else {
                                photoUrl = mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("photo")
                                        .get("photo_75").toString();
                            }
                            attachmentsPhotos.add(new Photo(photoId,
                                    photoOwnerId,
                                    photoAlbumId,
                                    photoUrl,
                                    photoDate,
                                    photoText,
                                    photoWidth,
                                    photoHeight));
                        }
                        else if (mResponse
                                .getJSONArray("items")
                                .getJSONObject(index)
                                .getJSONArray("attachments")
                                .getJSONObject(i)
                                .get("type")
                                .toString()
                                .equals("link")) {
                            String linkUrl = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("link")
                                    .get("url").toString();
                            String linkTitle = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("link")
                                    .get("title").toString();
                            String linkCaption = getDomainName(linkUrl);
                            String linkDescription = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("link")
                                    .get("description").toString();
                            String linkPhotoUrl = "";
                            boolean linkIsExternal = false;
                            if (mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("link")
                                    .get("target").toString().equals("external")) {
                                linkIsExternal = true;
                            }
                            int linkPhotoType = 0;
                            if (!mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("link")
                                    .has("preview_page")) {
                                if (mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("link")
                                        .has("image_big")) {
                                    linkPhotoUrl = mResponse
                                            .getJSONArray("items")
                                            .getJSONObject(index)
                                            .getJSONArray("attachments")
                                            .getJSONObject(i)
                                            .getJSONObject("link")
                                            .get("image_big").toString();
                                    linkPhotoType = 2;
                                } else if (mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("link")
                                        .has("image_src")) {
                                    linkPhotoUrl = mResponse
                                            .getJSONArray("items")
                                            .getJSONObject(index)
                                            .getJSONArray("attachments")
                                            .getJSONObject(i)
                                            .getJSONObject("link")
                                            .get("image_src").toString();
                                    linkPhotoType = 1;
                                }
                            }
                            link = new Link(linkUrl,
                                    linkTitle,
                                    linkDescription,
                                    linkCaption,
                                    new Photo(),
                                    linkIsExternal,
                                    linkPhotoType);
                            try {
                                link.getPhoto().setPhotoUrl(linkPhotoUrl);
                            } catch (ExecutionException | InterruptedException pE) {
                                pE.printStackTrace();
                            }
                        } else if (mResponse
                                .getJSONArray("items")
                                .getJSONObject(index)
                                .getJSONArray("attachments")
                                .getJSONObject(i)
                                .get("type")
                                .toString()
                                .equals("video")) {
                            long videoId = Long.parseLong(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("id").toString());
                            long videoOwnerId = Long.parseLong(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("owner_id").toString());
                            String videoTitle = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("title").toString();
                            int videoDuration = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("duration").toString());
                            int videoCommentsCount = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("comments").toString());
                            int videoViewsCount = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("views").toString());
                            String videoDescription = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("description").toString();
                            String videoPhotoUrl = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("photo_320").toString();
                            String videoAccessKey = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("access_key").toString();
                            long videoDate = Long.parseLong(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .get("date").toString());
                            String videoPlatform = "vk";
                            if (mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("video")
                                    .has("platform")) {
                                videoPlatform = mResponse
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .getJSONArray("attachments")
                                        .getJSONObject(i)
                                        .getJSONObject("video")
                                        .get("platform")
                                        .toString();
                            }
                            Video video = new Video(videoId,
                                    videoOwnerId,
                                    videoDate,
                                    videoDuration,
                                    videoCommentsCount,
                                    videoViewsCount,
                                    videoTitle,
                                    videoDescription,
                                    videoPlatform,
                                    videoAccessKey,
                                    new Photo());
                            try {
                                video.getPhoto().setPhotoUrl(videoPhotoUrl);
                            } catch (ExecutionException | InterruptedException pE) {
                                pE.printStackTrace();
                            }
                            attachmentsVideos.add(video);
                        } else if (mResponse
                                .getJSONArray("items")
                                .getJSONObject(index)
                                .getJSONArray("attachments")
                                .getJSONObject(i)
                                .get("type")
                                .toString()
                                .equals("audio")) {
                            long audioId = Long.parseLong(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("audio")
                                    .get("id").toString());
                            String audioArtist = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("audio")
                                    .get("artist").toString();
                            String audioTitle = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("audio")
                                    .get("title").toString();
                            int audioDuration = Integer.parseInt(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("audio")
                                    .get("duration").toString());
                            String audioUrl = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("audio")
                                    .get("url").toString();
                            Audio audio = new Audio(audioId,
                                    audioArtist,
                                    audioTitle,
                                    audioDuration,
                                    audioUrl);
                            attachmentsAudios.add(audio);
                        } else if (mResponse
                                .getJSONArray("items")
                                .getJSONObject(index)
                                .getJSONArray("attachments")
                                .getJSONObject(i)
                                .get("type")
                                .toString()
                                .equals("page")) {
                            long pageId = Long.parseLong(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("page")
                                    .get("id").toString());
                            String pageTitle = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("page")
                                    .get("title").toString();
                            String pageViewUrl = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("page")
                                    .get("view_url").toString();
                            page = new Page(pageId, pageTitle, pageViewUrl);
                        }
                        i++;
                    }
                }

                if (attachmentsPhotos.size() > 0 && link != null) {
                    link.setPhotoType(0);
                }

                int likes = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .getJSONObject("likes")
                        .get("count")
                        .toString());
                int comments = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .getJSONObject("comments")
                        .get("count")
                        .toString());
                int reposts = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .getJSONObject("reposts")
                        .get("count")
                        .toString());
                boolean userLikes = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .getJSONObject("likes")
                        .get("user_likes")
                        .toString()) == 1;
                boolean canComment = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .getJSONObject("comments")
                        .get("can_post")
                        .toString()) == 1;
                boolean userReposted = Integer.parseInt(mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .getJSONObject("reposts")
                        .get("user_reposted")
                        .toString()) == 1;

                Note note = new Note(id,
                        sourceId,
                        sourceName,
                        context,
                        contextPreview,
                        date,
                        sourcePhoto,
                        attachmentsPhotos,
                        attachmentsVideos,
                        attachmentsAudios,
                        likes,
                        userLikes,
                        comments,
                        canComment,
                        reposts,
                        userReposted);
                if (link != null) {
                    note.setAttachedLink(link);
                }
                if (page != null) {
                    note.setAttachedPage(page);
                }
                if (signer != null) {
                    note.setSigner(signer);
                }
                results.add(note);
            }
        } catch (JSONException | URISyntaxException pE) {
            pE.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<Note> result) {
        mActivity.taskCompletionResult(result, mStartFrom);
    }

    private String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
