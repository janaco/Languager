package com.nandy.reader.model.word;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yana on 02.07.17.
 */

public class Dictionary extends RealmObject{

    @SerializedName("def")
    private RealmList<DictionaryItem> items;

    public Dictionary(){}

    public List<DictionaryItem> getItems() {
        return items;
    }



}
