package com.stiletto.tr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Part of Dictionary Item Yandex Response.
 *
 * Created by yana on 11.03.17.
 */

public class Text implements Parcelable {

    @SerializedName("text")
    private String text;

    Text(String text) {
        this.text = text;
    }

    Text(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Text> CREATOR = new Creator<Text>() {
        @Override
        public Text createFromParcel(Parcel in) {
            return new Text(in);
        }

        @Override
        public Text[] newArray(int size) {
            return new Text[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
