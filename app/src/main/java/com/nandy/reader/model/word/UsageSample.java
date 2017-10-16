package com.nandy.reader.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Part of Yandex Dictionary API Response.
 * Examples of possible word usages.
 * <p>
 * Created by yana on 11.03.17.
 */

public class UsageSample extends RealmObject implements Parcelable{

    @SerializedName("tr")
    private RealmList<Text> translations;
    @SerializedName("text")
    private String text;

    public UsageSample(){}

    protected UsageSample(Parcel in) {
        text = in.readString();
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

    public String getTranslationsAsString() {
        StringBuilder builder = new StringBuilder();
        for (Text text: translations){
            builder.append(text.getText()).append(", ");
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 2) : builder.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean hasTranslations() {
        return translations != null && translations.size() > 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
}
