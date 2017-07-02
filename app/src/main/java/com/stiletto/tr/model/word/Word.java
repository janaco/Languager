package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.translator.yandex.Language;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Data model to represent word or phrase with its translations.
 * Used with dictionary (DictionaryFragment)
 * <p>
 * Created by yana on 12.03.17.
 */

public class Word extends RealmObject implements Comparable<Word>, Parcelable {

    private String original;
    @SerializedName("text")
    private RealmList<RealmString> translations;
    private Dictionary dictionary;

    private String bookId;

    private String originLanguage;
    private String translationLanguage;

    private String status = "Unknown";

    public Word(){}

    public Word(String text,  String[] translations, Language[]languages) {
        this.original = text;
        this.originLanguage = languages[0].toString();
        this.translationLanguage = languages[1].toString();
        this.translations = new RealmList<>(RealmString.convert(translations));
    }

    protected Word(Parcel in) {
        original = in.readString();
        bookId = in.readString();
        originLanguage = in.readString();
        translationLanguage = in.readString();
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

    public String getTranslationsAsString() {

        StringBuilder builder = new StringBuilder();

        if (translations != null) {
            for (RealmString text : translations) {
                builder.append(text.getContent()).append(", ");
            }
        }else if (dictionary.getItems() != null){

            for (DictionaryItem item : dictionary.getItems()){
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
        return Language.getLanguage(originLanguage);
    }

    public void setOriginLanguage(Language originLanguage) {
        this.originLanguage = originLanguage.toString();
    }

    public Language getTranslationLanguage() {
        return Language.getLanguage(translationLanguage);
    }

    public void setTranslationLanguage(Language translationLanguage) {
        this.translationLanguage = translationLanguage.toString();
    }

    public List<DictionaryItem> getDictionaryItems() {
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

    public void insert(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Word word = realm.copyToRealm(this);
        realm.commitTransaction();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original);
        dest.writeString(bookId);
        dest.writeString(originLanguage);
        dest.writeString(translationLanguage);
        dest.writeString(status);
    }
}
