package ru.ifmo.practice;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.util.DownloadImageTask;
import ru.ifmo.practice.util.FeedRecyclerViewAdapter;

public class NoteViewActivity extends AppCompatActivity {

    private static Note mNote;
    private LinearLayoutManager mLinearLayoutManager;
    private FeedRecyclerViewAdapter mAdapter;
    private JSONObject mResponse;
    private TextView authorNameText;
    private TextView contextText;
    private TextView dateText;
    private TextView likesCountText;
    private TextView commentsCountText;
    private TextView repostsCountText;
    private ImageView likeIcon;
    private ImageView commentIcon;
    private ImageView repostIcon;
    private ImageView sourcePhoto;
    private RelativeLayout likeBlock;
    private RelativeLayout commentBlock;
    private RelativeLayout repostBlock;
    private CardView sourceInfoBlock;
    private CardView noteLayout;
    private RelativeLayout mainLayout;
    private LinearLayout socialAcionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view_layout);

        authorNameText = (TextView) findViewById(R.id.note_view_source_name);
        contextText = (TextView) findViewById(R.id.note_view_context);
        dateText = (TextView) findViewById(R.id.note_view_date);
        likesCountText = (TextView) findViewById(R.id.note_view_likes_count);
        commentsCountText = (TextView) findViewById(R.id.note_view_comments_count);
        repostsCountText = (TextView) findViewById(R.id.note_view_reposts_count);
        sourcePhoto = (ImageView) findViewById(R.id.note_view_source_photo);
        likeIcon = (ImageView) findViewById(R.id.note_view_like_icon);
        commentIcon = (ImageView) findViewById(R.id.note_view_comment_icon);
        repostIcon = (ImageView) findViewById(R.id.note_view_repost_icon);
        likeBlock = (RelativeLayout) findViewById(R.id.note_view_like_block);
        commentBlock = (RelativeLayout) findViewById(R.id.note_view_comment_block);
        repostBlock = (RelativeLayout) findViewById(R.id.note_view_repost_block);
        sourceInfoBlock = (CardView) findViewById(R.id.note_view_source_info);
        noteLayout = (CardView) findViewById(R.id.note_view);
        mainLayout = (RelativeLayout) findViewById(R.id.note_view_main_layout);
        socialAcionsLayout = (LinearLayout) findViewById(R.id.note_view_social_actions);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.feed_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);
        mAdapter = new NoteCommentsRecyclerViewAdapter(getApplicationContext());
        rv.setAdapter(mAdapter);

        authorNameText.setText(mNote.getSourceName());
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
}
