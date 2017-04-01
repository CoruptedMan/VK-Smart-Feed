package ru.ifmo.practice.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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

import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;
import ru.ifmo.practice.model.Comment;

public class NoteCommentsRecyclerViewAdapter
        extends RecyclerView.Adapter<NoteCommentsRecyclerViewAdapter.DataObjectHolder> {

    private static ArrayList<Comment> mDataSet;
    private final Context mContext;
    private static long mSourceId;

    public NoteCommentsRecyclerViewAdapter(Context context, long sourceId, ArrayList<Comment>
            dataSet) {
        mContext = context;
        mSourceId = sourceId;
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
                .inflate(R.layout.component_note_comment, parent, false);

        return new NoteCommentsRecyclerViewAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Comment tmpComment = mDataSet.get(position);
        holder.authorNameText.setText(tmpComment.getAuthorName());
        holder.dateText.setText(new PrettyTime(Locale.getDefault()).format(new Date(tmpComment.getDate() *
                1000)));
        holder.authorPhoto.setImageBitmap(tmpComment.getAuthorPhoto().getImageBitmap());

        holder.contextText.setText(tmpComment.getContext());

        holder.likesCountText.setText(tmpComment.getLikesCount() > 0
                ? String.valueOf(tmpComment.getLikesCount())
                : "");

        holder.likeIcon.setImageDrawable(ResourcesCompat.getDrawable(
                VKSmartFeedApplication.getContext().getResources(),
                tmpComment.isUserLikes()
                        ? R.drawable.ic_favorite_pressed_24dp
                        : tmpComment.getLikesCount() > 0
                            ? R.drawable.ic_favorite_darkgray_24dp
                            : R.drawable.ic_favorite_gray_24dp,
                null));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    final static class DataObjectHolder extends RecyclerView.ViewHolder {
        private JSONObject mResponse;
        @BindView(R.id.comment_author_name) TextView authorNameText;
        @BindView(R.id.comment_context) TextView contextText;
        @BindView(R.id.comment_date) TextView dateText;
        @BindView(R.id.comment_likes_count) TextView likesCountText;
        @BindView(R.id.comment_like_icon) ImageView likeIcon;
        @BindView(R.id.comment_author_photo) ImageView authorPhoto;
        @BindView(R.id.comment_like_block) RelativeLayout likeBlock;
        @BindView(R.id.comment_view) RelativeLayout mainLayout;
        @BindView(R.id.comment_author_info) CardView authorInfoBlock;

        DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            final Context context = itemView.getContext();

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            likeBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Comment tmpComment = mDataSet.get(getAdapterPosition());
                VKRequest request;
                int likes = tmpComment.getLikesCount();
                request = new VKRequest("likes." + (tmpComment.isUserLikes()
                        ? "delete" : "add"), VKParameters.from("type", "comment",
                        "owner_id", -mSourceId,
                        "item_id", tmpComment.getId()));
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
                tmpComment.setLikesCount(likes);
                likesCountText.setText(tmpComment.getLikesCount() > 0
                        ? String.valueOf(likes)
                        : "");
                tmpComment.setUserLikes(
                        !tmpComment.isUserLikes());
                likeIcon.setImageDrawable(context.getDrawable(
                        tmpComment.isUserLikes()
                                ? R.drawable.ic_favorite_pressed_24dp
                                : tmpComment.getLikesCount() > 0
                                    ? R.drawable.ic_favorite_darkgray_24dp
                                    : R.drawable.ic_favorite_gray_24dp));
                }
            });

            authorInfoBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}