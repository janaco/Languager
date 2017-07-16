package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.stiletto.tr.translator.yandex.Language;

import io.realm.RealmObject;

/**
 * Created by yana on 16.07.17.
 */

public class WordInfo extends RealmObject implements Parcelable{

    private String bookId;

    private String originLanguage;
    private String translationLanguage;

    private String status = "Unknown";

    public WordInfo(){}

    public WordInfo(Language []languages) {
        this.originLanguage = languages[0].toString();
        this.translationLanguage = languages[1].toString();
    }

    protected WordInfo(Parcel in) {
        bookId = in.readString();
        originLanguage = in.readString();
        translationLanguage = in.readString();
        status = in.readString();
    }

    public static final Creator<WordInfo> CREATOR = new Creator<WordInfo>() {
        @Override
        public WordInfo createFromParcel(Parcel in) {
            return new WordInfo(in);
        }

        @Override
        public WordInfo[] newArray(int size) {
            return new WordInfo[size];
        }
    };

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOriginLanguage() {
        return originLanguage;
    }

    public void setOriginLanguage(Language originLanguage) {
        this.originLanguage = originLanguage.toString();
    }

    public String getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(Language translationLanguage) {
        this.translationLanguage = translationLanguage.toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(originLanguage);
        dest.writeString(translationLanguage);
        dest.writeString(status);
    }
}
