package ru.ifmo.practice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import butterknife.BindView;
import butterknife.ButterKnife;
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
import java.util.concurrent.ExecutionException;

import ru.ifmo.practice.model.Comment;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.attachment.Photo;
import ru.ifmo.practice.adapter.NoteCommentsRecyclerViewAdapter;

public class NoteViewActivity extends AppCompatActivity {
    @BindView(R.id.note_view_source_name)               TextView        sourceNameText;
    @BindView(R.id.note_view_context)                   TextView        contextText;
    @BindView(R.id.note_view_date)                      TextView        dateText;
    @BindView(R.id.note_view_likes_count)               TextView        likesCountText;
    @BindView(R.id.note_view_reposts_count)             TextView        repostsCountText;
    @BindView(R.id.note_view_load_more_comments_text)   TextView        loadMoreCommentsText;
    @BindView(R.id.note_view_attachment_link_title)     TextView        attachedLinkTitleText;
    @BindView(R.id.note_view_attachment_link_caption)   TextView        attachedLinkCaptionText;
    @BindView(R.id.note_view_like_icon)                 ImageView       likeIcon;
    @BindView(R.id.note_view_repost_icon)               ImageView       repostIcon;
    @BindView(R.id.note_view_source_photo)              ImageView       sourcePhoto;
    @BindView(R.id.note_view_attachment_link_photo)     ImageView       attachedLinkPhoto;
    @BindView(R.id.note_view_load_more_comments)        LinearLayout    loadMoreCommentsLayout;
    @BindView(R.id.note_leave_comment_layout)           LinearLayout    noteLeaveCommentLayout;
    @BindView(R.id.note_view_attachments_icon)          RelativeLayout  attachBlock;
    @BindView(R.id.note_view_emoji_icon)                RelativeLayout  emojiBlock;
    @BindView(R.id.note_view_send_icon)                 RelativeLayout  sendBlock;
    @BindView(R.id.note_view_like_block)                RelativeLayout  likeBlock;
    @BindView(R.id.note_view_repost_block)              RelativeLayout  repostBlock;
    @BindView(R.id.note_view_attachment_block)          RelativeLayout  attachmentBlock;
    @BindView(R.id.note_view_source_info)               CardView        sourceInfoBlock;
    @BindView(R.id.note_view_attachment_link)           CardView        attachedLinkBlock;
    @BindView(R.id.note_view_comments)                  RecyclerView    rv;

    public  static      Note                                mNote;
    private             Context                             mContext;
    private             JSONObject                          mResponse;
    private             NoteCommentsRecyclerViewAdapter     mAdapter;
    private             LinearLayoutManager                 mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        ButterKnife.bind(this);

        mContext = this;
        Toolbar tb = (Toolbar) findViewById(R.id.note_view_toolbar);
        setSupportActionBar(tb);
        tb.setNavigationIcon(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.getContext().getResources(),
                R.drawable.ic_keyboard_backspace_white_24dp,
                null));
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setFocusable(false);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(mLinearLayoutManager);
        mAdapter = new NoteCommentsRecyclerViewAdapter(getApplicationContext(),
                mNote.getSourceId(),
                addData());
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
            public void onClick(View v) {
                VKRequest request;
                int likes = mNote.getLikesCount();
                request = new VKRequest("likes." + (mNote.isUserLikes()
                        ? "delete" : "add"), VKParameters.from("type", "post",
                        "owner_id", -mNote.getSourceId(),
                        "item_id", mNote.getId()));
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
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void attemptFailed(VKRequest request,
                                              int attemptNumber,
                                              int totalAttempts) {
                        Toast.makeText(mContext, "Attempt Failed!", Toast.LENGTH_LONG).show();
                    }
                });
                try {
                    likes = Integer.parseInt(mResponse.get("likes").toString());
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
                mNote.setLikesCount(likes);
                likesCountText.setText(mNote.getLikesCount() > 0
                        ? String.valueOf(likes)
                        : "");
                mNote.setUserLikes(!mNote.isUserLikes());
                likeIcon.setImageDrawable(mContext.getDrawable(
                        mNote.isUserLikes()
                                ? R.drawable.ic_favorite_pressed_24dp
                                : R.drawable.ic_favorite_white_24dp));
            }
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
        sourcePhoto.setImageBitmap(mNote.getSourcePhoto().getImageBitmap());
        contextText.setVisibility(mNote.getContext().equals("")
                ? View.GONE
                : View.VISIBLE);
        contextText.setText(mNote.getContext());

        if (mNote.getAttachedLink() != null) {
            attachedLinkBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mNote
                            .getAttachedLink().getUrl()));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browserIntent);
                }
            });
            attachmentBlock.setVisibility(View.VISIBLE);
            attachedLinkTitleText.setText(mNote.getAttachedLink().getTitle());
            attachedLinkCaptionText.setText(mNote.getAttachedLink().getCaption());
            attachedLinkPhoto.setImageBitmap(mNote.getAttachedLink().getPhoto().getImageBitmap());
        }
        else {
            attachmentBlock.setVisibility(View.GONE);
        }

        likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.getContext().getResources(),
                mNote.isUserLikes()
                        ? R.drawable.ic_favorite_pressed_24dp
                        : R.drawable.ic_favorite_white_24dp,
                null));

        likesCountText.setText(mNote.getLikesCount() != 0
                ? String.valueOf(mNote.getLikesCount())
                : "");

        repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.getContext().getResources(),
                mNote.isUserReposted()
                        ? R.drawable.ic_share_pressed_24dp
                        : R.drawable.ic_share_white_24dp,
                null));
        repostsCountText.setText(mNote.getRepostsCount() != 0
                ? String.valueOf(mNote.getRepostsCount())
                : "");

        if (!mNote.isCanComment()) {
            loadMoreCommentsLayout.setVisibility(View.GONE);
            noteLeaveCommentLayout.setVisibility(View.GONE);
        }

        if (mNote.getCommentsCount() <= 10) {
            loadMoreCommentsLayout.setVisibility(View.GONE);
        } else {
            loadMoreCommentsText.setText("Показать ещё " + (mNote.getCommentsCount() - 10) + " " +
                    "комментариев");
        }
    }

    private ArrayList<Comment> addData() {
        ArrayList<Comment> results = new ArrayList<>();
        VKRequest request = new VKRequest("wall.getComments", VKParameters.from(
                "owner_id", String.valueOf(-mNote.getSourceId()),
                "post_id", String.valueOf(mNote.getId()),
                "need_likes", 1,
                "count", 100,
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
            mNote.setCommentsCount(commentCount);
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

                    Photo authorPhoto = new Photo();
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
                            try {
                                authorPhoto.setPhotoUrl(photoUrl);
                            } catch (ExecutionException | InterruptedException pE) {
                                pE.printStackTrace();
                            }
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
                            authorPhoto,
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
}
