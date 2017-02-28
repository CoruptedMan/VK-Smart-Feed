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

import com.squareup.picasso.Picasso;
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

import ru.ifmo.practice.FeedActivity;
import ru.ifmo.practice.GroupFeedViewActivity;
import ru.ifmo.practice.NoteViewActivity;
import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.dialog.RepostNoteDialogFragment;

import static com.vk.sdk.VKUIHelper.getApplicationContext;
import static ru.ifmo.practice.R.id.context;

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

    private final static class DataObjectHolder
            extends RecyclerView.ViewHolder
            implements TextSwitcher.ViewFactory,
            RepostNoteDialogFragment.RepostNoteDialogListener {
        private Context         mContext;
        private JSONObject      mResponse;
        private Button          seeMoreText;
        private TextView        sourceNameText;
        private TextView        dateText;
        private TextView        addNewNoteText;
        private TextView        likesCountText;
        private TextView        commentsCountText;
        private TextView        repostsCountText;
        private TextView        attachedLinkBigTitleText;
        private TextView        attachedLinkBigCaptionText;
        private TextView        attachedLinkSmallTitleText;
        private TextView        attachedLinkSmallCaptionText;
        private TextView        attachedLinkPreviewTitleText;
        private TextView        attachedLinkPreviewCaptionText;
        private TextView        attachmentPhotoCountText;
        private TextView        attachmentVideoCountText;
        private TextView        attachmentVideoTimeText;
        private TextView        attachmentVideoPlatformText;
        private TextView        attachmentVideoTitleText;
        private TextView        attachmentVideoViewsText;
        private TextView        signerNameText;
        private TextView        contextText;
        private TextView        attachmentPageTitle;
        private TextView        attachmentPageCaption;
        private ImageView       likeIcon;
        private ImageView       commentIcon;
        private ImageView       repostIcon;
        private ImageView       optionsIcon;
        private ImageView       sourcePhoto;
        private ImageView       attachedPhoto;
        private ImageView       attachedVideo;
        private ImageView       attachedLinkBigPhoto;
        private ImageView       attachedLinkSmallPhoto;
        private ImageView       attachedLinkPreviewPhoto;
        private ImageView       attachmentPhotoCountIcon;
        private ImageView       attachmentVideoCountIcon;
        private ImageView       attachmentPagePhoto;
        private ImageView       signerIcon;
        private ImageView       attachmentAudioIcon;
        private RelativeLayout  likeBlock;
        private RelativeLayout  commentBlock;
        private RelativeLayout  repostBlock;
        private RelativeLayout  cardLayout;
        private RelativeLayout  signerBlock;
        private RelativeLayout  attachedLinkGroupBlock;
        private RelativeLayout  attachedLinkPreviewBlock;
        private RelativeLayout  attachedVideoBlock;
        private RelativeLayout  attachedPageBlock;
        private RelativeLayout  attachedAudioBlock;
        private CardView        sourceInfoBlock;
        private CardView        optionsBlock;
        private CardView        attachedLinkBigBlock;
        private CardView        attachedLinkSmallBlock;
        private CardView        attachmentPhotoCountBlock;
        private CardView        attachmentVideoCountBlock;
        private CardView        attachmentVideoPlatformBlock;
        private LinearLayout    socialAcionsLayout;

        DataObjectHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            seeMoreText = (Button) itemView.findViewById(R.id.see_more);
            sourceNameText = (TextView) itemView.findViewById(R.id.source_name);
            dateText = (TextView) itemView.findViewById(R.id.note_date);
            addNewNoteText = (TextView) itemView.findViewById(R.id.add_note);
            contextText = (TextView) itemView.findViewById(context);
            likesCountText = (TextView) itemView.findViewById(R.id.likes_count);
            commentsCountText = (TextView) itemView.findViewById(R.id.comments_count);
            repostsCountText = (TextView) itemView.findViewById(R.id.reposts_count);
            attachedLinkBigTitleText = (TextView) itemView.findViewById(R.id.attachment_link_big_title);
            attachedLinkBigCaptionText = (TextView) itemView.findViewById(R.id.attachment_link_big_caption);
            attachedLinkSmallTitleText = (TextView) itemView.findViewById(R.id.attachment_link_small_title);
            attachedLinkSmallCaptionText = (TextView) itemView.findViewById(R.id.attachment_link_small_caption);
            attachedLinkPreviewTitleText = (TextView) itemView.findViewById(R.id.attachment_link_preview_title);
            attachedLinkPreviewCaptionText = (TextView) itemView.findViewById(R.id.attachment_link_preview_caption);
            attachmentPhotoCountText = (TextView) itemView.findViewById(R.id.attachment_photo_count);
            attachmentVideoCountText = (TextView) itemView.findViewById(R.id.attachment_video_count);
            attachmentVideoTimeText = (TextView) itemView.findViewById(R.id.attachment_video_time);
            attachmentVideoPlatformText = (TextView) itemView.findViewById(R.id.attachment_video_platform);
            attachmentVideoTitleText = (TextView) itemView.findViewById(R.id.attachment_video_title);
            attachmentVideoViewsText = (TextView) itemView.findViewById(R.id.attachment_video_views_count);
            signerNameText = (TextView) itemView.findViewById(R.id.signer_name);
            attachmentPageTitle = (TextView) itemView.findViewById(R.id.attachment_page_title);
            attachmentPageCaption = (TextView) itemView.findViewById(R.id.attachment_page_caption);
            sourcePhoto = (ImageView) itemView.findViewById(R.id.source_photo);
            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            commentIcon = (ImageView) itemView.findViewById(R.id.comment_icon);
            repostIcon = (ImageView) itemView.findViewById(R.id.repost_icon);
            optionsIcon = (ImageView) itemView.findViewById(R.id.options);
            attachedPhoto = (ImageView) itemView.findViewById(R.id.attachment_photo);
            attachedVideo = (ImageView) itemView.findViewById(R.id.attachment_video);
            attachedLinkBigPhoto = (ImageView) itemView.findViewById(R.id.attachment_link_big_photo);
            attachedLinkSmallPhoto = (ImageView) itemView.findViewById(R.id.attachment_link_small_photo);
            attachedLinkPreviewPhoto = (ImageView) itemView.findViewById(R.id.attachment_link_preview_photo);
            attachmentPhotoCountIcon = (ImageView) itemView.findViewById(R.id.attachment_photo_count_icon);
            attachmentVideoCountIcon = (ImageView) itemView.findViewById(R.id.attachment_video_count_icon);
            signerIcon = (ImageView) itemView.findViewById(R.id.signer_icon);
            attachmentPagePhoto = (ImageView) itemView.findViewById(R.id.attachment_page_photo);
            attachmentAudioIcon = (ImageView) itemView.findViewById(R.id.attachment_audio_icon);
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);
            cardLayout = (RelativeLayout) itemView.findViewById(R.id.note_relative_layout);
            signerBlock = (RelativeLayout) itemView.findViewById(R.id.signer_block);
            attachedLinkGroupBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_link_group);
            attachedVideoBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_video_block);
            attachedPageBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_page_block);
            attachedAudioBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_audio_block);
            sourceInfoBlock = (CardView) itemView.findViewById(R.id.source_info);
            optionsBlock = (CardView) itemView.findViewById(R.id.options_block);
            attachedLinkBigBlock = (CardView) itemView.findViewById(R.id.attachment_link_big);
            attachedLinkSmallBlock = (CardView) itemView.findViewById(R.id.attachment_link_small);
            attachedLinkPreviewBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_link_preview);
            attachmentVideoCountBlock = (CardView) itemView.findViewById(R.id.attachment_video_count_block);
            attachmentVideoPlatformBlock = (CardView) itemView.findViewById(R.id.attachment_video_platform_block);
            attachmentPhotoCountBlock = (CardView) itemView.findViewById(R.id.attachment_photo_count_block);
            socialAcionsLayout = (LinearLayout) itemView.findViewById(R.id.social_actions);

            /*contextText.setFactory(this);
            Animation inAnimation = AnimationUtils.loadAnimation(mContext,
                    android.R.anim.fade_in);
            Animation outAnimation = AnimationUtils.loadAnimation(mContext,
                    android.R.anim.fade_out);
            contextText.setInAnimation(inAnimation);
            contextText.setOutAnimation(outAnimation);*/

            likeBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VKSmartFeedApplication.isOnline()) {
                        Note tmpNote = mDataSet.get(getAdapterPosition());
                        VKRequest request;
                        int likes = tmpNote.getLikesCount();
                        request = new VKRequest("likes." + (tmpNote.isUserLikes()
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

                            @Override
                            public void onError(VKError error) {
                                Log.e("likesRequest", error.toString());
                            }
                        });
                        try {
                            likes = Integer.parseInt(mResponse.get("likes").toString());
                        } catch (JSONException pE) {
                            pE.printStackTrace();
                        }
                        tmpNote.setLikesCount(likes);
                        likesCountText.setText(tmpNote.getLikesCount() > 0
                                ? String.valueOf(likes)
                                : "");
                        tmpNote.setUserLikes(
                                !tmpNote.isUserLikes());
                        likeIcon.setImageDrawable(mContext.getDrawable(
                                tmpNote.isUserLikes()
                                        ? R.drawable.ic_favorite_pressed_24dp
                                        : R.drawable.ic_favorite_white_24dp));
                    }
                    else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            View.OnClickListener openNoteByClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VKSmartFeedApplication.isOnline()) {
                        Intent intent = new Intent(getApplicationContext(), NoteViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(intent);
                        NoteViewActivity.mNote = mDataSet.get(getAdapterPosition());
                        mActivity.overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_right);
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
                repostsCountText.setText(String.valueOf(repostCount));
                likesCountText.setText(String.valueOf(likesCount));
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

    private final static class ConnectionLostViewHolder
            extends RecyclerView.ViewHolder {
        TextView descriptionText;
        Button refreshButton;
        ImageView icon;

        ConnectionLostViewHolder(View v) {
            super(v);
            descriptionText = (TextView) v.findViewById(R.id.connection_lost_description);
            refreshButton = (Button) v.findViewById(R.id.connection_lost_button);
            icon = (ImageView) v.findViewById(R.id.connection_lost_image);
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
        return position != getItemCount() - 1 ? VIEW_NOTE :
                VKSmartFeedApplication.isOnline() ? VIEW_PROG_BAR : VIEW_CONNECTION_ERROR;
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
            /*((DataObjectHolder) holder).seeMoreText.setVisibility(tmpNote.getContextPreview().equals("")
                ? View.GONE
                : View.VISIBLE);*/
            ((DataObjectHolder) holder).seeMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DataObjectHolder) holder).contextText.setText(tmpNote.getContext());
                    ((DataObjectHolder) holder).seeMoreText.setVisibility(View.GONE);
                }
            });
            ((DataObjectHolder) holder).contextText.setText(((DataObjectHolder) holder).seeMoreText.getVisibility() == View.VISIBLE
                    ? tmpNote.getContextPreview()
                    : tmpNote.getContext());

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
                    ? String.valueOf(tmpNote.getLikesCount())
                    : "");

            ((DataObjectHolder) holder).commentBlock.setVisibility(tmpNote.isCanComment()
                    ? View.VISIBLE
                    : View.GONE);
            ((DataObjectHolder) holder).commentIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    R.drawable.ic_question_answer_white_24dp,
                    null));
            ((DataObjectHolder) holder).commentsCountText.setText(tmpNote.getCommentsCount() != 0
                    ? String.valueOf(tmpNote.getCommentsCount())
                    : "");

            ((DataObjectHolder) holder).repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.getContext().getResources(),
                    tmpNote.isUserReposted()
                            ? R.drawable.ic_share_pressed_24dp
                            : R.drawable.ic_share_white_24dp,
                    null));
            ((DataObjectHolder) holder).repostsCountText.setText(tmpNote.getRepostsCount() != 0
                    ? String.valueOf(tmpNote.getRepostsCount())
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
}