package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.translator.yandex.Language;

import java.util.List;

/**
 * Data model to represent word or phrase with its translations.
 * Used with dictionary (DictionaryFragment)
 * <p>
 * Created by yana on 12.03.17.
 */

public class Word implements Comparable<Word>, Parcelable {

    private String original;
    @SerializedName("text")
    private String[] translations;
    private Dictionary dictionary;

    private String bookId;

    private Language originLanguage;
    private Language translationLanguage;

    private String status = "Unknown";

    public Word(){}

    public Word(String text,  String []translations, Language[]languages) {
        this.original = text;
        this.originLanguage = languages[0];
        this.translationLanguage = languages[1];
        this.translations = translations;
    }

    protected Word(Parcel in) {
        original = in.readString();
        translations = in.createStringArray();
        dictionary = in.readParcelable(Dictionary.class.getClassLoader());
        bookId = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original);
        dest.writeStringArray(translations);
        dest.writeParcelable(dictionary, flags);
        dest.writeString(bookId);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getTranslationsAsString() {

        StringBuilder builder = new StringBuilder();

        if (translations != null) {
            for (String text : translations) {
                builder.append(text).append(", ");
            }
        }else if (dictionary.getItems() != null){

            for (Dictionary.Item item : dictionary.getItems()){
                builder.append(item.getTranslations().get(0).getText()).append(", ");
            }
        }

        return builder.length() > 0 ? builder.delete(builder.length() - 2, builder.length()).toString()
                : builder.toString();
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String getText() {
        return original;
    }

    public void setText(String text) {
        this.original = text;
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

    public List<Dictionary.Item> getDictionaryItems() {
        return dictionary.getItems();
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
