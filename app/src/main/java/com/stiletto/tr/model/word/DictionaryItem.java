package com.stiletto.tr.model.word;

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

public class DictionaryItem extends RealmObject implements Parcelable{
    @SerializedName("text")
    private String originText;
    @SerializedName("pos")
    private String partOfSpeech;
    @SerializedName("ts")
    private String transcription;
    @SerializedName("tr")
    private RealmList<Translation> translations;

    public DictionaryItem(){}

    protected DictionaryItem(Parcel in) {
        originText = in.readString();
        partOfSpeech = in.readString();
        transcription = in.readString();
    }

    public static final Creator<DictionaryItem> CREATOR = new Creator<DictionaryItem>() {
        @Override
        public DictionaryItem createFromParcel(Parcel in) {
            return new DictionaryItem(in);
        }

        @Override
        public DictionaryItem[] newArray(int size) {
            return new DictionaryItem[size];
        }
    };

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
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

    public void addTranslation(Translation translation) {
        if (translations == null) {
            setTranslations(new ArrayList<Translation>());
        }
        translations.add(translation);
    }

    public boolean isKnownPartOfSpeech() {
        return partOfSpeech != null && !partOfSpeech.isEmpty();
    }

    public boolean hasTranscription() {
        return transcription != null && !transcription.isEmpty();
    }

    public String getAsJson() {
        return new Gson().toJson(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originText);
        dest.writeString(partOfSpeech);
        dest.writeString(transcription);
    }
}