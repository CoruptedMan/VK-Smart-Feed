package ru.ifmo.practice.util;

import android.content.Context;
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

import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Note;

public class FeedRecyclerViewAdapter
        extends RecyclerView.Adapter<FeedRecyclerViewAdapter.DataObjectHolder> {

    private static ArrayList<Note> mDataSet;
    private final Context mContext;

    final static class DataObjectHolder extends RecyclerView.ViewHolder {
        private JSONObject mResponse;
        private TextView sourceNameText;
        private TextView contextText;
        private TextView seeMoreText;
        private TextView dateText;
        private TextView likesCountText;
        private TextView commentsCountText;
        private TextView repostsCountText;
        private ImageView likeIcon;
        private ImageView commentIcon;
        private ImageView repostIcon;
        private ImageView optionsPhoto;
        private ImageView sourcePhoto;
        private RelativeLayout likeBlock;
        private RelativeLayout commentBlock;
        private RelativeLayout repostBlock;
        private CardView sourceInfoBlock;
        private CardView optionsBlock;
        private CardView cardLayout;
        private LinearLayout mainLayout;
        private LinearLayout socialAcionsLayout;

        DataObjectHolder(View itemView) {
            super(itemView);

            final Context context = itemView.getContext();
            sourceNameText = (TextView) itemView.findViewById(R.id.source_name);
            contextText = (TextView) itemView.findViewById(R.id.context);
            seeMoreText = (TextView) itemView.findViewById(R.id.see_more);
            dateText = (TextView) itemView.findViewById(R.id.note_date);
            likesCountText = (TextView) itemView.findViewById(R.id.likes_count);
            commentsCountText = (TextView) itemView.findViewById(R.id.comments_count);
            repostsCountText = (TextView) itemView.findViewById(R.id.reposts_count);
            sourcePhoto = (ImageView) itemView.findViewById(R.id.source_photo);
            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            commentIcon = (ImageView) itemView.findViewById(R.id.comment_icon);
            repostIcon = (ImageView) itemView.findViewById(R.id.repost_icon);
            optionsPhoto = (ImageView) itemView.findViewById(R.id.options);
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);
            sourceInfoBlock = (CardView) itemView.findViewById(R.id.source_info);
            optionsBlock = (CardView) itemView.findViewById(R.id.options_block);
            cardLayout = (CardView) itemView.findViewById(R.id.note);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.main_layout);
            socialAcionsLayout = (LinearLayout) itemView.findViewById(R.id.social_actions);

            likeBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VKRequest request;
                    int likes = mDataSet.get(getAdapterPosition()).getLikesCount();
                    //TODO: complete request for correct liking all types of content.
                    if (mDataSet.get(getAdapterPosition()).getUserLikes()) {
                        request = new VKRequest("likes.delete", VKParameters.from(
                                "type", "post",
                                "owner_id", -mDataSet.get(getAdapterPosition()).getSourceId(),
                                "item_id", mDataSet.get(getAdapterPosition()).getId()));
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
                                Toast.makeText(context, error.toString(), Toast
                                        .LENGTH_LONG).show();
                            }
                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                Toast.makeText(context, "Attempt Failed!", Toast
                                        .LENGTH_LONG).show();
                            }
                        });

                        try {
                            likes = Integer.parseInt(mResponse.get("likes").toString());
                        } catch (JSONException pE) {
                            pE.printStackTrace();
                        }
                        mDataSet.get(getAdapterPosition()).setLikesCount(likes);
                        likesCountText.setText(mDataSet.get(
                                getAdapterPosition()).getLikesCount() != 0
                                ? String.valueOf(likes)
                                : "");
                        mDataSet.get(getAdapterPosition()).setUserLikes(false);
                        likeIcon.setImageDrawable(context.getDrawable(
                                R.drawable.ic_favorite_white_24dp));
                    }
                    else {
                        request = new VKRequest("likes.add", VKParameters.from(
                                "type", "post",
                                "owner_id", -mDataSet.get(getAdapterPosition()).getSourceId(),
                                "item_id", mDataSet.get(getAdapterPosition()).getId()));
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
                                Toast.makeText(context, error.toString(), Toast
                                        .LENGTH_LONG).show();
                            }
                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                Toast.makeText(context, "Attempt Failed!", Toast
                                        .LENGTH_LONG).show();
                            }
                        });

                        try {
                            likes = Integer.parseInt(mResponse.get("likes").toString());
                        } catch (JSONException pE) {
                            pE.printStackTrace();
                        }
                        mDataSet.get(getAdapterPosition()).setLikesCount(likes);
                        likesCountText.setText(String.valueOf(likes));
                        mDataSet.get(getAdapterPosition()).setUserLikes(true);
                        likeIcon.setImageDrawable(context.getDrawable(
                                R.drawable.ic_favorite_pressed_24dp));
                    }

                }
            });

            commentBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ...
                }
            });

            repostBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ...
                }
            });

            sourceInfoBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ...
                }
            });

            optionsBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ...
                }
            });

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(mDataSet.get(getAdapterPosition()).toString());
                }
            });
        }
    }

    public FeedRecyclerViewAdapter(Context context, ArrayList<Note> dataSet) {
        mContext = context;
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
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        Note tmpNote = mDataSet.get(position);
        if (position == 0) {
            holder.cardLayout.setRadius(0);
            holder.cardLayout.setCardElevation(12);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 18);
            holder.cardLayout.setLayoutParams(layoutParams);

            holder.contextText.setVisibility(View.GONE);
            holder.seeMoreText.setVisibility(View.GONE);
            holder.socialAcionsLayout.setVisibility(View.GONE);

            holder.optionsPhoto.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_camera_alt_white_24dp,
                    null));
            holder.sourceNameText.setText(tmpNote.getSourceName());
            holder.dateText.setText("Что у Вас нового?");
            new DownloadImageTask(holder.sourcePhoto).execute(tmpNote.getSourcePhotoUrl());
        }
        else {
            holder.cardLayout.setRadius(9);
            holder.cardLayout.setCardElevation(9);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(12, 12, 12, 12);
            holder.cardLayout.setLayoutParams(layoutParams);

            holder.socialAcionsLayout.setVisibility(View.VISIBLE);

            holder.sourceNameText.setText(tmpNote.getSourceName());
            holder.dateText.setText(new PrettyTime(Locale.getDefault()).format(new Date(tmpNote.getDate() *
                    1000)));
            new DownloadImageTask(holder.sourcePhoto).execute(tmpNote.getSourcePhotoUrl());

            holder.optionsPhoto.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_more_vert_white_24dp,
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
                            ? R.drawable.ic_reply_pressed_24dp
                            : R.drawable.ic_reply_white_24dp,
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