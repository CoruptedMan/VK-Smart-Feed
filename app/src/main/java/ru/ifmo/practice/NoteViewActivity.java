package ru.ifmo.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.ifmo.practice.model.Comment;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.util.DownloadImageTask;
import ru.ifmo.practice.util.NoteCommentsRecyclerViewAdapter;

public class NoteViewActivity extends AppCompatActivity {

    public static Note mNote;
    private LinearLayoutManager mLinearLayoutManager;
    private NoteCommentsRecyclerViewAdapter mAdapter;
    private JSONObject mResponse;
    private TextView sourceNameText;
    private TextView contextText;
    private TextView dateText;
    private TextView likesCountText;
    private TextView commentsCountText;
    private TextView repostsCountText;
    private ImageView likeIcon;
    private ImageView commentIcon;
    private ImageView repostIcon;
    private ImageView sourcePhoto;
    private LinearLayout loadMoreCommentsLayout;
    private RelativeLayout attachBlock;
    private RelativeLayout emojiBlock;
    private RelativeLayout sendBlock;
    private RelativeLayout likeBlock;
    private RelativeLayout commentBlock;
    private RelativeLayout repostBlock;
    private RelativeLayout mainLayout;
    private CardView sourceInfoBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view_layout);

        Toolbar tb = (Toolbar) findViewById(R.id.note_view_toolbar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            //  ab.setDisplayShowHomeEnabled(true);
            //ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        tb.setNavigationIcon(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.context().getResources(),
                R.drawable.ic_reply_white_24dp,
                null));
        sourceNameText = (TextView) findViewById(R.id.note_view_source_name);
        contextText = (TextView) findViewById(R.id.note_view_context);
        dateText = (TextView) findViewById(R.id.note_view_date);
        likesCountText = (TextView) findViewById(R.id.note_view_likes_count);
        commentsCountText = (TextView) findViewById(R.id.note_view_comments_count);
        repostsCountText = (TextView) findViewById(R.id.note_view_reposts_count);
        sourcePhoto = (ImageView) findViewById(R.id.note_view_source_photo);
        likeIcon = (ImageView) findViewById(R.id.note_view_like_icon);
        commentIcon = (ImageView) findViewById(R.id.note_view_comment_icon);
        repostIcon = (ImageView) findViewById(R.id.note_view_repost_icon);
        loadMoreCommentsLayout = (LinearLayout) findViewById(R.id.note_view_load_more_comments);
        likeBlock = (RelativeLayout) findViewById(R.id.note_view_like_block);
        commentBlock = (RelativeLayout) findViewById(R.id.note_view_comment_block);
        repostBlock = (RelativeLayout) findViewById(R.id.note_view_repost_block);
        mainLayout = (RelativeLayout) findViewById(R.id.note_view_main_layout);
        attachBlock = (RelativeLayout) findViewById(R.id.note_view_attachments_icon);
        emojiBlock = (RelativeLayout) findViewById(R.id.note_view_emoji_icon);
        sendBlock = (RelativeLayout) findViewById(R.id.note_view_send_icon);
        sourceInfoBlock = (CardView) findViewById(R.id.note_view_source_info);
        RecyclerView rv = (RecyclerView) findViewById(R.id.note_view_comments);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setFocusable(false);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(mLinearLayoutManager);
        mAdapter = new NoteCommentsRecyclerViewAdapter(getApplicationContext(),
                mNote.getSourceId(),
                addData(0));
        rv.setAdapter(mAdapter);

        loadMoreCommentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        sourceInfoBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        likeBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        commentBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        repostBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        attachBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        emojiBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });
        sendBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });

        sourceNameText.setText(mNote.getSourceName());
        dateText.setText(new PrettyTime(Locale.getDefault()).format(new Date(mNote.getDate() *
                1000)));
        new DownloadImageTask(sourcePhoto).execute(mNote.getSourcePhotoUrl());

        contextText.setVisibility(mNote.getContext().equals("")
                ? View.GONE
                : View.VISIBLE);
        contextText.setText(mNote.getContext());

        likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.context().getResources(),
                mNote.getUserLikes()
                        ? R.drawable.ic_favorite_pressed_24dp
                        : R.drawable.ic_favorite_white_24dp,
                null));

        likesCountText.setText(mNote.getLikesCount() != 0
                ? String.valueOf(mNote.getLikesCount())
                : "");

        commentBlock.setVisibility(mNote.getCanComment()
                ? View.VISIBLE
                : View.GONE);
        commentIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.context().getResources(),
                R.drawable.ic_question_answer_white_24dp,
                null));
        commentsCountText.setText(mNote.getCommentsCount() != 0
                ? String.valueOf(mNote.getCommentsCount())
                : "");

        repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.context().getResources(),
                mNote.getUserReposted()
                        ? R.drawable.ic_reply_pressed_24dp
                        : R.drawable.ic_reply_white_24dp,
                null));
        repostsCountText.setText(mNote.getRepostsCount() != 0
                ? String.valueOf(mNote.getRepostsCount())
                : "");
    }

    private ArrayList<Comment> addData(long offset) {
        ArrayList<Comment> results = new ArrayList<>();
        VKRequest request = new VKRequest("wall.getComments", VKParameters.from(
                "owner_id", String.valueOf(-mNote.getSourceId()),
                "post_id", String.valueOf(mNote.getId()),
                "need_likes", 1,
                "count", 50,
                "extended", 1));
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
            int commentCount = Integer.parseInt(mResponse
                    .get("count")
                    .toString());
            commentsCountText.setText(String.valueOf(commentCount));
            for (int index = 0; index < 50; index++) {
                if (mResponse
                        .getJSONArray("items")
                        .isNull(index)) {
                    break;
                } else {
                    long id = Math.abs(Integer.parseInt(mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .get("id")
                            .toString()));
                    long authorId = Math.abs(Integer.parseInt(mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .get("from_id")
                            .toString()));
                    String authorName = "";
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
                        if (groupId == authorId) {
                            authorName = groupName;
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
                        if (userId == authorId) {
                            authorName = userFirstName + " " + userLastName;
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
                    long date = Integer.parseInt(mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .get("date")
                            .toString());
                    int likes = Integer.parseInt(mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .getJSONObject("likes")
                            .get("count")
                            .toString());
                    boolean userLikes = Integer.parseInt(mResponse
                            .getJSONArray("items")
                            .getJSONObject(index)
                            .getJSONObject("likes")
                            .get("user_likes")
                            .toString()) == 1;

                    Comment comment = new Comment(id,
                            authorId,
                            date,
                            authorName,
                            context,
                            photoUrl,
                            likes,
                            userLikes);
                    results.add(comment);
                }
            }

            //mOffset = results.get(results.size() - 1).getDate();
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return results;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right ,R.anim.slide_out_right);
        finish();
    }
}
