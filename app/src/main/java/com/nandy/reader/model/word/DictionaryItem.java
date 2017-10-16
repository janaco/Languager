package com.nandy.reader.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yana on 02.07.17.
 */

public class DictionaryItem extends RealmObject {
    @SerializedName("text")
    private String originText;
    @SerializedName("pos")
    private String partOfSpeech;
    @SerializedName("ts")
    private String transcription;
    @SerializedName("tr")
    private RealmList<Translation> translations;

    public DictionaryItem(){}

    public String getOriginText() {
        return originText;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getTranscription() {
        return transcription;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public String getTranslationsAsString() {
        StringBuilder builder = new StringBuilder();
        int index = 0;

        for (Translation translation : translations) {

            builder.append(translation.getText());
            if (index++ < translations.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = (RealmList<Translation>) translations;
    }

    public boolean isKnownPartOfSpeech() {
        return partOfSpeech != null && !partOfSpeech.isEmpty();
    }

    public boolean hasTranscription() {
        return transcription != null && !transcription.isEmpty();
    }

}