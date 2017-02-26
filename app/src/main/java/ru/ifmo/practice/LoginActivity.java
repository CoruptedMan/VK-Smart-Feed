package ru.ifmo.practice;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;

import me.relex.circleindicator.CircleIndicator;
import ru.ifmo.practice.model.Account;

public class LoginActivity extends Activity {

    public static Account mAccount;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_login);
        TextView lLogoText = (TextView) findViewById(R.id.logoType);
        ViewPager lViewPager = (ViewPager) findViewById(R.id.container);
        CircleIndicator lCircleIndicator = (CircleIndicator) findViewById(R.id.attachment_photo_indicator);
        Typeface bukhari = Typeface.createFromAsset(getAssets(), "fonts/Bukhari.otf");
        lLogoText.setTypeface(bukhari);

        findViewById(R.id.btn_change_scene).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(LoginActivity.this, "friends", "wall", "video");
            }
        });

        SectionsPagerAdapter lSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        lViewPager.setAdapter(lSectionsPagerAdapter);
        lCircleIndicator.setViewPager(lViewPager);
        if (VKAccessToken.currentToken() != null && !VKAccessToken.currentToken().isExpired()) {
            VKRequest request = new VKRequest("users.get", VKParameters.from("fields",
                    "photo_100"));
            request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    try {
                        long userId = Integer.parseInt(response
                                .json
                                .getJSONArray("response")
                                .getJSONObject(0)
                                .get("id")
                                .toString());
                        String firstName = response
                                .json
                                .getJSONArray("response")
                                .getJSONObject(0)
                                .get("first_name")
                                .toString();
                        String lastName = response
                                .json
                                .getJSONArray("response")
                                .getJSONObject(0)
                                .get("last_name")
                                .toString();
                        String photoUrl = response
                                .json
                                .getJSONArray("response")
                                .getJSONObject(0)
                                .get("photo_100")
                                .toString();
                        mAccount = new Account(userId, firstName, lastName, photoUrl);
                    } catch (JSONException pE) {
                        pE.printStackTrace();
                    }
                }

                @Override
                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    super.attemptFailed(request, attemptNumber, totalAttempts);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                }
            });
            startActivity(new Intent(getApplicationContext(), FeedActivity.class));
            overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                VKRequest request = new VKRequest("users.get", VKParameters.from("fields",
                        "photo_100"));
                request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        try {
                            long userId = Integer.parseInt(response
                                    .json
                                    .getJSONArray("response")
                                    .getJSONObject(0)
                                    .get("id")
                                    .toString());
                            String firstName = response
                                    .json
                                    .getJSONArray("response")
                                    .getJSONObject(0)
                                    .get("first_name")
                                    .toString();
                            String lastName = response
                                    .json
                                    .getJSONArray("response")
                                    .getJSONObject(0)
                                    .get("last_name")
                                    .toString();
                            String photoUrl = response
                                    .json
                                    .getJSONArray("response")
                                    .getJSONObject(0)
                                    .get("photo_100")
                                    .toString();
                            mAccount = new Account(userId, firstName, lastName, photoUrl);
                        } catch (JSONException pE) {
                            pE.printStackTrace();
                        }
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        super.attemptFailed(request, attemptNumber, totalAttempts);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }
                });

                startActivity(new Intent(getApplicationContext(), FeedActivity.class));
                overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
                finish();
            }
            @Override
            public void onError(VKError error) {
                //TODO: Better log it.
                //Snackbar.make(coordinatorLayout, "Auth Error!", Snackbar.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager pFragmentManager) {
            super(pFragmentManager);
        }

        @Override
        public Fragment getItem(int pPosition) {
            switch (pPosition) {
                case 0:
                    return PlaceholderFragment.newInstance(
                            getResources().getString(R.string.page_title1),
                            getResources().getString(R.string.page_description1),
                            pPosition + 1);
                case 1:
                    return PlaceholderFragment.newInstance(
                            getResources().getString(R.string.page_title2),
                            getResources().getString(R.string.page_description2),
                            pPosition + 1);
                case 2:
                    return PlaceholderFragment.newInstance(
                            getResources().getString(R.string.page_title3),
                            getResources().getString(R.string.page_description3),
                            pPosition + 1);
                case 3:
                    return PlaceholderFragment.newInstance(
                            getResources().getString(R.string.page_title4),
                            getResources().getString(R.string.page_description4),
                            pPosition + 1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String TITLE_TEXT = "title_text";
        private static final String CONTENT_TEXT = "content_text";
        private static final String ARG_SECTION_NUMBER = "section_number";

        TextView mTitleTextView;
        TextView mContentTextView;

        public static PlaceholderFragment newInstance(String pTitleText,
                                                      String pContentText,
                                                      int pSectionNumber) {
            PlaceholderFragment lFragment = new PlaceholderFragment();
            Bundle lArgs = new Bundle();
            lArgs.putString(TITLE_TEXT, pTitleText);
            lArgs.putString(CONTENT_TEXT, pContentText);
            lArgs.putInt(ARG_SECTION_NUMBER, pSectionNumber);
            lFragment.setArguments(lArgs);
            return lFragment;
        }

        @Override
        public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer,
                                 Bundle pSavedInstanceState) {
            View lRootView = pInflater.inflate(R.layout.fragment_app_start, pContainer, false);

            mTitleTextView = (TextView) lRootView.findViewById(R.id.titleText);
            mContentTextView = (TextView) lRootView.findViewById(R.id.contentText);

            mTitleTextView.setText(getString(R.string.content_format, getArguments().getString(TITLE_TEXT)));
            mContentTextView.setText(getString(R.string.content_format, getArguments().getString(CONTENT_TEXT)));

            Typeface robotoThin = Typeface.createFromAsset(getResources().getAssets(), "fonts/RobotoThin.ttf");
            Typeface robotoBlack = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/RobotoBlack.ttf");

            mTitleTextView.setTypeface(robotoBlack);
            mContentTextView.setTypeface(robotoThin);

            return lRootView;
        }
    }
}