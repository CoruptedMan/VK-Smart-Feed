package ru.ifmo.practice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.ifmo.practice.model.Comment;

public class NoteCommentsRecyclerViewAdapter
        extends RecyclerView.Adapter<NoteCommentsRecyclerViewAdapter.DataObjectHolder> {

    private static ArrayList<Comment> mDataSet;
    private final Context mContext;

    public NoteCommentsRecyclerViewAdapter(Context context, ArrayList<Comment> dataSet) {
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
        return null;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    final static class DataObjectHolder extends RecyclerView.ViewHolder {

        public DataObjectHolder(View itemView) {
            super(itemView);
        }
    }
}
