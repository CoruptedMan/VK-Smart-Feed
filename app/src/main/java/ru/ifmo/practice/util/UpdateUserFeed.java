package ru.ifmo.practice.util;

import android.os.AsyncTask;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ifmo.practice.AppStartActivity;
import ru.ifmo.practice.MainActivity;
import ru.ifmo.practice.model.Note;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

public class UpdateUserFeed extends AsyncTask<VKRequest, Void, ArrayList<Note>> {
    private JSONObject mResponse;
    private MainActivity mActivity;
    //private OnDownloadFeedDataResultDelegate mDelegate;
    private String mStartFrom;
    private final int MIN_NOTES_COUNT = 6;

    public UpdateUserFeed(MainActivity pActivity/*, OnDownloadFeedDataResultDelegate pDelegate*/) {
        mActivity = pActivity;
        mStartFrom = mActivity.getStartFrom();
        //mDelegate = pActivity;
    }

    @Override
    protected ArrayList<Note> doInBackground(VKRequest... params) {
        ArrayList<Note> results = new ArrayList<>();
        if (mStartFrom.equals("")) {
            results.add(new Note(AppStartActivity.mAccount.getId(),
                    -1,
                    AppStartActivity.mAccount.getFirstName()
                            + " " + AppStartActivity.mAccount.getLastName(),
                    "", "", -1,
                    AppStartActivity.mAccount.getPhotoUrl(),
                    null, -1, false, -1, false, -1, false));
        }
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
                //Log.i("addData", String.valueOf("id: " + id));
                String sourceName = "";
                String photoUrl = "";
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
                    if (groupId == sourceId) {
                        sourceName = groupName;
                        photoUrl = mResponse
                                .getJSONArray("groups")
                                .getJSONObject(i)
                                .get("photo_100")
                                .toString();
                    }
                }

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
                    if (userId == sourceId) {
                        sourceName = userFirstName + " " + userLastName;
                        photoUrl = mResponse
                                .getJSONArray("profiles")
                                .getJSONObject(i)
                                .get("photo_100")
                                .toString();
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
                //Log.i("addData", String.valueOf("date: " + date));
                int i = 0;
                ArrayList<String> attachmentsPhotos = new ArrayList<>();
                if (mResponse
                        .getJSONArray("items")
                        .getJSONObject(index)
                        .optJSONArray("attachments") != null) {
                    while (!mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .getJSONArray("attachments")
                            .isNull(i)) {
                        if (mResponse
                                .getJSONArray("items")
                                .getJSONObject(index)
                                .getJSONArray("attachments")
                                .getJSONObject(i)
                                .get("type")
                                .toString()
                                .equals("photo")) {
                            attachmentsPhotos.add(mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .getJSONArray("attachments")
                                    .getJSONObject(i)
                                    .getJSONObject("photo")
                                    .get("photo_604")
                                    .toString());
                        }
                        i++;
                    }
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
                        photoUrl,
                        attachmentsPhotos,
                        likes,
                        userLikes,
                        comments,
                        canComment,
                        reposts,
                        userReposted);
                results.add(note);
            }
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<Note> result) {
        mActivity.taskCompletionResult(result, mStartFrom);
    }
}
