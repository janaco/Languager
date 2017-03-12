package com.stiletto.tr.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by yana on 12.03.17.
 */

public class Word implements Comparable<Word>, Parcelable{

    private String text;
    private String bookId;
    private Language originLanguage;
    private Language translationLanguage;
    private ArrayList<DictionaryItem> dictionaryItems;
    private String status = "Unknown";

    public Word(String text, Language originLanguage, ArrayList<DictionaryItem> dictionaryItems, Language translationLanguage) {
        this.text = text;
        this.originLanguage = originLanguage;
        this.dictionaryItems = dictionaryItems;
        this.translationLanguage = translationLanguage;
    }

    protected Word(Parcel in) {
        text = in.readString();
        bookId = in.readString();
        dictionaryItems = in.createTypedArrayList(DictionaryItem.CREATOR);
        status = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ArrayList<DictionaryItem> getDictionaryItems() {
        return dictionaryItems;
    }

    public void setDictionaryItems(ArrayList<DictionaryItem> dictionaryItems) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(bookId);
        parcel.writeTypedList(dictionaryItems);
        parcel.writeString(status);
    }
}
