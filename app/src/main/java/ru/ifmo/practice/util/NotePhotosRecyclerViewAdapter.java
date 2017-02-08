package ru.ifmo.practice.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ru.ifmo.practice.R;
import ru.ifmo.practice.model.Photo;

class NotePhotosRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<Photo> mDataSet;
    private int mRowIndex = -1;

    NotePhotosRecyclerViewAdapter() {
    }

    public void setData(ArrayList<Photo> data) {
        if (mDataSet != data) {
            mDataSet = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_photos_list_content, parent, false);
        return new DataObjectHolder(view);
    }

    private static class DataObjectHolder extends RecyclerView.ViewHolder {
        private ImageView contentPhoto;

        DataObjectHolder(View itemView) {
            super(itemView);
            contentPhoto = (ImageView) itemView.findViewById(R.id.content);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder pHolder, int position) {
        DataObjectHolder holder = (DataObjectHolder) pHolder;
        new DownloadImageTask(holder.contentPhoto).execute(mDataSet.get(position).getThumbnailUrl());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
