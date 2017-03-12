package com.stiletto.tr.model;

import android.support.annotation.NonNull;

import com.stiletto.tr.translator.yandex.Language;

import java.util.List;
import java.util.Objects;

/**
 * Created by yana on 12.03.17.
 */

public class Word implements Comparable<Word>{

    private String text;
    private String bookId;
    private Language originLanguage;
    private Language translationLanguage;
    private List<DictionaryItem> dictionaryItems;

    public Word(String text, Language originLanguage, List<DictionaryItem> dictionaryItems, Language translationLanguage) {
        this.text = text;
        this.originLanguage = originLanguage;
        this.dictionaryItems = dictionaryItems;
        this.translationLanguage = translationLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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

    public List<DictionaryItem> getDictionaryItems() {
        return dictionaryItems;
    }

    public void setDictionaryItems(List<DictionaryItem> dictionaryItems) {
        this.dictionaryItems = dictionaryItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word = (Word) o;
        return getText().equalsIgnoreCase(word.getText());
    }

    @Override
    public int hashCode() {
        return getText().length() * 37;
    }

    @Override
    public int compareTo(@NonNull Word word) {
        return getText().compareToIgnoreCase(word.getText());
    }
}
