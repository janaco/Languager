package com.stiletto.tr.model.test;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by yana on 21.07.17.
 */

public class Result extends RealmObject implements Parcelable {

    private long timestamp;
    private int percentage;
    private int tasksCount;
    private String type;
    private boolean passed;

    public Result() {
    }

    public Result(long timestamp, int percentage, boolean passed) {
        this.timestamp = timestamp;
        this.percentage = percentage;
        this.passed = passed;
    }

    protected Result(Parcel in) {
        timestamp = in.readLong();
        percentage = in.readInt();
        tasksCount = in.readInt();
        type = in.readString();
        passed = in.readByte() != 0;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getDate(){
        return new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date(timestamp));

    }

    public void insert(Context context) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(this);
        realm.commitTransaction();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeInt(percentage);
        dest.writeInt(tasksCount);
        dest.writeString(type);
        dest.writeByte((byte) (passed ? 1 : 0));
    }
}
