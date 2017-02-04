package ru.ifmo.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;

import ru.ifmo.practice.utils.DataObject;
import ru.ifmo.practice.utils.EndlessRecyclerViewScrollListener;
import ru.ifmo.practice.utils.MyRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerViewAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static String LOG_TAG = "CardViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("before");
        RecyclerView lRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lRecyclerView.setHasFixedSize(true);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        lRecyclerView.setAdapter(mAdapter);
        System.out.println("after");

        /*scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                long startTime = mAdapter.getDataSet().get(mAdapter.getItemCount() - 1).getDate();
                mAdapter.getDataSet().clear();
                //mAdapter.notifyDataSetChanged();
                scrollListener.resetState();
                mAdapter.setDataSet(getDataSet(startTime));
            }
        };*/
        //lRecyclerView.addOnScrollListener(scrollListener);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
                startActivity(new Intent(getApplicationContext(), AppStartActivity.class));
                overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
                finish();
            }
        });

        final VKRequest request = new VKRequest("account.getProfileInfo");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    Toast.makeText(getBaseContext(), "Hello " +
                            response.json.getJSONObject("response").get
                                    ("first_name").toString() +
                            response.json.getJSONObject("response").get
                                    ("last_name").toString(), Toast
                            .LENGTH_LONG).show();
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
                //greetingText.setText(response.json.getJSONObject("response").get
                //("first_name").toString());
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        final ArrayList<DataObject> results = new ArrayList<>();
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS,
                "post", VKApiConst.COUNT, "5"));
        final long[] id = new long[5];
        final String[] context = new String[5];
        final String[] sourceName = new String[5];
        final Date[] date = new Date[5];
        final JSONObject[] res = new JSONObject[1];
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                System.out.println("responding");
               // res[0] = response.json;
                try {
                    for (int index = 0; index < 4; index++) {
                        id[index] = Integer
                                    .parseInt(
                                        response
                                        .json
                                        .getJSONObject("response")
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .get("source_id")
                                        .toString());
                        /*final boolean group;
                        VKRequest sourceNameRequest;
                        if (id[index] < 0) {
                            group = true;
                            id[index] = Math.abs(id[index]);
                            sourceNameRequest = new VKRequest("groups.getById",
                                    VKParameters.from("group_id", id));
                        } else {
                            group = false;
                            sourceNameRequest = VKApi.users().get(
                                    VKParameters.from(VKApiConst.USER_IDS, id));
                        }*/
                        /*sourceNameRequest.executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                try {
                                    if (group) {
                                        sourceName[0] = response
                                                        .json
                                                        .getJSONArray("response")
                                                        .getJSONObject(0)
                                                        .get("name")
                                                        .toString();
                                    } else {
                                        sourceName[0] = response
                                                        .json
                                                        .getJSONArray("response")
                                                        .getJSONObject(0)
                                                        .get("first_name")
                                                        .toString()
                                                        +
                                                        response
                                                        .json
                                                        .getJSONArray("response")
                                                        .getJSONObject(0)
                                                        .get("last_name")
                                                        .toString();
                                    }
                                    System.out.println(sourceName[0]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(VKError error) {

                            }
                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

                            }
                        });*/
                        context[index] = response
                                        .json
                                        .getJSONObject("response")
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .get("text")
                                        .toString();
                        long unixTime = Integer
                                        .parseInt(
                                                response
                                                .json
                                                .getJSONObject("response")
                                                .getJSONArray("items")
                                                .getJSONObject(index)
                                                .get("date")
                                                .toString());
                        date[index] = new Date(unixTime * 1000);
                        DataObject obj = new DataObject(
                                                        id[index],
                                                        "test",
                                                        context[index],
                                                        date[index]);
                        results.add(index, obj);
                        System.out.println("end of iteration");
                    }
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
        return results;
    }

    /*private ArrayList<DataObject> getDataSet(long time) {
        final ArrayList<DataObject> results = new ArrayList<>();
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS,
                "post", "start_time", time, VKApiConst.COUNT, "20", VKApiConst.VERSION,
                "5.62"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    for (int index = 0; index < 20; index++) {
                        long id = Integer
                                .parseInt(
                                        response
                                        .json
                                        .getJSONObject("response")
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .get("source_id")
                                        .toString());

                        boolean group = id < 0;

                        String sourceName = group
                                            ? String.valueOf(Math.abs(id))
                                            : String.valueOf(id);

                        String context = response
                                        .json
                                        .getJSONObject("response")
                                        .getJSONArray("items")
                                        .getJSONObject(index)
                                        .get("text")
                                        .toString();

                        long date = Integer
                                        .parseInt(
                                                response
                                                .json
                                                .getJSONObject("response")
                                                .getJSONArray("items")
                                                .getJSONObject(index)
                                                .get("date")
                                                .toString());

                        DataObject obj = new DataObject(id, group, sourceName, context, date);
                        results.add(index, obj);
                    }
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {
                System.out.println(error.toString());
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                System.out.println("Attempt failed");
            }
        });
        return results;
    }*/
}