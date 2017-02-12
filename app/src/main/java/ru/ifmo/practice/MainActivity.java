package ru.ifmo.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.util.FeedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int MIN_NOTES_COUNT = 6;
    private JSONObject mResponse;
    private FeedRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private int visibleThreshold = MIN_NOTES_COUNT;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int previousTotal = 0;
    private long mOffset = 0;
    private boolean mLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        final RecyclerView rv = (RecyclerView) findViewById(R.id.feed_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);
        mAdapter = new FeedRecyclerViewAdapter(getApplicationContext(), addData(mOffset));
        rv.setAdapter(mAdapter);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                rv.post(new Runnable() {
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
                mOffset = 0;
                previousTotal = 0;
                mAdapter.addAll(addData(mOffset));
                rv.post(new Runnable() {
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                });
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

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mLinearLayoutManager.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                Log.v("onScrolled", String.valueOf("Loading before: " + mLoading));
                Log.v("onScrolled", String.valueOf("totalItemCount: " + totalItemCount));
                Log.v("onScrolled", String.valueOf("previousTotal: " + previousTotal));
                if (mLoading) {
                    if (totalItemCount > previousTotal) {
                        mLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                Log.v("onScrolled", String.valueOf("Loading after: " + mLoading));

                Log.v("onScrolled", String.valueOf("totalItemCount: " + totalItemCount));
                Log.v("onScrolled", String.valueOf("previousTotal: " + previousTotal));
                Log.v("onScrolled", String.valueOf("visibleItemCount: " + visibleItemCount));
                Log.v("onScrolled", String.valueOf("firstVisibleItem: " + firstVisibleItem));
                Log.v("onScrolled", String.valueOf("visibleThreshold: " + visibleThreshold));
                if (!mLoading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    Log.v("onScrolled", "End!");
                    ArrayList<Note> tmpList = addData(mOffset);
                    mAdapter.addAll(tmpList);
                    recyclerView.post(new Runnable() {
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mLoading = true;
                        }
                    });
                }
                Log.e("onScrolled", String.valueOf("Loading end: " + mLoading));
            }
        });
    }

    private ArrayList<Note> addData(long offset) {
        Log.e("addData", String.valueOf("mOffset: " + offset));
        ArrayList<Note> results = new ArrayList<>();
        if (offset == 0) {
            results.add(new Note(AppStartActivity.mAccount.getId(),
                    -1,
                    AppStartActivity.mAccount.getFirstName()
                            + " " + AppStartActivity.mAccount.getLastName(),
                    "", "", -1,
                    AppStartActivity.mAccount.getPhotoUrl(),
                    null, -1, false, -1, false, -1, false));
        }
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from("filters",
                "post", "end_time", offset, "count", MIN_NOTES_COUNT));
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
                Log.e("addData", String.valueOf("id: " + id));
                String sourceName = "null";
                String photoUrl = "null";
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
                Log.e("addData", String.valueOf("date: " + date));
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
            mOffset = results.get(results.size() - 1).getDate();
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return results;
    }
}