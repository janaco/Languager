package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by yana on 08.01.17.
 */

public class Dictionary {

    @SerializedName("def") private DictionaryWord [] dictionary;

    public DictionaryWord[] getDictionary() {
        return dictionary;
    }

    @Override
    public String toString() {
        return "Dictionary{" + "\n" +
                "dictionary=" + Arrays.toString(dictionary) + "\n" +
                '}' + "\n";
    }
}
