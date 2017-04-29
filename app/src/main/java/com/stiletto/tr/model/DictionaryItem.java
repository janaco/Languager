package com.stiletto.tr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model to represent dictionary item (based on Yandex Dictionary API response).
 * <p>
 * Created by yana on 08.03.17.
 */

public class DictionaryItem implements Parcelable {

    @SerializedName("text")
    private String originText;
    @SerializedName("pos")
    private String partOfSpeech;
    @SerializedName("ts")
    private String transcription;
    @SerializedName("tr")
    private List<Translation> translations;

    private Language originLanguage;
    private Language translationLanguage;
    private String bookId;
    private Status status;
    private String mainTranslation;

    public DictionaryItem(String originText, List<Translation> translations) {
        this.translations = translations;
        this.originText = originText;
    }

    public DictionaryItem(String originText) {
        this.originText = originText;
    }

    protected DictionaryItem(Parcel in) {
        originText = in.readString();
        partOfSpeech = in.readString();
        transcription = in.readString();
        translations = in.createTypedArrayList(Translation.CREATOR);
        bookId = in.readString();
        mainTranslation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originText);
        dest.writeString(partOfSpeech);
        dest.writeString(transcription);
        dest.writeTypedList(translations);
        dest.writeString(bookId);
        dest.writeString(mainTranslation);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getMainTranslation() {
        return mainTranslation;
    }

    public void setMainTranslation(String mainTranslation) {
        this.mainTranslation = mainTranslation;
    }

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
        this.translations = translations;
    }

    public void addTranslation(Translation translation) {
        if (translations == null) {
            setTranslations(new ArrayList<Translation>());
        }
        translations.add(translation);
    }

    public Language getOriginLanguage() {
        return originLanguage;
    }

    public void setOriginLanguage(Language originLanguage) {
        this.originLanguage = originLanguage;
    }

    public Language getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(Language translationLanguage) {
        this.translationLanguage = translationLanguage;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public static String getTranslation(List<DictionaryItem> items) {
        StringBuilder builder = new StringBuilder();
        for (DictionaryItem item : items) {

            if (item.getTranslations() != null && item.getTranslations().size() > 0) {
                Translation translation = item.translations.get(0);
                builder.append(translation.getText());
                builder.append(", ");
            }
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 2) : builder.toString();
    }

}
