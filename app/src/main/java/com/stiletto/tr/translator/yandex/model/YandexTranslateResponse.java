package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {
 * "code": 200,
 * "lang": "en-uk",
 * "text": [
 * "Проект"
 * ]
 * }
 * <p>
 * Created by yana on 05.01.17.
 */

public class YandexTranslateResponse {

    @SerializedName("text") private String[] text;

    public DictionaryItem getAsDictionaryItem(CharSequence origin) {
        List<Translation> translations = new ArrayList<>();

        for (String s: text){
            translations.add(new Translation(s));
        }

        return new DictionaryItem(origin.toString(), translations);
    }


}
