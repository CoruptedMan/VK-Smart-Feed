package ru.ifmo.practice.utils;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import ru.ifmo.practice.model.Wall;

import static ru.ifmo.practice.R.id.like;
import static ru.ifmo.practice.R.id.options;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static ArrayList<Wall> mDataSet;
    private static long mNoteId;
    private static MyClickListener myClickListener;

    public ArrayList<Wall> getDataSet() {
        return mDataSet;
    }

    public void setDataSet(ArrayList<Wall> pDataSet) {
        mDataSet = pDataSet;
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        private JSONObject mResponse;

        TextView sourceNameText;
        TextView contextText;
        TextView dateText;
        TextView likesCountText;
        TextView commentsCountText;
        TextView repostsCountText;
        ImageView likeIcon;
        ImageView commentIcon;
        ImageView repostIcon;
        ImageView sourcePhoto;
        ImageView optionsPhoto;
        RelativeLayout likeBlock;
        RelativeLayout commentBlock;
        RelativeLayout repostBlock;
        CardView sourceInfoBlock;
        CardView optionsBlock;
        LinearLayout mainLayout;

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
            commentIcon = (ImageView) itemView.findViewById(R.id.comment);
            repostIcon = (ImageView) itemView.findViewById(R.id.repost);
            optionsPhoto = (ImageView) itemView.findViewById(options);
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);
            sourceInfoBlock = (CardView) itemView.findViewById(R.id.source_info);
            optionsBlock = (CardView) itemView.findViewById(R.id.options_block);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.main_layout);

            likeBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: complete request for correct liking all types of content.
                    if (mDataSet.get(getAdapterPosition()).getUserLikes()) {
                        VKRequest request = new VKRequest("likes.delete",
                                VKParameters.from("type", "post",
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

                        int likes = 0;
                        try {
                            likes = Integer.parseInt(mResponse.get("likes").toString());
                        } catch (JSONException pE) {
                            pE.printStackTrace();
                        }
                        mDataSet.get(getAdapterPosition()).setLikesCount(likes);
                        if (mDataSet.get(getAdapterPosition()).getLikesCount() == 0) {
                            likesCountText.setText("");
                        }
                        else {
                            likesCountText.setText(String.valueOf(likes));
                        }
                        mDataSet.get(getAdapterPosition()).setUserLikes(false);
                        likeIcon.setImageDrawable(VKSmartFeedApplication.context().getDrawable(
                                R.drawable.ic_favorite_white_18dp));
                    }
                    else {
                        VKRequest request = new VKRequest("likes.add",
                                VKParameters.from("type", "post",
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

                        int likes = 0;
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
                }
            });

            repostBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            sourceInfoBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            optionsBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            String LOG_TAG = "MyRecyclerViewAdapter";
            Log.i(LOG_TAG, "Adding Listener");
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getAdapterPosition(), v);
                }
            });*/
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        MyRecyclerViewAdapter.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<Wall> myDataset) {
        mDataSet = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_card_content, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        mNoteId = mDataSet.get(position).getId();
        holder.sourceNameText.setText(mDataSet.get(position).getSourceName());
        holder.dateText.setText(mDataSet.get(position).getDate());
        new DownloadImageTask(holder.sourcePhoto).execute(mDataSet.get(position).getPhotoUrl());

        if (!mDataSet.get(position).getContext().equals("")) {
            holder.contextText.setText(mDataSet.get(position).getContext());
        }
        else {
            holder.contextText.setVisibility(View.GONE);
        }

        if (mDataSet.get(position).getUserLikes()) {
            holder.likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_favorite_red_18dp,
                    null));
        }
        if (mDataSet.get(position).getLikesCount() != 0) {
            holder.likesCountText.setText(String.valueOf(mDataSet.get(position).getLikesCount()));
        }
        if (mDataSet.get(position).getCanComment()) {
            if (mDataSet.get(position).getCommentsCount() != 0) {
                holder.commentsCountText.setText(String.valueOf(mDataSet.get(position).getCommentsCount()));
            }
        }
        else {
            holder.commentBlock.setVisibility(View.GONE);
        }
        if (mDataSet.get(position).getUserReposted()) {
            holder.repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_reply_yellow_18dp,
                    null));
        }
        if (mDataSet.get(position).getRepostsCount() != 0) {
            holder.repostsCountText.setText(String.valueOf(mDataSet.get(position).getRepostsCount()));
        }

    }

    public void addItem(Wall dataObj, int index) {
        mDataSet.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}