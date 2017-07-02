package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yana on 02.07.17.
 */

public class Dictionary extends RealmObject implements Parcelable{

    @SerializedName("def")
    private RealmList<DictionaryItem> items;

    public Dictionary(){}

    protected Dictionary(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dictionary> CREATOR = new Creator<Dictionary>() {
        @Override
        public Dictionary createFromParcel(Parcel in) {
            return new Dictionary(in);
        }

        @Override
        public Dictionary[] newArray(int size) {
            return new Dictionary[size];
        }
    };

    public List<DictionaryItem> getItems() {
        return items;
    }

}
