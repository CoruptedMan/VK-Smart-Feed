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

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.util.FeedRecyclerViewAdapter;
import ru.ifmo.practice.util.OnDownloadFeedDataResultDelegate;
import ru.ifmo.practice.util.UpdateUserFeed;

public class MainActivity extends AppCompatActivity implements OnDownloadFeedDataResultDelegate {

    private FeedRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Note> mNotes;
    private boolean mLoading = true;
    private final int MIN_NOTES_COUNT = 6;
    private int visibleThreshold = MIN_NOTES_COUNT;
    private int totalItemCount = 0;
    private int previousTotal = 0;
    public int index = -1;
    public int top = -1;
    private int firstVisibleItem;
    private int visibleItemCount;
    public static String mStartFrom = "";

    public String getStartFrom() {
        return mStartFrom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotes = new ArrayList<>();

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayShowCustomEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                previousTotal = 0;
                mStartFrom = "";
                try {
                    toggleSwipeContainerRefreshingState(true);
                    addData();
                } catch (ExecutionException | InterruptedException pE) {
                    pE.printStackTrace();
                }
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        });

        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
                startActivity(new Intent(getApplicationContext(), AppStartActivity.class));
                overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new FeedRecyclerViewAdapter(getApplicationContext(), this, null);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mLinearLayoutManager.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (mLoading) {
                    if (totalItemCount > previousTotal) {
                        mLoading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!mLoading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    try {
                        addData();
                        mLoading = true;
                    } catch (ExecutionException | InterruptedException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        });

        toggleSwipeContainerRefreshingState(true);
        try {
            addData();
        } catch (ExecutionException | InterruptedException pE) {
            pE.printStackTrace();
        }
    }

    private void addData() throws ExecutionException, InterruptedException {
        VKRequest request = new VKRequest("newsfeed.get",
                VKParameters.from("filters", "post",
                        "start_from", mStartFrom,
                        "count", MIN_NOTES_COUNT,
                        "version", "5.62"));

        new UpdateUserFeed(this).execute(request);
    }

    public void toggleSwipeContainerRefreshingState(boolean state) {
        if (swipeContainer != null)
            swipeContainer.setRefreshing(state);
    }

    @Override
    public void onPause() {
        super.onPause();

        index = mLinearLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        top = (v == null)
                ? 0
                : (v.getTop() - mRecyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();

        if(index != -1) {
            mLinearLayoutManager.scrollToPositionWithOffset(index, top);
        }
    }

    @Override
    public void taskCompletionResult(ArrayList<Note> result, String pStartFrom) {
        mNotes = result;
        if (mStartFrom.equals("")) {
            mAdapter.clear();
            mAdapter.addAll(mNotes);
            mRecyclerView.post(new Runnable() {
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    toggleSwipeContainerRefreshingState(false);
                }
            });
        } else {
            mAdapter.addAll(mNotes);
            mRecyclerView.post(new Runnable() {
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        mStartFrom = pStartFrom;
    }
}