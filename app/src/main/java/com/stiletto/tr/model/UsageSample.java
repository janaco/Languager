package com.stiletto.tr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yana on 11.03.17.
 */

public class UsageSample extends  Text implements Parcelable{

    @SerializedName("tr") private Text[] translations;

    public UsageSample(String text, Text[] translations) {
        super(text);
        this.translations = translations;
    }

    protected UsageSample(Parcel in) {
        super(in);
        translations = in.createTypedArray(Text.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedArray(translations, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UsageSample> CREATOR = new Creator<UsageSample>() {
        @Override
        public UsageSample createFromParcel(Parcel in) {
            return new UsageSample(in);
        }

        @Override
        public UsageSample[] newArray(int size) {
            return new UsageSample[size];
        }
    };

    public Text[] getTranslations() {
        return translations;
    }

    public void setTranslations(Text[] translations) {
        this.translations = translations;
    }
}
