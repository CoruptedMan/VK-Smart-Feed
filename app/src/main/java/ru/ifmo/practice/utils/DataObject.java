package ru.ifmo.practice.utils;

import java.util.Date;

public class DataObject {
    private long mId;
    private boolean mGroup;
    private String mSourceName;
    private String mContext;
    private Date mDate;

    public DataObject(long pId, String pSourceName, String pContext, Date pDate) {
        mId = pId;
        mSourceName = pSourceName;
        mContext = pContext;
        mDate = pDate;
    }

    public long getId() {
        return mId;
    }

    public void setId(long pId) {
        mId = pId;
    }

    public boolean isGroup() {
        return mGroup;
    }

    public void setGroup(boolean pGroup) {
        mGroup = pGroup;
    }

    public String getSourceName() {
        return mSourceName;
    }

    public void setSourceName(String pSourceName) {
        mSourceName = pSourceName;
    }

    public String getContext() {
        return mContext;
    }

    public void setContext(String pContext) {
        mContext = pContext;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date pDate) {
        mDate = pDate;
    }

    @Override
    public String toString() {
        return (mGroup ? "Group " : " ") + "id" + mId + "| " + mSourceName + "\n" + mContext +
                "\nat " + mDate;
    }
}