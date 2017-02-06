package ru.ifmo.practice.utils;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Wall;

import static ru.ifmo.practice.R.id.like;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private ArrayList<Wall> mDataSet;
    private static MyClickListener myClickListener;

    public ArrayList<Wall> getDataSet() {
        return mDataSet;
    }

    public void setDataSet(ArrayList<Wall> pDataSet) {
        mDataSet = pDataSet;
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
            //implements View
            //.OnClickListener
        TextView sourceNameText;
        TextView contextText;
        TextView dateText;
        TextView likesCountText;
        TextView commentsCountText;
        TextView repostsCountText;
        ImageView likeIcon;
        ImageView commentIcon;
        ImageView repostIcon;
        RelativeLayout likeBlock;
        RelativeLayout commentBlock;
        RelativeLayout repostBlock;
        RoundedImageView sourcePhoto;

        DataObjectHolder(View itemView) {
            super(itemView);
            sourceNameText = (TextView) itemView.findViewById(R.id.source_name);
            contextText = (TextView) itemView.findViewById(R.id.context);
            dateText = (TextView) itemView.findViewById(R.id.note_date);
            likesCountText = (TextView) itemView.findViewById(R.id.likes_count);
            commentsCountText = (TextView) itemView.findViewById(R.id.comments_count);
            repostsCountText = (TextView) itemView.findViewById(R.id.reposts_count);
            sourcePhoto = (RoundedImageView) itemView.findViewById(R.id.source_photo);
            likeIcon = (ImageView) itemView.findViewById(like);
            commentIcon = (ImageView) itemView.findViewById(R.id.comment);
            repostIcon = (ImageView) itemView.findViewById(R.id.repost);
            likeBlock = (RelativeLayout) itemView.findViewById(R.id.like_block);
            commentBlock = (RelativeLayout) itemView.findViewById(R.id.comment_block);
            repostBlock = (RelativeLayout) itemView.findViewById(R.id.repost_block);

            String LOG_TAG = "MyRecyclerViewAdapter";
            Log.i(LOG_TAG, "Adding Listener");
            //itemView.setOnClickListener(this);
        }



        /*@Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }*/
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
        holder.sourceNameText.setText(mDataSet.get(position).getSourceName());
        holder.contextText.setText(mDataSet.get(position).getContext());
        holder.dateText.setText(mDataSet.get(position).getDate());

        System.out.println("User " + (mDataSet.get(position).getCanLike() ? "can" : "can't") + " " +
                "like.");
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
        } else holder.commentBlock.setVisibility(View.GONE);
        if (mDataSet.get(position).getUserReposted()) {
            holder.repostIcon.setImageDrawable(ResourcesCompat.getDrawable(
                    VKSmartFeedApplication.context().getResources(),
                    R.drawable.ic_reply_yellow_18dp,
                    null));
        }
        if (mDataSet.get(position).getRepostsCount() != 0) {
            holder.repostsCountText.setText(String.valueOf(mDataSet.get(position).getRepostsCount()));
        }

        new DownloadImageTask(holder.sourcePhoto).execute(mDataSet.get(position).getPhotoUrl());
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