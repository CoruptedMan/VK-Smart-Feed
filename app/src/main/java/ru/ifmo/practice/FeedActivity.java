package ru.ifmo.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.ifmo.practice.adapter.FeedRecyclerViewAdapter;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.util.OnDownloadFeedDataResultDelegate;
import ru.ifmo.practice.util.task.DownloadUserFeedDataTask;

import static ru.ifmo.practice.util.AppConsts.MIN_NOTES_COUNT;

public class FeedActivity extends AppCompatActivity implements OnDownloadFeedDataResultDelegate {

    private         CoordinatorLayout       coordinatorLayout;
    private         ProgressBar             progressBar;
    private         SwipeRefreshLayout      swipeContainer;
    private         RelativeLayout          noInternetPlaceholder;
    private         Button                  noInternetPlaceholderButton;
    private         RecyclerView            mRecyclerView;
    private         LinearLayoutManager     mLinearLayoutManager;
    private         FeedRecyclerViewAdapter mAdapter;
    private         ArrayList<Note>         mNotes;
    private         Snackbar                mSnackbarRefresh;
    private         Snackbar                mSnackbarNoInternetError;
    private         int                     visibleThreshold = MIN_NOTES_COUNT;
    private         int                     totalItemCount = 0;
    private         int                     previousTotal = 0;
    private         int                     firstVisibleItem;
    private         int                     visibleItemCount;
    private         boolean                 isDataRelevant = true;
    private         boolean                 loading = true;
    public          int                     index = -1;
    public          int                     top = -1;
    public  static  String                  mStartFrom = "";

    public String getStartFrom() {
        return mStartFrom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
        noInternetPlaceholder = (RelativeLayout) findViewById(R.id.no_internet_placeholder);
        noInternetPlaceholderButton = (Button) findViewById(R.id.no_internet_placeholder_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);

        mSnackbarRefresh = Snackbar.make(coordinatorLayout, "Есть новые записи.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Показать", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSnackbarRefresh.dismiss();
                        refreshFeed();
                    }
                });
        mSnackbarRefresh.setActionTextColor(ContextCompat.getColor(getApplicationContext(),
                R.color.color_accent));
        mSnackbarRefresh.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                R.color.color_black_transparent_light));

        mSnackbarNoInternetError = Snackbar.make(coordinatorLayout, getResources().getString(R
                .string.no_internet), Snackbar.LENGTH_LONG);
        mSnackbarNoInternetError.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                R.color.color_black_transparent_light));
        mSnackbarNoInternetError.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                mSnackbarRefresh.show();
            }
        });
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayShowCustomEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
                if (!isDataRelevant) {
                    refreshFeed();
                    mSnackbarRefresh.dismiss();
                }
            }
        });

        noInternetPlaceholderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VKSmartFeedApplication.isOnline()) {
                    try {
                        addData();
                        noInternetPlaceholder.setVisibility(View.GONE);
                    } catch (InterruptedException | ExecutionException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
                if (isDataRelevant) {
                    mSnackbarRefresh.dismiss();
                }
            }
        });
        swipeContainer.setColorSchemeResources(R.color.color_primary, R.color.color_primary_dark);

        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        mNotes = new ArrayList<>();
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

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    try {
                        if (VKSmartFeedApplication.isOnline()) {
                            addData();
                            loading = true;
                        }
                    } catch (ExecutionException | InterruptedException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        });
        if (VKSmartFeedApplication.isOnline()) {
            try {
                addData();
            } catch (ExecutionException | InterruptedException pE) {
                pE.printStackTrace();
            }
        } else {
            noInternetPlaceholder.setVisibility(View.VISIBLE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (this) {
                            wait(5000);
                            if (!VKSmartFeedApplication.isOnline()) {
                                continue;
                            }
                            if (mAdapter.getDataSet().size() > 0) {
                                final Long[] resultDate = {mAdapter.getDataSet().get(0).getDate()};
                                new VKRequest("newsfeed.get", VKParameters.from(
                                        "filters", "post",
                                        "count", 1))
                                        .executeSyncWithListener(new VKRequest.VKRequestListener() {
                                            @Override
                                            public void onComplete(VKResponse response) {
                                                try {
                                                    resultDate[0] = Long.parseLong(response
                                                            .json
                                                            .getJSONObject("response")
                                                            .getJSONArray("items")
                                                            .getJSONObject(0)
                                                            .get("date").toString());
                                                } catch (JSONException pE) {
                                                    pE.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(VKError error) {
                                                Log.e("newsfeedGetRequest", error.toString());
                                            }
                                        });
                                if (mAdapter.getDataSet().get(0).getDate() != resultDate[0]) {
                                    if (isDataRelevant) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSnackbarRefresh.show();
                                            }
                                        });
                                    }
                                    isDataRelevant = false;
                                }
                            } else {
                                if (VKSmartFeedApplication.isOnline()) {
                                    try {
                                        addData();
                                    } catch (ExecutionException | InterruptedException pE) {
                                        pE.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void addData() throws ExecutionException, InterruptedException {
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from("filters", "post",
                "start_from", mStartFrom,
                "count", MIN_NOTES_COUNT));

        new DownloadUserFeedDataTask(this).execute(request);
    }

    private void toggleSwipeContainerRefreshingState(boolean state) {
        if (swipeContainer != null)
            swipeContainer.setRefreshing(state);
    }

    public void refreshFeed() {
        mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
        previousTotal = 0;
        mStartFrom = "";
        toggleSwipeContainerRefreshingState(true);
        try {
            if (VKSmartFeedApplication.isOnline()) {
                addData();
            } else {
                mSnackbarNoInternetError.show();
                toggleSwipeContainerRefreshingState(false);
            }
        } catch (ExecutionException | InterruptedException pE) {
            pE.printStackTrace();
        }
        isDataRelevant = true;
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
                    mRecyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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
        if (noInternetPlaceholder.getVisibility() == View.VISIBLE)
            noInternetPlaceholder.setVisibility(View.GONE);
    }
}