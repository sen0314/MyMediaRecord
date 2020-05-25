package com.demo.mymediarecord;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 录音文件的实体类
 */
public class RecordingItem implements Parcelable {

    private static final String TAG = "RecordingItem";

    private String mName;
    private String mFilePath;
    private int mId;
    private int mLength;
    private long mTime;

    public RecordingItem() {
    }

    public RecordingItem(Parcel in) {
        Log.d(TAG, "RecordingItem(Parcel in) ==== ");
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mLength = in.readInt();
        mTime = in.readLong();
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public static final Parcelable.Creator<RecordingItem> CREATOR = new Parcelable.Creator<RecordingItem>() {
        @Override
        public RecordingItem createFromParcel(Parcel source) {
            Log.d(TAG, "createFromParcel ==== ");
            return new RecordingItem(source);
        }

        @Override
        public RecordingItem[] newArray(int size) {
            Log.d(TAG, "newArray ==== ");
            return new RecordingItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel ==== ");
        dest.writeString(mName);
        dest.writeString(mFilePath);
        dest.writeInt(mId);
        dest.writeInt(mLength);
        dest.writeLong(mTime);
    }

    @Override
    public int describeContents() {
        Log.d(TAG, "describeContents ==== ");
        return 0;
    }
}
