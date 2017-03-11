package com.stiletto.tr.model;

import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryItem {

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

    public DictionaryItem(String originText, List<Translation> translations) {
        this.translations = translations;
        this.originText = originText;
    }

    public DictionaryItem(String originText) {
        this.originText = originText;
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

    public void addTranslation(Translation translation){
        if (translations == null){
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

    public boolean isKnownPartOfSpeech(){
        return partOfSpeech != null && !partOfSpeech.isEmpty();
    }

    public boolean hasTranscription(){
        return transcription != null && !transcription.isEmpty();
    }
}
