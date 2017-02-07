package ru.ifmo.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.ifmo.practice.model.Group;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.Photo;
import ru.ifmo.practice.model.User;
import ru.ifmo.practice.util.FeedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private FeedRecyclerViewAdapter mAdapter;
    private JSONObject mResponse;
    private ArrayList<Note> mNotes;
    private ArrayList<User> mUsers;
    private ArrayList<Group> mGroups;
    private SwipeRefreshLayout swipeContainer;
    private static final int MIN_NOTES_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsers = new ArrayList<>();
        mGroups = new ArrayList<>();
        mNotes = getDataSet();

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
            //ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
            ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        }
        RecyclerView lRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lRecyclerView.setHasFixedSize(true);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FeedRecyclerViewAdapter(getApplicationContext(), mNotes);
        lRecyclerView.setAdapter(mAdapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                mAdapter.addAll(getDataSet());
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
                startActivity(new Intent(getApplicationContext(), AppStartActivity.class));
                overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
                finish();
            }
        });
    }

    private ArrayList<Note> getDataSet() {
        ArrayList<Note> results = new ArrayList<>();
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS,
                "post", VKApiConst.COUNT, MIN_NOTES_COUNT));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    mResponse = response.json.getJSONObject("response");
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getParent().getApplicationContext(), error.toString(), Toast
                        .LENGTH_LONG).show();
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Toast.makeText(getParent().getApplicationContext(), "Attempt Failed!", Toast
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
                String sourceName = "null";
                String photoUrl = "null";
                int groupCount = mResponse.getJSONArray("groups").length();
                for (int i = 0; i < groupCount; i++) {
                    int groupId = Integer.parseInt(mResponse.getJSONArray("groups").getJSONObject(i)
                            .get("id").toString());
                    String groupName = mResponse.getJSONArray("groups").getJSONObject(i).get
                            ("name").toString();
                    mGroups.add(new Group(groupId, groupName));
                    if (groupId == sourceId) {
                        sourceName = groupName;
                        photoUrl = mResponse.getJSONArray("groups").getJSONObject(i).get
                                ("photo_100").toString();
                    }
                }

                int userCount = mResponse.getJSONArray("profiles").length();
                for (int i = 0; i < userCount; i++) {
                    int userId = Integer.parseInt(mResponse.getJSONArray("profiles").getJSONObject(i)
                            .get("id").toString());
                    String userFirstName = mResponse.getJSONArray("profiles").getJSONObject(i).get
                            ("first_name").toString();
                    String userLastName = mResponse.getJSONArray("profiles").getJSONObject(i).get
                            ("last_name").toString();
                    mUsers.add(new User(userId, userFirstName, userLastName));
                    if (userId == sourceId) {
                        sourceName = userFirstName + " " + userLastName;
                        photoUrl = mResponse.getJSONArray("profiles").getJSONObject(i).get
                                ("photo_100").toString();
                    }
                }

                String context = mResponse
                                    .getJSONArray("items")
                                    .getJSONObject(index)
                                    .get("text")
                                    .toString();
                long unixTime = Integer.parseInt(mResponse
                                                    .getJSONArray("items")
                                                    .getJSONObject(index)
                                                    .get("date")
                                                    .toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM 'в' H:mm", Locale
                        .getDefault());
                String date = dateFormat.format(new Date(unixTime * 1000));

                int i = 0;
                ArrayList<Photo> attachmentsPhotos = new ArrayList<>();
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
                            attachmentsPhotos.add(new Photo(
                                    mResponse
                                            .getJSONArray("items")
                                            .getJSONObject(index)
                                            .getJSONArray("attachments")
                                            .getJSONObject(i)
                                            .getJSONObject("photo")
                                            .get("photo_604")
                                            .toString()));
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
}