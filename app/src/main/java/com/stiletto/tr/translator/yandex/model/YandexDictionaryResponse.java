package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.model.DictionaryItem;

import java.util.Arrays;

/**
 * Created by yana on 08.01.17.
 */

public class YandexDictionaryResponse {

    @SerializedName("def") private DictionaryItem[] items;

    public DictionaryItem[] getItems() {
        return items;
    }
}
