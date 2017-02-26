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
import java.util.regex.Pattern;

import ru.ifmo.practice.GroupFeedViewActivity;
import ru.ifmo.practice.NoteViewActivity;
import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.dialog.RepostNoteDialogFragment;
import ru.ifmo.practice.model.span.PatternEditableBuilder;

import static com.vk.sdk.VKUIHelper.getApplicationContext;
import static ru.ifmo.practice.R.id.context;

public class FeedRecyclerViewAdapter
        extends RecyclerView.Adapter {

    private final int VIEW_NOTE = 1;
    private final int VIEW_PROG_BAR = 0;

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
        private ImageView       signerIcon;
        private RelativeLayout  likeBlock;
        private RelativeLayout  commentBlock;
        private RelativeLayout  repostBlock;
        private RelativeLayout  cardLayout;
        private RelativeLayout  signerBlock;
        private RelativeLayout  attachedLinkGroupBlock;
        private RelativeLayout  attachedLinkPreviewBlock;
        private RelativeLayout  attachedVideoBlock;
        private CardView        sourceInfoBlock;
        private CardView        optionsBlock;
        private CardView        attachedLinkBigBlock;
        private CardView        attachedLinkSmallBlock;
        private CardView        attachmentPhotoCountBlock;
        private CardView        attachmentVideoCountBlock;
        private CardView        attachmentVideoPlatformBlock;
        private LinearLayout    socialAcionsLayout;
        private LinearLayout    attachmentAudioBlock;

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
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);
            cardLayout = (RelativeLayout) itemView.findViewById(R.id.note_relative_layout);
            signerBlock = (RelativeLayout) itemView.findViewById(R.id.signer_block);
            attachedLinkGroupBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_link_group);
            attachedVideoBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_video_block);
            sourceInfoBlock = (CardView) itemView.findViewById(R.id.source_info);
            optionsBlock = (CardView) itemView.findViewById(R.id.options_block);
            attachedLinkBigBlock = (CardView) itemView.findViewById(R.id.attachment_link_big);
            attachedLinkSmallBlock = (CardView) itemView.findViewById(R.id.attachment_link_small);
            attachedLinkPreviewBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_link_preview);
            attachmentVideoCountBlock = (CardView) itemView.findViewById(R.id.attachment_video_count_block);
            attachmentVideoPlatformBlock = (CardView) itemView.findViewById(R.id.attachment_video_platform_block);
            attachmentPhotoCountBlock = (CardView) itemView.findViewById(R.id.attachment_photo_count_block);
            socialAcionsLayout = (LinearLayout) itemView.findViewById(R.id.social_actions);
            attachmentAudioBlock = (LinearLayout) itemView.findViewById(R.id.attachment_audio_block);

            new PatternEditableBuilder().
                    addPattern(Pattern.compile("\\#(\\w+)"), Color.CYAN,
                            new PatternEditableBuilder.SpannableClickedListener() {
                                @Override
                                public void onSpanClicked(String text) {
                                    Toast.makeText(mActivity, "Clicked hashtag: " + text,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).into(contextText);

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
                    Note tmpNote = mDataSet.get(getAdapterPosition());
                    VKRequest request;
                    int likes = tmpNote.getLikesCount();
                    request = new VKRequest("likes." + (tmpNote.getUserLikes()
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
                    tmpNote.setLikesCount(likes);
                    likesCountText.setText(tmpNote.getLikesCount() > 0
                            ? String.valueOf(likes)
                            : "");
                    tmpNote.setUserLikes(
                            !tmpNote.getUserLikes());
                    likeIcon.setImageDrawable(mContext.getDrawable(
                            tmpNote.getUserLikes()
                                    ? R.drawable.ic_favorite_pressed_24dp
                                    : R.drawable.ic_favorite_white_24dp));
                }
            });
            View.OnClickListener openNoteByClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NoteViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    NoteViewActivity.mNote = mDataSet.get(getAdapterPosition());
                    mActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_right);
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
                    dialog.show(mActivity.getFragmentManager(), "test");
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
                    // ...
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
                Toast.makeText(mContext, "Note successfully reposted to your wall!", Toast
                        .LENGTH_LONG).show();
                repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                        VKSmartFeedApplication.context().getResources(),
                        R.drawable.ic_share_pressed_24dp,
                        null));
                repostsCountText.setText(String.valueOf(repostCount));
                likesCountText.setText(String.valueOf(likesCount));
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
            View view = LayoutInflater.from(VKSmartFeedApplication.context())
                    .inflate(R.layout.activity_main_card_note_content, parent, false);

            viewHolder = new DataObjectHolder(view);
        } else {
            View view = LayoutInflater.from(VKSmartFeedApplication.context())
                    .inflate(R.layout.activity_main_card_progress_bar_content, parent, false);

            viewHolder = new ProgressViewHolder (view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) != null ? VIEW_NOTE : VIEW_PROG_BAR;
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
            //((DataObjectHolder) holder).sourcePhoto.setImageBitmap(tmpNote.getSourcePhoto()
            //        .getImageBitmap());

            ((DataObjectHolder) holder).optionsIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
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
                //((DataObjectHolder) holder).attachedVideo.setImageBitmap();
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
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                System.out.println("error suka");
                            }

                            @Override
                            public void onError(VKError error) {
                                System.out.println(error.toString());
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
                                VKSmartFeedApplication.context().getResources(),
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
                //((DataObjectHolder) holder).attachedPhoto.setImageBitmap(tmpNote
                //       .getAttachmentsPhotos().get(0).getImageBitmap());
            }
            else {
                ((DataObjectHolder) holder).attachedPhoto.setVisibility(View.GONE);
            }
            if (tmpNote.getAttachmentsPhotos().size() > 1) {
                ((DataObjectHolder) holder).attachmentPhotoCountBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).attachmentPhotoCountIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                VKSmartFeedApplication.context().getResources(),
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
                                .getTitle().equals("") ? "Ссылка" : tmpNote.getAttachedLink().getTitle());
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
                        //((DataObjectHolder) holder).attachedLinkSmallPhoto.setImageBitmap(tmpNote
                        //    .getAttachedLink().getPhoto().getImageBitmap());
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
                        //((DataObjectHolder) holder).attachedLinkBigPhoto.setImageBitmap(tmpNote
                        //    .getAttachedLink().getPhoto().getImageBitmap());
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

            if (tmpNote.getSigner() != null) {
                ((DataObjectHolder) holder).signerBlock.setVisibility(View.VISIBLE);
                ((DataObjectHolder) holder).signerIcon.setImageDrawable(ResourcesCompat.getDrawable(
                        VKSmartFeedApplication.context().getResources(),
                        R.drawable.ic_account_circle_blue_24dp,
                        null));
                ((DataObjectHolder) holder).signerNameText.setText(tmpNote.getSigner().getFirstName() + " " +
                        tmpNote.getSigner().getLastName());
                ((DataObjectHolder) holder).signerBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ...
                    }
                });
            }
            else {
                ((DataObjectHolder) holder).signerBlock.setVisibility(View.GONE);
            }

            ((DataObjectHolder) holder).likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    tmpNote.getUserLikes()
                            ? R.drawable.ic_favorite_pressed_24dp
                            : R.drawable.ic_favorite_white_24dp,
                    null));

            ((DataObjectHolder) holder).likesCountText.setText(tmpNote.getLikesCount() != 0
                    ? String.valueOf(tmpNote.getLikesCount())
                    : "");

            ((DataObjectHolder) holder).commentBlock.setVisibility(tmpNote.getCanComment()
                    ? View.VISIBLE
                    : View.GONE);
            ((DataObjectHolder) holder).commentIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_question_answer_white_24dp,
                    null));
            ((DataObjectHolder) holder).commentsCountText.setText(tmpNote.getCommentsCount() != 0
                    ? String.valueOf(tmpNote.getCommentsCount())
                    : "");

            ((DataObjectHolder) holder).repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    tmpNote.getUserReposted()
                            ? R.drawable.ic_share_pressed_24dp
                            : R.drawable.ic_share_white_24dp,
                    null));
            ((DataObjectHolder) holder).repostsCountText.setText(tmpNote.getRepostsCount() != 0
                    ? String.valueOf(tmpNote.getRepostsCount())
                    : "");
        }
        else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    private String convertSecondsToReadableTime(int secs) {
        String hours = secs / 3600 > 0
                ? String.format(Locale.getDefault(), "%02d:", secs / 3600)
                : "";
        String minutes = (secs % 3600) / 60 > 0
                ? String.format(Locale.getDefault(), "%02d:", (secs % 3600) / 60)
                : "00:";
        String seconds = String.format(Locale.getDefault(), "%02d", secs % 60);

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