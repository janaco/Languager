package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by yana on 02.07.17.
 */

public class RealmString extends RealmObject implements Parcelable{

    private String content;

    public RealmString(){}

    public RealmString(String content){
        this.content = content;
    }

    protected RealmString(Parcel in) {
        content = in.readString();
    }

    public static final Creator<RealmString> CREATOR = new Creator<RealmString>() {
        @Override
        public RealmString createFromParcel(Parcel in) {
            return new RealmString(in);
        }

        @Override
        public RealmString[] newArray(int size) {
            return new RealmString[size];
        }
    };

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

    public static RealmString[] convert(String []text){
        RealmString []realmStrings = new RealmString[text.length];

        for (int i = 0; i <text.length; i++){
            realmStrings[i] = new RealmString(text[i]);
        }

        return realmStrings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }
}
