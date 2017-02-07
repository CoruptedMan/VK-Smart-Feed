package ru.ifmo.practice.util;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;

import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Note;

import static ru.ifmo.practice.R.id.like;
import static ru.ifmo.practice.R.id.options;

public class FeedRecyclerViewAdapter
        extends RecyclerView.Adapter<FeedRecyclerViewAdapter.DataObjectHolder> {

    private static ArrayList<Note> mDataSet;
    private Context mContext;
    /*private EndlessRecyclerViewScrollListener onLoadMoreListener;

    public interface EndlessRecyclerViewScrollListener {
        void onLoadMore();
    }*/

    public FeedRecyclerViewAdapter(Context context, ArrayList<Note> myDataset) {
        mContext = context;
        mDataSet = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_card_content, parent, false);

        return new DataObjectHolder(view);
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        private JSONObject mResponse;

        private TextView sourceNameText;
        private TextView contextText;
        private TextView dateText;
        private TextView likesCountText;
        private TextView commentsCountText;
        private TextView repostsCountText;
        private ImageView likeIcon;
        private ImageView repostIcon;
        private ImageView sourcePhoto;
        private ImageView optionsPhoto;
        private RelativeLayout likeBlock;
        private RelativeLayout commentBlock;
        private RelativeLayout repostBlock;
        private CardView sourceInfoBlock;
        private CardView optionsBlock;
        private LinearLayout mainLayout;
        private NotePhotosRecyclerViewAdapter mAdapter;
        private RecyclerView mRecyclerView;

        DataObjectHolder(View itemView) {
            super(itemView);

            sourceNameText = (TextView) itemView.findViewById(R.id.source_name);
            contextText = (TextView) itemView.findViewById(R.id.context);
            dateText = (TextView) itemView.findViewById(R.id.note_date);
            likesCountText = (TextView) itemView.findViewById(R.id.likes_count);
            commentsCountText = (TextView) itemView.findViewById(R.id.comments_count);
            repostsCountText = (TextView) itemView.findViewById(R.id.reposts_count);
            sourcePhoto = (ImageView) itemView.findViewById(R.id.source_photo);
            likeIcon = (ImageView) itemView.findViewById(like);
            repostIcon = (ImageView) itemView.findViewById(R.id.repost);
            optionsPhoto = (ImageView) itemView.findViewById(options);
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);
            sourceInfoBlock = (CardView) itemView.findViewById(R.id.source_info);
            optionsBlock = (CardView) itemView.findViewById(R.id.options_block);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.main_layout);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.note_photos_recycler_view);

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
                                Toast.makeText(VKSmartFeedApplication.context(), error.toString(), Toast
                                        .LENGTH_LONG).show();
                            }
                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                Toast.makeText(VKSmartFeedApplication.context(), "Attempt Failed!", Toast
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
                        likeIcon.setImageDrawable(VKSmartFeedApplication.context().getDrawable(
                                R.drawable.ic_favorite_white_18dp));
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
                                Toast.makeText(VKSmartFeedApplication.context(), error.toString(), Toast
                                        .LENGTH_LONG).show();
                            }
                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                Toast.makeText(VKSmartFeedApplication.context(), "Attempt Failed!", Toast
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
                        likeIcon.setImageDrawable(VKSmartFeedApplication.context().getDrawable(
                                R.drawable.ic_favorite_red_18dp));
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

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mAdapter = new NotePhotosRecyclerViewAdapter(mDataSet
                .get(position).getAttachmentsPhotos());
        holder.mRecyclerView.setAdapter(holder.mAdapter);
        holder.sourceNameText.setText(mDataSet.get(position).getSourceName());
        holder.dateText.setText(mDataSet.get(position).getDate());
        new DownloadImageTask(holder.sourcePhoto).execute(mDataSet.get(position).getPhotoUrl());

        holder.contextText.setVisibility(mDataSet.get(position).getContext().equals("")
                ?   View.GONE
                :   View.VISIBLE);
        holder.contextText.setText(mDataSet.get(position).getContext());

        holder.likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.context().getResources(),
                mDataSet.get(position).getUserLikes()
                ?   R.drawable.ic_favorite_red_18dp
                :   R.drawable.ic_favorite_white_18dp,
                null));

        holder.likesCountText.setText(mDataSet.get(position).getLikesCount() != 0
                ?   String.valueOf(mDataSet.get(position).getLikesCount())
                :   "");

        holder.commentBlock.setVisibility(mDataSet.get(position).getCanComment()
                ?   View.VISIBLE
                :   View.GONE);
        holder.commentsCountText.setText(mDataSet.get(position).getCommentsCount() != 0
                ?   String.valueOf(mDataSet.get(position).getCommentsCount())
                :   "");

        holder.repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.context().getResources(),
                mDataSet.get(position).getUserReposted()
                ?   R.drawable.ic_reply_yellow_18dp
                :   R.drawable.ic_reply_white_18dp,
                null));
        holder.repostsCountText.setText(mDataSet.get(position).getRepostsCount() != 0
                ?   String.valueOf(mDataSet.get(position).getRepostsCount())
                :   "");
    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();

    }

    public void addAll(ArrayList<Note> list) {
        for (int i = 0; i < list.size(); i++) {
            mDataSet.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}