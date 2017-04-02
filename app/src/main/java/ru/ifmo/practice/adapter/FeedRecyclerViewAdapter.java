package ru.ifmo.practice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.ifmo.practice.FeedActivity;
import ru.ifmo.practice.GroupFeedViewActivity;
import ru.ifmo.practice.NoteViewActivity;
import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.dialog.RepostNoteDialogFragment;
import ru.ifmo.practice.model.span.EmailSpan;
import ru.ifmo.practice.model.span.GroupHashtagSpan;
import ru.ifmo.practice.model.span.HashtagSpan;
import ru.ifmo.practice.model.span.LinkSpan;
import ru.ifmo.practice.model.span.LinkTouchMovementMethod;
import ru.ifmo.practice.model.span.MyClickableSpan;
import ru.ifmo.practice.model.span.ReferenceSpan;
import ru.ifmo.practice.util.AppConsts;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

public class FeedRecyclerViewAdapter
        extends RecyclerView.Adapter {

    private final int VIEW_NOTE = 1;
    private final int VIEW_PROG_BAR = 0;
    private final int VIEW_CONNECTION_ERROR = -1;

    private static ArrayList<Note> mDataSet;
    private static Activity mActivity;
    private final Context mContext;

    public Context getContext() {
        return mContext;
    }

    public ArrayList<Note> getDataSet() {
        return mDataSet;
    }

    final static class DataObjectHolder
            extends RecyclerView.ViewHolder
            implements TextSwitcher.ViewFactory,
            RepostNoteDialogFragment.RepostNoteDialogListener {
        @BindView(R.id.see_more)                        Button          seeMoreText;
        @BindView(R.id.source_name)                     TextView        sourceNameText;
        @BindView(R.id.note_date)                       TextView        dateText;
        @BindView(R.id.add_note)                        TextView        addNewNoteText;
        @BindView(R.id.likes_count)                     TextView        likesCountText;
        @BindView(R.id.comments_count)                  TextView        commentsCountText;
        @BindView(R.id.reposts_count)                   TextView        repostsCountText;
        @BindView(R.id.attachment_link_big_title)       TextView        attachedLinkBigTitleText;
        @BindView(R.id.attachment_link_big_caption)     TextView        attachedLinkBigCaptionText;
        @BindView(R.id.attachment_link_small_title)     TextView        attachedLinkSmallTitleText;
        @BindView(R.id.attachment_link_small_caption)   TextView        attachedLinkSmallCaptionText;
        @BindView(R.id.attachment_link_preview_title)   TextView        attachedLinkPreviewTitleText;
        @BindView(R.id.attachment_link_preview_caption) TextView        attachedLinkPreviewCaptionText;
        @BindView(R.id.attachment_photo_count)          TextView        attachmentPhotoCountText;
        @BindView(R.id.attachment_video_count)          TextView        attachmentVideoCountText;
        @BindView(R.id.attachment_video_time)           TextView        attachmentVideoTimeText;
        @BindView(R.id.attachment_video_platform)       TextView        attachmentVideoPlatformText;
        @BindView(R.id.attachment_video_title)          TextView        attachmentVideoTitleText;
        @BindView(R.id.attachment_video_views_count)    TextView        attachmentVideoViewsText;
        @BindView(R.id.signer_name)                     TextView        signerNameText;
        @BindView(R.id.context)                         TextView        contextText;
        @BindView(R.id.attachment_page_title)           TextView        attachmentPageTitle;
        @BindView(R.id.attachment_page_caption)         TextView        attachmentPageCaption;
        @BindView(R.id.like_icon)                       ImageView       likeIcon;
        @BindView(R.id.comment_icon)                    ImageView       commentIcon;
        @BindView(R.id.repost_icon)                     ImageView       repostIcon;
        @BindView(R.id.options)                         ImageView       optionsIcon;
        @BindView(R.id.source_photo)                    ImageView       sourcePhoto;
        @BindView(R.id.attachment_photo)                ImageView       attachedPhoto;
        @BindView(R.id.attachment_video)                ImageView       attachedVideo;
        @BindView(R.id.attachment_link_big_photo)       ImageView       attachedLinkBigPhoto;
        @BindView(R.id.attachment_link_small_photo)     ImageView       attachedLinkSmallPhoto;
        @BindView(R.id.attachment_link_preview_photo)   ImageView       attachedLinkPreviewPhoto;
        @BindView(R.id.attachment_photo_count_icon)     ImageView       attachmentPhotoCountIcon;
        @BindView(R.id.attachment_video_count_icon)     ImageView       attachmentVideoCountIcon;
        @BindView(R.id.attachment_page_photo)           ImageView       attachmentPagePhoto;
        @BindView(R.id.attachment_audio_icon)           ImageView       attachmentAudioIcon;
        @BindView(R.id.signer_icon)                     ImageView       signerIcon;
        @BindView(R.id.comment_block)                   RelativeLayout  commentBlock;
        @BindView(R.id.repost_block)                    RelativeLayout  repostBlock;
        @BindView(R.id.note_relative_layout)            RelativeLayout  cardLayout;
        @BindView(R.id.signer_block)                    RelativeLayout  signerBlock;
        @BindView(R.id.attachment_link_group)           RelativeLayout  attachedLinkGroupBlock;
        @BindView(R.id.attachment_link_preview)         RelativeLayout  attachedLinkPreviewBlock;
        @BindView(R.id.attachment_video_block)          RelativeLayout  attachedVideoBlock;
        @BindView(R.id.attachment_page_block)           RelativeLayout  attachedPageBlock;
        @BindView(R.id.attachment_audio_block)          RelativeLayout  attachedAudioBlock;
        @BindView(R.id.source_info)                     CardView        sourceInfoBlock;
        @BindView(R.id.options_block)                   CardView        optionsBlock;
        @BindView(R.id.attachment_link_big)             CardView        attachedLinkBigBlock;
        @BindView(R.id.attachment_link_small)           CardView        attachedLinkSmallBlock;
        @BindView(R.id.attachment_photo_count_block)    CardView        attachmentPhotoCountBlock;
        @BindView(R.id.attachment_video_count_block)    CardView        attachmentVideoCountBlock;
        @BindView(R.id.attachment_video_platform_block) CardView        attachmentVideoPlatformBlock;
        @BindView(R.id.social_actions)                  LinearLayout    socialAcionsLayout;

        private Context    mContext;
        private JSONObject mResponse;

        DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();

            View.OnClickListener openNoteByClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VKSmartFeedApplication.isOnline()) {
                        Intent intent = new Intent(getApplicationContext(), NoteViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(intent);
                        NoteViewActivity.mNote = mDataSet.get(getAdapterPosition());
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                    else {
                        // TODO let user pass to another activity, but show connect error in that
                        // activity
                    }
                }
            };
            final RepostNoteDialogFragment dialog = new RepostNoteDialogFragment();
            dialog.setListener(this);
            repostBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putLong("source_id", -mDataSet.get(getAdapterPosition()).getSourceId());
                    args.putLong("object_id", mDataSet.get(getAdapterPosition()).getId());
                    dialog.setArguments(args);
                    dialog.show(mActivity.getFragmentManager(), "");
                }
            });
            sourceInfoBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GroupFeedViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_right);
                }
            });
            cardLayout.setOnClickListener(openNoteByClick);
            commentBlock.setOnClickListener(openNoteByClick);
            optionsBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });
        }

        @OnClick(R.id.like_block)
        void likeClick() {
            if (VKSmartFeedApplication.isOnline()) {
                Note tmpNote = mDataSet.get(getAdapterPosition());
                int likes = tmpNote.getLikesCount();
                VKRequest request = new VKRequest("likes." + (tmpNote.isUserLikes()
                        ? "delete" : "add"), VKParameters.from("type", "post",
                        "owner_id", -tmpNote.getSourceId(),
                        "item_id", tmpNote.getId()));
                request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        try {
                            mResponse = response.json.getJSONObject("response");
                        } catch (JSONException pE) {
                            pE.printStackTrace();
                        }
                    }
                });
                try {
                    likes = Integer.parseInt(mResponse.get("likes").toString());
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
                tmpNote.setLikesCount(likes);
                likesCountText.setText(tmpNote.getLikesCount() > 0 ? optimizeBigValues(likes) : "");
                tmpNote.setUserLikes(!tmpNote.isUserLikes());
                likeIcon.setImageDrawable(mContext.getDrawable(tmpNote.isUserLikes()
                                ? R.drawable.ic_favorite_pressed_24dp
                                : R.drawable.ic_favorite_white_24dp));
            }
            else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public View makeView() {
            TextView textView = new TextView(mContext);
            textView.setTextSize(14);
            textView.setTextColor(Color.BLACK);
            return textView;
        }

        @Override
        public void onFinishRepostNoteDialog(int resultCode, int repostCount, int likesCount) {
            if (resultCode == 1) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.note_repost_wall_success),
                        Toast.LENGTH_LONG).show();
                repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                        VKSmartFeedApplication.getContext().getResources(),
                        R.drawable.ic_share_pressed_24dp,
                        null));
                repostsCountText.setText(optimizeBigValues(repostCount));
                likesCountText.setText(optimizeBigValues(likesCount));
            }
            else if (resultCode == 2) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final static class ProgressViewHolder
            extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.notes_list_progress_bar);
        }
    }

    final static class ConnectionLostViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.connection_lost_description) TextView    descriptionText;
        @BindView(R.id.connection_lost_button)      Button      refreshButton;
        @BindView(R.id.connection_lost_image)       ImageView   icon;

        ConnectionLostViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public FeedRecyclerViewAdapter(Context context, Activity activity, ArrayList<Note> dataSet) {
        mContext = context;
        mActivity = activity;
        if (dataSet != null) {
            mDataSet = new ArrayList<>(dataSet);
        }
        else {
            mDataSet = new ArrayList<>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_NOTE) {
            View view = LayoutInflater.from(VKSmartFeedApplication.getContext())
                    .inflate(R.layout.activity_feed_note, parent, false);
            viewHolder = new DataObjectHolder(view);
        } else if (viewType == VIEW_PROG_BAR) {
            View view = LayoutInflater.from(VKSmartFeedApplication.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
            viewHolder = new ProgressViewHolder(view);
        } else {
            View view = LayoutInflater.from(VKSmartFeedApplication.getContext())
                    .inflate(R.layout.connection_lost, parent, false);
            viewHolder = new ConnectionLostViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position != getItemCount() - 1
                ? VIEW_NOTE
                : VKSmartFeedApplication.isOnline()
                    ? VIEW_PROG_BAR
                    : VIEW_CONNECTION_ERROR;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataObjectHolder) {
            final Note tmpNote = mDataSet.get(position);
            ((DataObjectHolder) holder).socialAcionsLayout.setVisibility(View.VISIBLE);
            ((DataObjectHolder) holder).sourceNameText.setVisibility(View.VISIBLE);
            ((DataObjectHolder) holder).dateText.setVisibility(View.VISIBLE);
            ((DataObjectHolder) holder).addNewNoteText.setVisibility(View.GONE);

            ((DataObjectHolder) holder).sourceNameText.setText(tmpNote.getSourceName());
            ((DataObjectHolder) holder).dateText.setText(new PrettyTime(Locale.getDefault())
                    .format(new Date(tmpNote.getDate() * 1000)));

            Picasso.with(mContext)
                    .load(tmpNote.getSourcePhoto().getPhotoUrl())
                    .into(((DataObjectHolder) holder).sourcePhoto);
            ((DataObjectHolder) holder).optionsIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    R.drawable.ic_more_vert_gray_24dp,
                    null));

            ((DataObjectHolder) holder).contextText.setVisibility(tmpNote.getContext().equals("")
                    ? View.GONE
                    : View.VISIBLE);
            ((DataObjectHolder) holder).seeMoreText.setVisibility(tmpNote.getContextPreview().equals("")
                ? View.GONE
                : View.VISIBLE);
            ((DataObjectHolder) holder).seeMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DataObjectHolder) holder).contextText.setText(getSpannableString(tmpNote.getContext()));
                    ((DataObjectHolder) holder).seeMoreText.setVisibility(View.GONE);
                }
            });
            ((DataObjectHolder) holder).contextText.setText(((DataObjectHolder) holder).seeMoreText.getVisibility() == View.VISIBLE
                    ? getSpannableString(tmpNote.getContextPreview())
                    : getSpannableString(tmpNote.getContext()));
            ((DataObjectHolder) holder).contextText.setMovementMethod(new LinkTouchMovementMethod());

            if (tmpNote.getAttachmentsVideos().size() > 0) {
                ((DataObjectHolder) holder).attachedVideoBlock.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(tmpNote.getAttachmentsVideos().get(0).getPhoto().getPhotoUrl())
                        .into(((DataObjectHolder) holder).attachedVideo);
                ((DataObjectHolder) holder).attachedVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VKRequest lRequest = new VKRequest("video.get",
                                VKParameters.from(
                                        "videos", tmpNote.getAttachmentsVideos().get(0)
                                                .getOwnerId() + "_" +
                                                tmpNote.getAttachmentsVideos().get(0)
                                                        .getId() + "_" +
                                                tmpNote.getAttachmentsVideos().get(0)
                                                        .getAccessKey()));
                        lRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                try {
                                    final String videoUrl = response.json
                                            .getJSONObject("response")
                                            .getJSONArray("items")
                                            .getJSONObject(0)
                                            .get("player").toString();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(browserIntent);
                                } catch (JSONException pE) {
                                    pE.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VKError error) {
                                Log.e("videoGetRequest", error.toString());
                            }
                        });
                    }
                });

                if (tmpNote.getAttachmentsVideos().get(0).getPlatform().equals("vk")) {
                    ((DataObjectHolder) holder).attachmentVideoPlatformBlock.setVisibility(View.GONE);
                }
                else {
                    ((DataObjectHolder) holder).attachmentVideoPlatformBlock.setVisibility(View.VISIBLE);
                    ((DataObjectHolder) holder).attachmentVideoPlatformText.setText(tmpNote
                            .getAttachmentsVideos().get(0).getPlatform());
                }

                ((DataObjectHolder) holder).attachmentVideoTimeText.setText(convertSecondsToReadableTime(tmpNote
                        .getAttachmentsVideos().get(0).getDuration()));
                ((DataObjectHolder) holder).attachmentVideoTitleText.setText(tmpNote
                        .getAttachmentsVideos().get(0).getTitle());
                ((DataObjectHolder) holder).attachmentVideoViewsText.setText(String.valueOf(tmpNote
                        .getAttachmentsVideos().get(0).getViewsCount()) + " просмотров");
            }
            else {
                ((DataObjectHolder) holder).attachedVideoBlock.setVisibility(View.GONE);
            }

            if (tmpNote.getAttachmentsVideos().size() > 1) {
                ((DataObjectHolder) holder).attachmentVideoCountBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).attachmentVideoCountIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                VKSmartFeedApplication.getContext().getResources(),
                                R.drawable.ic_videocam_blue_24dp,
                                null));
                ((DataObjectHolder) holder).attachmentVideoCountText.setText(String.valueOf(
                        tmpNote.getAttachmentsVideos().size()));
            }
            else {
                ((DataObjectHolder) holder).attachmentVideoCountBlock.setVisibility(View.GONE);
            }

            if (tmpNote.getAttachmentsPhotos().size() > 0) {
                ((DataObjectHolder) holder).attachedPhoto.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(tmpNote.getAttachmentsPhotos().get(0).getPhotoUrl())
                        .into(((DataObjectHolder) holder).attachedPhoto);
            }
            else {
                ((DataObjectHolder) holder).attachedPhoto.setVisibility(View.GONE);
            }
            if (tmpNote.getAttachmentsPhotos().size() > 1) {
                ((DataObjectHolder) holder).attachmentPhotoCountBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).attachmentPhotoCountIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                VKSmartFeedApplication.getContext().getResources(),
                                R.drawable.ic_camera_alt_blue_24dp,
                                null));
                ((DataObjectHolder) holder).attachmentPhotoCountText.setText(String.valueOf(
                        tmpNote.getAttachmentsPhotos().size()));
            }
            else {
                ((DataObjectHolder) holder).attachmentPhotoCountBlock.setVisibility(View.GONE);
            }

            if (tmpNote.getAttachedLink() != null) {
                ((DataObjectHolder) holder).attachedLinkGroupBlock.setVisibility(View.VISIBLE);
                switch (tmpNote.getAttachedLink().getPhotoType()) {
                    case 0:
                        ((DataObjectHolder) holder).attachedLinkBigBlock.setVisibility(View.GONE);
                        ((DataObjectHolder) holder).attachedLinkSmallBlock.setVisibility(View.GONE);
                        ((DataObjectHolder) holder).attachedLinkPreviewBlock.setVisibility(View.VISIBLE);
                        ((DataObjectHolder) holder).attachedLinkPreviewPhoto.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                        mContext.getResources(),
                                        R.drawable.ic_pageview_gray_36dp,
                                        null
                                ));
                        ((DataObjectHolder) holder).attachedLinkPreviewTitleText.setText(tmpNote.getAttachedLink()
                                .getTitle().equals("")
                                    ? mContext.getResources().getString(R.string.link)
                                    : tmpNote.getAttachedLink().getTitle());
                        ((DataObjectHolder) holder).attachedLinkPreviewCaptionText.setText(tmpNote.getAttachedLink()
                                .getCaption());
                        ((DataObjectHolder) holder).attachedLinkPreviewBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmpNote
                                        .getAttachedLink().getUrl()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(browserIntent);
                            }
                        });
                        break;
                    case 1:
                        ((DataObjectHolder) holder).attachedLinkPreviewBlock.setVisibility(View.GONE);
                        ((DataObjectHolder) holder).attachedLinkBigBlock.setVisibility(View.GONE);
                        ((DataObjectHolder) holder).attachedLinkSmallBlock.setVisibility(View.VISIBLE);
                        Picasso.with(mContext)
                                .load(tmpNote.getAttachedLink().getPhoto().getPhotoUrl())
                                .into(((DataObjectHolder) holder).attachedLinkSmallPhoto);
                        ((DataObjectHolder) holder).attachedLinkSmallTitleText.setText(tmpNote.getAttachedLink()
                                .getTitle());
                        ((DataObjectHolder) holder).attachedLinkSmallCaptionText.setText(tmpNote.getAttachedLink()
                                .getCaption());
                        ((DataObjectHolder) holder).attachedLinkSmallBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmpNote
                                        .getAttachedLink().getUrl()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(browserIntent);
                            }
                        });
                        break;
                    case 2:
                        ((DataObjectHolder) holder).attachedLinkPreviewBlock.setVisibility(View.GONE);
                        ((DataObjectHolder) holder).attachedLinkSmallBlock.setVisibility(View.GONE);
                        ((DataObjectHolder) holder).attachedLinkBigBlock.setVisibility(View.VISIBLE);
                        Picasso.with(mContext)
                                .load(tmpNote.getAttachedLink().getPhoto().getPhotoUrl())
                                .into(((DataObjectHolder) holder).attachedLinkBigPhoto);
                        ((DataObjectHolder) holder).attachedLinkBigTitleText.setText(tmpNote.getAttachedLink().getTitle());
                        ((DataObjectHolder) holder).attachedLinkBigCaptionText.setText(tmpNote.getAttachedLink().getCaption());
                        ((DataObjectHolder) holder).attachedLinkBigBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmpNote
                                        .getAttachedLink().getUrl()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(browserIntent);
                            }
                        });
                        break;
                }
            }
            else {
                ((DataObjectHolder) holder).attachedLinkGroupBlock.setVisibility(View.GONE);
            }

            if (tmpNote.getAttachedPage() != null) {
                ((DataObjectHolder) holder).attachedPageBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).attachmentPageTitle.setText(tmpNote.getAttachedPage()
                        .getTitle());
                ((DataObjectHolder) holder).attachmentPageCaption.setText(mContext
                        .getResources().getString(R.string.wiki_page));
                ((DataObjectHolder) holder).attachmentPagePhoto.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                mContext.getResources(),
                                R.drawable.ic_pageview_gray_36dp,
                                null
                        ));
                ((DataObjectHolder) holder).attachedPageBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmpNote
                                .getAttachedPage().getViewUrl()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(browserIntent);
                    }
                });
            }
            else {
                ((DataObjectHolder) holder).attachedPageBlock.setVisibility(View.GONE);
            }

            if (tmpNote.isAudioAttached()) {
                ((DataObjectHolder) holder).attachedAudioBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).attachmentAudioIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                mContext.getResources(),
                                R.drawable.ic_music_note_blue_36dp,
                                null
                        ));
            }
            else {
                ((DataObjectHolder) holder).attachedAudioBlock.setVisibility(View.GONE);
            }

            if (tmpNote.getSigner() != null) {
                ((DataObjectHolder) holder).signerBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).signerIcon.setImageDrawable(ResourcesCompat.getDrawable(
                        VKSmartFeedApplication.getContext().getResources(),
                        R.drawable.ic_account_circle_blue_24dp,
                        null));
                ((DataObjectHolder) holder).signerNameText.setText(tmpNote.getSigner().getFirstName() + " " +
                        tmpNote.getSigner().getLastName());
                ((DataObjectHolder) holder).signerBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO
                    }
                });
            }
            else {
                ((DataObjectHolder) holder).signerBlock.setVisibility(View.GONE);
            }

            ((DataObjectHolder) holder).likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    tmpNote.isUserLikes()
                            ? R.drawable.ic_favorite_pressed_24dp
                            : R.drawable.ic_favorite_white_24dp,
                    null));

            ((DataObjectHolder) holder).likesCountText.setText(tmpNote.getLikesCount() != 0
                    ? optimizeBigValues(tmpNote.getLikesCount())
                    : "");

            ((DataObjectHolder) holder).commentBlock.setVisibility(tmpNote.isCanComment()
                    ? View.VISIBLE
                    : View.GONE);
            ((DataObjectHolder) holder).commentIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    R.drawable.ic_question_answer_white_24dp,
                    null));
            ((DataObjectHolder) holder).commentsCountText.setText(tmpNote.getCommentsCount() != 0
                    ? optimizeBigValues(tmpNote.getCommentsCount())
                    : "");

            ((DataObjectHolder) holder).repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    tmpNote.isUserReposted()
                            ? R.drawable.ic_share_pressed_24dp
                            : R.drawable.ic_share_white_24dp,
                    null));
            ((DataObjectHolder) holder).repostsCountText.setText(tmpNote.getRepostsCount() != 0
                    ? optimizeBigValues(tmpNote.getRepostsCount())
                    : "");
        }
        else if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
        else {
            ((ConnectionLostViewHolder) holder).descriptionText.setText(mContext.getResources()
                    .getString(R.string.connection_lost_description));
            ((ConnectionLostViewHolder) holder).refreshButton.setText(mContext.getResources()
                    .getString(R.string.refresh_page));
            ((ConnectionLostViewHolder) holder).icon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    R.drawable.ic_warning_blue_36dp,
                    null));
            ((ConnectionLostViewHolder) holder).refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VKSmartFeedApplication.isOnline()) {
                        ((FeedActivity) mActivity).refreshFeed();
                        if (!((FeedActivity) mActivity).isDataRelevant()) {
                            ((FeedActivity) mActivity).getSnackbarRefresh().dismiss();
                        }
                    }
                }
            });
        }
    }

    private String convertSecondsToReadableTime(int secs) {
        String hours = secs / 3600 > 0
                ? String.format(Locale.getDefault(), "%d:", secs / 3600)
                : "";
        String minutes = (secs % 3600) / 60 > 0
                ? String.format(Locale.getDefault(), "%d:", (secs % 3600) / 60)
                : "0:";
        String seconds = String.format(Locale.getDefault(), "%02d", secs % 60);
        if ((hours + minutes + seconds).equals("0:00"))
            return "Неизвестно";
        else
            return hours + minutes + seconds;
    }

    private static String optimizeBigValues(double value) {
        if (value > 1000) {
            return String.format(Locale.getDefault(), "%.1fK", value / 1000);
        }
        else return String.format(Locale.getDefault(), "%.0f", value);
    }

    public void clear() {
        mDataSet.clear();
    }

    public void addAll(ArrayList<Note> list) {
        for (int i = 0; i < list.size(); i++) {
            mDataSet.add(list.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private SpannableStringBuilder getSpannableString(String inputString) {
        SpannableStringBuilder sb = new SpannableStringBuilder();

        Pattern referencePattern = Pattern.compile(AppConsts.REGEX_REFERENCE_PATTERN);
        Pattern hashtagPattern = Pattern.compile(AppConsts.REGEX_SEPARATE_HASHTAG_PATTERN);
        Pattern groupPattern = Pattern.compile(AppConsts.REGEX_GROUP_HASHTAG_PATTERN);
        Pattern linkPattern = Pattern.compile(AppConsts.REGEX_WEB_LINK_PATTERN);
        Pattern emailPattern = Pattern.compile(AppConsts.REGEX_EMAIL_PATTERN);

        Matcher referenceMatcher = referencePattern.matcher(inputString);
        Matcher hashtagMatcher = hashtagPattern.matcher(inputString);
        Matcher groupMatcher = groupPattern.matcher(inputString);
        Matcher linkMatcher = linkPattern.matcher(inputString);
        Matcher emailMatcher = emailPattern.matcher(inputString);

        ArrayList<MyClickableSpan> spans = new ArrayList<>();

        while (referenceMatcher.find()) {
            spans.add(new ReferenceSpan(referenceMatcher.start(),
                    referenceMatcher.end(),
                    referenceMatcher.group(),
                    mContext));
        }
        while (groupMatcher.find()) {
            spans.add(new GroupHashtagSpan(groupMatcher.start(),
                    groupMatcher.end(),
                    groupMatcher.group(),
                    mContext));
        }
        while (linkMatcher.find()) {
            spans.add(new LinkSpan(linkMatcher.start(),
                    linkMatcher.end(),
                    linkMatcher.group(),
                    mContext));
        }
        while (emailMatcher.find()) {
            spans.add(new EmailSpan(emailMatcher.start(),
                    emailMatcher.end(),
                    emailMatcher.group(),
                    mContext));
        }
        // TODO make separate check for unnecessary (included in other) spans and delete them
        // from entire List
        while (hashtagMatcher.find()) {
            boolean canAdd = true;
            for (int i = 0; i < spans.size(); i++) {
                if (spans.get(i) instanceof GroupHashtagSpan) {
                    if (hashtagMatcher.start() == spans.get(i).getStartPosition()) {
                        canAdd = false;
                        break;
                    }
                }
                else if (spans.get(i) instanceof LinkSpan) {
                    if (hashtagMatcher.start() >= spans.get(i).getStartPosition() &&
                            hashtagMatcher.end() <= spans.get(i).getEndPosition()) {
                        canAdd = false;
                        break;
                    }
                }
            }
            if(canAdd) {
                spans.add(new HashtagSpan(hashtagMatcher.start(),
                        hashtagMatcher.end(),
                        hashtagMatcher.group(),
                        mContext));
            }
        }

        if (spans.size() > 0) {
            Collections.sort(spans, MyClickableSpan.MyClickableSpanComparator);
            int lastSpanEndPosition = 0;
            for (MyClickableSpan span : spans) {
                String beforeSpan = inputString.substring(lastSpanEndPosition,
                        span.getStartPosition());
                sb.append(beforeSpan);
                span.setStartPosition(sb.length());
                if (span instanceof LinkSpan) {
                    if (((LinkSpan) span).getShortString().length() > 0) {
                        span.setVisibleEndPosition(sb.length() +
                                ((LinkSpan) span).getShortString().length());
                        sb.append(((LinkSpan) span).getShortString());
                    }
                    else {
                        span.setVisibleEndPosition(sb.length() +
                                span.getString().length());
                        sb.append(span.getString());
                    }
                }
                else if (span instanceof ReferenceSpan) {
                    span.setVisibleEndPosition(sb.length() + ((ReferenceSpan) span).getName().length());
                    sb.append(((ReferenceSpan) span).getName());
                }
                else {
                    span.setVisibleEndPosition(sb.length() + span.getString().length());
                    sb.append(span.getString());
                }
                sb.setSpan(span,
                        span.getStartPosition(),
                        span.getVisibleEndPosition(),
                        Spanned.SPAN_INTERMEDIATE);
                lastSpanEndPosition = span.getEndPosition();
            }
            if (lastSpanEndPosition < inputString.length() - 1) {
                sb.append(inputString.substring(lastSpanEndPosition, inputString.length()));
            }
        } else {
            sb.append(inputString);
        }
        return sb;
    }
}