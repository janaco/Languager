package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by yana on 08.01.17.
 */

public class DictionaryWord {

    @SerializedName("text")
    private String originText;

    @SerializedName("pos")
    private String wordType;

    @SerializedName("ts") private String transcryption;
    @SerializedName("fl") private String forms;

    @SerializedName("tr")
    private DictionaryTranslation[] translations;

    public String getOriginText() {
        return originText;
    }

    public String getWordType() {
        return wordType;
    }

    public DictionaryTranslation[] getTranslations() {
        return translations;
    }

    public String getTranscryption() {
        return transcryption;
    }

    public String getForms() {
        return forms;
    }

    public boolean hasWordType(){
        return wordType != null && !wordType.isEmpty();
    }

    public boolean hasTranscryption(){
        return transcryption != null && !transcryption.isEmpty();
    }

    public boolean hasForms(){
        return forms != null && !forms.isEmpty();
    }

    @Override
    public String toString() {
        return "Dictionary{" + "\n" +
                "originText='" + originText + '\'' + "\n" +
                ", wordType='" + wordType + '\'' + "\n" +
                ", translations=" + Arrays.toString(translations) + "\n" +
                '}' + "\n";
    }
}
