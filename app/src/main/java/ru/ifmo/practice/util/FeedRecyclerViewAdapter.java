package ru.ifmo.practice.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.concurrent.ExecutionException;

import ru.ifmo.practice.GroupFeedViewActivity;
import ru.ifmo.practice.NoteViewActivity;
import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Note;
import ru.ifmo.practice.model.dialog.RepostNoteDialogFragment;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

public class FeedRecyclerViewAdapter
        extends RecyclerView.Adapter<FeedRecyclerViewAdapter.DataObjectHolder> {

    private static ArrayList<Note> mDataSet;
    private static Activity mActivity;
    private final Context mContext;

    public ArrayList<Note> getDataSet() {
        return mDataSet;
    }

    final static class DataObjectHolder extends RecyclerView.ViewHolder {
        private JSONObject mResponse;
        private TextView sourceNameText;
        private TextView dateText;
        private TextView addNewNoteText;
        private TextView contextText;
        private TextView seeMoreText;
        private TextView likesCountText;
        private TextView commentsCountText;
        private TextView repostsCountText;
        private TextView attachedLinkTitleText;
        private TextView attachedLinkCaptionText;
        private TextView attachmentPhotoCountText;
        private TextView signerNameText;
        private ImageView likeIcon;
        private ImageView commentIcon;
        private ImageView repostIcon;
        private ImageView optionsIcon;
        private ImageView sourcePhoto;
        private ImageView attachedPhoto;
        private ImageView attachedLinkPhoto;
        private ImageView attachmentPhotoCountIcon;
        private ImageView signerIcon;
        private RelativeLayout likeBlock;
        private RelativeLayout commentBlock;
        private RelativeLayout repostBlock;
        private RelativeLayout cardLayout;
        private RelativeLayout attachmentBlock;
        private RelativeLayout signerBlock;
        private CardView sourceInfoBlock;
        private CardView optionsBlock;
        private CardView attachedLinkBlock;
        private CardView attachmentPhotoCountBlock;
        private LinearLayout socialAcionsLayout;

        DataObjectHolder(View itemView) {
            super(itemView);

            final Context context = itemView.getContext();
            sourceNameText = (TextView) itemView.findViewById(R.id.source_name);
            dateText = (TextView) itemView.findViewById(R.id.note_date);
            addNewNoteText = (TextView) itemView.findViewById(R.id.add_note);
            contextText = (TextView) itemView.findViewById(R.id.context);
            seeMoreText = (TextView) itemView.findViewById(R.id.see_more);
            likesCountText = (TextView) itemView.findViewById(R.id.likes_count);
            commentsCountText = (TextView) itemView.findViewById(R.id.comments_count);
            repostsCountText = (TextView) itemView.findViewById(R.id.reposts_count);
            attachedLinkTitleText = (TextView) itemView.findViewById(R.id.attachment_link_title);
            attachedLinkCaptionText = (TextView) itemView.findViewById(R.id.attachment_link_caption);
            attachmentPhotoCountText = (TextView) itemView.findViewById(R.id.attachment_photo_count);
            signerNameText = (TextView) itemView.findViewById(R.id.signer_name);
            sourcePhoto = (ImageView) itemView.findViewById(R.id.source_photo);
            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            commentIcon = (ImageView) itemView.findViewById(R.id.comment_icon);
            repostIcon = (ImageView) itemView.findViewById(R.id.repost_icon);
            optionsIcon = (ImageView) itemView.findViewById(R.id.options);
            attachedPhoto = (ImageView) itemView.findViewById(R.id.attachment_photo);
            attachedLinkPhoto = (ImageView) itemView.findViewById(R.id.attachment_link_photo);
            attachmentPhotoCountIcon = (ImageView) itemView.findViewById(R.id.attachment_photo_count_icon);
            signerIcon = (ImageView) itemView.findViewById(R.id.signer_icon);
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);
            cardLayout = (RelativeLayout) itemView.findViewById(R.id.note_relative_layout);
            attachmentBlock = (RelativeLayout) itemView.findViewById(R.id.attachment_block);
            signerBlock = (RelativeLayout) itemView.findViewById(R.id.signer_block);
            sourceInfoBlock = (CardView) itemView.findViewById(R.id.source_info);
            optionsBlock = (CardView) itemView.findViewById(R.id.options_block);
            attachedLinkBlock = (CardView) itemView.findViewById(R.id.attachment_link);
            attachmentPhotoCountBlock = (CardView) itemView.findViewById(R.id.attachment_photo_count_block);
            socialAcionsLayout = (LinearLayout) itemView.findViewById(R.id.social_actions);

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
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void attemptFailed(VKRequest request,
                                                  int attemptNumber,
                                                  int totalAttempts) {
                            Toast.makeText(context, "Attempt Failed!", Toast.LENGTH_LONG).show();
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
                    likeIcon.setImageDrawable(context.getDrawable(
                            tmpNote.getUserLikes()
                                    ? R.drawable.ic_favorite_pressed_24dp
                                    : R.drawable.ic_favorite_white_24dp));
                }
            });

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
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.activity_main_card_content, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final Note tmpNote = mDataSet.get(position);
        if (position == 0) {
            holder.contextText.setVisibility(View.GONE);
            holder.seeMoreText.setVisibility(View.GONE);
            holder.socialAcionsLayout.setVisibility(View.GONE);
            holder.sourceNameText.setVisibility(View.GONE);
            holder.dateText.setVisibility(View.GONE);
            holder.addNewNoteText.setVisibility(View.VISIBLE);

            holder.optionsIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_camera_alt_gray_24dp,
                    null));
            try {
                new DownloadImageTask(holder.sourcePhoto).execute(tmpNote.getSourcePhotoUrl()).get();
            } catch (InterruptedException | ExecutionException pE) {
                pE.printStackTrace();
            }

            holder.attachmentBlock.setVisibility(View.GONE);
            holder.signerBlock.setVisibility(View.GONE);
        }
        else {
            holder.socialAcionsLayout.setVisibility(View.VISIBLE);
            holder.sourceNameText.setVisibility(View.VISIBLE);
            holder.dateText.setVisibility(View.VISIBLE);
            holder.addNewNoteText.setVisibility(View.GONE);

            View.OnClickListener openNoteByClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NoteViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    NoteViewActivity.mNote = mDataSet.get(holder.getAdapterPosition());
                    mActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_right);
                }
            };
            holder.repostBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RepostNoteDialogFragment dialog = new RepostNoteDialogFragment();
                    dialog.show(mActivity.getFragmentManager(), "kek");
                }
            });
            holder.sourceInfoBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GroupFeedViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_right);
                }
            });
            holder.cardLayout.setOnClickListener(openNoteByClick);
            holder.commentBlock.setOnClickListener(openNoteByClick);
            holder.optionsBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ...
                }
            });

            holder.sourceNameText.setText(tmpNote.getSourceName());
            holder.dateText.setText(new PrettyTime(Locale.getDefault()).format(new Date(tmpNote.getDate() *
                    1000)));

            // TODO: remove .get because of internetless cases. It can probably endlessly freeze
            // application. But need to continue loading until every this picture would be
            // loaded.
            try {
                new DownloadImageTask(holder.sourcePhoto).execute(tmpNote.getSourcePhotoUrl()).get();
            } catch (InterruptedException | ExecutionException pE) {
                pE.printStackTrace();
            }

            holder.optionsIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_more_vert_gray_24dp,
                    null));

            holder.contextText.setVisibility(tmpNote.getContext().equals("")
                    ? View.GONE
                    : View.VISIBLE);
            holder.seeMoreText.setVisibility(tmpNote.getContextPreview().equals("")
                    ? View.GONE
                    : View.VISIBLE);
            holder.contextText.setText(holder.seeMoreText.getVisibility() == View.VISIBLE
                    ? tmpNote.getContextPreview()
                    : tmpNote.getContext());

            if (tmpNote.getAttachmentsPhotos().size() > 0) {
                holder.attachedPhoto.setVisibility(View.VISIBLE);
                try {
                    new DownloadImageTask(holder.attachedPhoto).execute(tmpNote
                            .getAttachmentsPhotos().get(0).getUrl()).get();
                } catch (InterruptedException | ExecutionException pE) {
                    pE.printStackTrace();
                }
            }
            else {
                holder.attachedPhoto.setVisibility(View.GONE);
            }
            if (tmpNote.getAttachmentsPhotos().size() > 1) {
                holder.attachmentPhotoCountBlock.setVisibility(View.VISIBLE);
                holder.attachmentPhotoCountIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                VKSmartFeedApplication.context().getResources(),
                                R.drawable.ic_camera_alt_blue_24dp,
                                null));
                holder.attachmentPhotoCountText.setText(String.valueOf(
                        tmpNote.getAttachmentsPhotos().size()));
            }
            else {
                holder.attachmentPhotoCountBlock.setVisibility(View.GONE);
            }

            holder.attachmentBlock.setVisibility(
                    tmpNote.getAttachedLink() == null && tmpNote.getAttachmentsPhotos().size() == 0
                    ? View.GONE
                    : View.VISIBLE);

            if (tmpNote.getAttachedLink() != null) {
                holder.attachedLinkBlock.setVisibility(View.VISIBLE);
                holder.attachedLinkBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmpNote
                                .getAttachedLink().getUrl()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(browserIntent);
                    }
                });
                holder.attachedLinkTitleText.setText(tmpNote.getAttachedLink().getTitle());
                holder.attachedLinkCaptionText.setText(tmpNote.getAttachedLink().getCaption());
                try {
                    new DownloadImageTask(holder.attachedLinkPhoto).execute(
                        tmpNote.getAttachedLink().getPhotoUrl()).get();
                } catch (InterruptedException | ExecutionException pE) {
                    pE.printStackTrace();
                }
            }
            else {
                holder.attachedLinkBlock.setVisibility(View.GONE);
            }

            if (tmpNote.getSigner() != null) {
                holder.signerBlock.setVisibility(View.VISIBLE);
                holder.signerIcon.setImageDrawable(ResourcesCompat.getDrawable(
                        VKSmartFeedApplication.context().getResources(),
                        R.drawable.ic_account_circle_blue_24dp,
                        null));
                holder.signerNameText.setText(tmpNote.getSigner().getFirstName() + " " +
                        tmpNote.getSigner().getLastName());
                holder.signerBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ...
                    }
                });
            }
            else {
                holder.signerBlock.setVisibility(View.GONE);
            }

            holder.likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    tmpNote.getUserLikes()
                            ? R.drawable.ic_favorite_pressed_24dp
                            : R.drawable.ic_favorite_white_24dp,
                    null));

            holder.likesCountText.setText(tmpNote.getLikesCount() != 0
                    ? String.valueOf(tmpNote.getLikesCount())
                    : "");

            holder.commentBlock.setVisibility(tmpNote.getCanComment()
                    ? View.VISIBLE
                    : View.GONE);
            holder.commentIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_question_answer_white_24dp,
                    null));
            holder.commentsCountText.setText(tmpNote.getCommentsCount() != 0
                    ? String.valueOf(tmpNote.getCommentsCount())
                    : "");

            holder.repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    tmpNote.getUserReposted()
                            ? R.drawable.ic_share_pressed_24dp
                            : R.drawable.ic_share_white_24dp,
                    null));
            holder.repostsCountText.setText(tmpNote.getRepostsCount() != 0
                    ? String.valueOf(tmpNote.getRepostsCount())
                    : "");
        }
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