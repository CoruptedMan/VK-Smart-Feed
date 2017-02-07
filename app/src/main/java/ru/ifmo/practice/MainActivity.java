package ru.ifmo.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ru.ifmo.practice.model.User;
import ru.ifmo.practice.model.Wall;
import ru.ifmo.practice.utils.MyRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerViewAdapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";
    private JSONObject mResponse;
    private ArrayList<Wall> mNotes;
    private ArrayList<User> mUsers;
    private ArrayList<Group> mGroups;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsers = new ArrayList<>();
        mGroups = new ArrayList<>();
        mNotes = getDataSet();

        RecyclerView lRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lRecyclerView.setHasFixedSize(true);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(mNotes);
        lRecyclerView.setAdapter(mAdapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //mNotes.clear();
                mAdapter.clear();
                //mNotes = getDataSet();
                mAdapter.addAll(getDataSet());
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);


        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
                startActivity(new Intent(getApplicationContext(), AppStartActivity.class));
                overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
                finish();
            }
        });
    }

    private ArrayList<Wall> getDataSet() {
        ArrayList<Wall> results = new ArrayList<>();
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS,
                "post", VKApiConst.COUNT, "5"));
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
            for (int index = 0; index < 5; index++) {
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
                boolean canLike = !userLikes;
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
                boolean canRepost = !userReposted;

                Wall note = new Wall(id,
                                    sourceId,
                                    sourceName,
                                    context,
                                    date,
                                    photoUrl,
                                    likes,
                                    userLikes,
                                    canLike,
                                    comments,
                                    canComment,
                                    reposts,
                                    userReposted,
                                    canRepost);
                results.add(note);
            }
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return results;
    }
}