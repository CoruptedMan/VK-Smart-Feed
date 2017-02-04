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
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ru.ifmo.practice.utils.DataObject;
import ru.ifmo.practice.utils.MyRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerViewAdapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";
    private JSONArray mResponse;
    private JSONObject mSourceNameResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView lRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lRecyclerView.setHasFixedSize(true);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        lRecyclerView.setAdapter(mAdapter);

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
        ArrayList<DataObject> results = new ArrayList<>();
        VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS,
                "post", VKApiConst.COUNT, "5"));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    System.out.println("keke");
                    mResponse = response.json
                            .getJSONObject("response")
                            .getJSONArray("items");
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
            for (int index = 0; index < 4; index++) {
                int id = Integer.parseInt(mResponse
                                                .getJSONObject(index)
                                                .get("source_id")
                                                .toString());
                String sourceName;
                if (id < 0) {
                    id *= -1;
                    sourceName = getSourceName(new VKRequest("groups.getById",
                            VKParameters.from("group_id", id)), true);
                } else {
                    sourceName = getSourceName(VKApi.users().get(VKParameters.from
                            (VKApiConst.USER_IDS, id)),
                            false);
                }
                String context = mResponse
                                    .getJSONObject(index)
                                    .get("text")
                                    .toString();
                long unixTime = Integer.parseInt(mResponse
                                                    .getJSONObject(index)
                                                    .get("date")
                                                    .toString());
                Date date = new Date(unixTime * 1000);
                DataObject obj = new DataObject(id, sourceName, context, date);
                results.add(index, obj);
            }
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return results;
    }

    private String getSourceName(VKRequest sourceNameRequest, boolean group) {
        sourceNameRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    System.out.println("ke");
                    mSourceNameResponse = response.json
                            .getJSONArray("response")
                            .getJSONObject(0);
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
            if (group) {
                return mSourceNameResponse
                        .get("name")
                        .toString();
            } else {
                return mSourceNameResponse
                        .get("first_name")
                        .toString()
                        + mSourceNameResponse
                        .get("last_name")
                        .toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}