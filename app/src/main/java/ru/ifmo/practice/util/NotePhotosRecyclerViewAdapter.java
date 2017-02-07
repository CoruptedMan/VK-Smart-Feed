package ru.ifmo.practice.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ru.ifmo.practice.R;
import ru.ifmo.practice.model.Photo;

public class NotePhotosRecyclerViewAdapter
        extends RecyclerView.Adapter<NotePhotosRecyclerViewAdapter.DataObjectHolder> {

    private static ArrayList<Photo> mDataSet;

    public NotePhotosRecyclerViewAdapter(ArrayList<Photo> myDataset) {
        mDataSet = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_photos_list_content, parent, false);
        return new DataObjectHolder(view);
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        private ImageView contentPhoto;

        DataObjectHolder(View itemView) {
            super(itemView);
            contentPhoto = (ImageView) itemView.findViewById(R.id.content);
        }
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        new DownloadImageTask(holder.contentPhoto).execute(mDataSet.get(position).getThumbnailUrl());
        System.out.println(mDataSet.get(position).getThumbnailUrl());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
