package com.stiletto.tr.model;

import com.stiletto.tr.translator.PartOfSpeech;
import com.stiletto.tr.translator.yandex.Language;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryItem {

    private String origin;
    private String translation;
    private String transcription;
    private PartOfSpeech partOfSpeech = PartOfSpeech.UNKNOWN;
    private Language originLanguage;
    private Language translationLanguage;

    public DictionaryItem(String origin, String translation) {
        this.origin = origin;
        this.translation = translation;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTranscription() {
        return transcription;
    }

    public boolean hasTranscription(){
        return transcription != null && !transcription.isEmpty();
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public boolean hasPartOfSpeech(){
        return partOfSpeech != null && partOfSpeech != PartOfSpeech.UNKNOWN;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
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
}
