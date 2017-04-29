package com.stiletto.tr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Part of Yandex Dictionary API Response.
 * Examples of possible word usages.
 *
 * Created by yana on 11.03.17.
 */

public class UsageSample extends  Text implements Parcelable{

    @SerializedName("tr") private List<Text> translations;

    public UsageSample(String text, List<Text> translations) {
        super(text);
        this.translations = translations;
    }

    protected UsageSample(Parcel in) {
        super(in);
        translations = in.createTypedArrayList(Text.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(translations);
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

    public List<Text> getTranslations() {
        return translations;
    }

    public String getTranslationsAsString(){
        return translations.toString().replace("[", "").replace("]", "");
    }

    public boolean hasTranslations(){
        return translations != null && translations.size() > 0;
    }

    public void setTranslations(List<Text> translations) {
        this.translations = translations;
    }
}
