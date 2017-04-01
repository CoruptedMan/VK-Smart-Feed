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

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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

public class FeedActivity
        extends AppCompatActivity
        implements OnDownloadFeedDataResultDelegate {
    @BindView(R.id.activity_main)                   CoordinatorLayout       mCoordinatorLayout;
    @BindView(R.id.progress_bar)                    ProgressBar             mProgressBar;
    @BindView(R.id.swipeContainer)                  SwipeRefreshLayout      mSwipeRefreshLayout;
    @BindView(R.id.no_internet_placeholder)         RelativeLayout          mNoInternetPlaceholder;
    @BindView(R.id.no_internet_placeholder_button)  Button                  mNoInternetPlaceholderButton;
    @BindView(R.id.feed_recycler_view)              RecyclerView            mRecyclerView;
    private                                         LinearLayoutManager     mLinearLayoutManager;
    private                                         FeedRecyclerViewAdapter mAdapter;
    private                                         ArrayList<Note>         mNotes;
    private                                         Snackbar                mSnackbarRefresh;
    private                                         Snackbar                mSnackbarNoInternetError;
    private                                         int                     mFirstVisibleItem;
    private                                         int                     mVisibleItemCount;
    private                                         int                     mVisibleThreshold       = MIN_NOTES_COUNT;
    private                                         int                     mTotalItemCount         = 0;
    private                                         int                     mPreviousTotal          = 0;
    private                                         boolean                 mIsDataRelevant         = true;
    private                                         boolean                 mLoading                = true;
    public                                          int                     mIndex                  = -1;
    public                                          int                     mTop                    = -1;
    public  static                                  String                  mStartFrom              = "";

    public Snackbar getSnackbarRefresh() {
        return mSnackbarRefresh;
    }

    public boolean isDataRelevant() {
        return mIsDataRelevant;
    }

    public String getStartFrom() {
        return mStartFrom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        mSnackbarRefresh = Snackbar.make(mCoordinatorLayout,
                getResources().getString(R.string.new_notes),
                Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.show),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSnackbarRefresh.dismiss();
                        refreshFeed();
                    }
                });
        mSnackbarRefresh
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(),
                R.color.color_accent));
        mSnackbarRefresh
                .getView()
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                R.color.color_black_transparent_light));

        mSnackbarNoInternetError = Snackbar.make(mCoordinatorLayout,
                getResources().getString(R
                .string.no_internet),
                Snackbar.LENGTH_LONG);
        mSnackbarNoInternetError
                .getView()
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
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
                if (!mIsDataRelevant) {
                    refreshFeed();
                    mSnackbarRefresh.dismiss();
                }
            }
        });

        mNoInternetPlaceholderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VKSmartFeedApplication.isOnline()) {
                    try {
                        addData();
                        mNoInternetPlaceholder.setVisibility(View.GONE);
                    } catch (InterruptedException | ExecutionException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
                if (mIsDataRelevant) {
                    mSnackbarRefresh.dismiss();
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_primary, R.color.color_primary_dark);

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

                mVisibleItemCount = mLinearLayoutManager.getChildCount();
                mTotalItemCount = mLinearLayoutManager.getItemCount();
                mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (mLoading) {
                    if (mTotalItemCount > mPreviousTotal) {
                        mLoading = false;
                        mPreviousTotal = mTotalItemCount;
                    }
                }

                if (!mLoading && (mTotalItemCount - mVisibleItemCount)
                        <= (mFirstVisibleItem + mVisibleThreshold)) {
                    try {
                        if (VKSmartFeedApplication.isOnline()) {
                            addData();
                            mLoading = true;
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
            mNoInternetPlaceholder.setVisibility(View.VISIBLE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (this) {
                            wait(2500);
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
                                    if (mIsDataRelevant) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSnackbarRefresh.show();
                                            }
                                        });
                                    }
                                    mIsDataRelevant = false;
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
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(state);
    }

    public void refreshFeed() {
        mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
        mPreviousTotal = 0;
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
        mIsDataRelevant = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        mIndex = mLinearLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        mTop = (v == null)
                ? 0
                : (v.getTop() - mRecyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mIndex != -1) {
            mLinearLayoutManager.scrollToPositionWithOffset(mIndex, mTop);
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
                    mProgressBar.setVisibility(View.GONE);
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
        if (mNoInternetPlaceholder.getVisibility() == View.VISIBLE)
            mNoInternetPlaceholder.setVisibility(View.GONE);
    }
}