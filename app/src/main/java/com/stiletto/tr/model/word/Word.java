package com.stiletto.tr.model.word;

import android.content.Context;
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
    private WordInfo info;


    public Word() {
    }

    public Word(String text, String[] translations, Language[] languages) {
        this.original = text;
        this.info = new WordInfo(languages);
        this.translations = new RealmList<>(RealmString.convert(translations));
    }

    protected Word(Parcel in) {
        original = in.readString();
        dictionary = in.readParcelable(Dictionary.class.getClassLoader());
        info = in.readParcelable(WordInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original);
        dest.writeParcelable(dictionary, flags);
        dest.writeParcelable(info, flags);
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
            for (RealmString text : translations) {
                builder.append(text.value).append(", ");
            }
        } else if (dictionary.getItems() != null) {

            for (DictionaryItem item : dictionary.getItems()) {
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

    public String getOriginLanguage() {
        return info.getOriginLanguage();
    }

    public String getTranslationLanguage() {
        return info.getTranslationLanguage();
    }

    public String getStatus() {
        return info.getStatus();
    }

    public void setBookId(String bookId) {
        if (info == null) {
            info = new WordInfo();
        }
        info.setBookId(bookId);
    }

    public void setOriginLanguage(Language originLanguage) {
        if (info == null) {
            info = new WordInfo();
        }
        info.setOriginLanguage(originLanguage);
    }

    public void setTranslationLanguage(Language translationLanguage) {
        if (info == null) {
            info = new WordInfo();
        }
        info.setTranslationLanguage(translationLanguage);
    }

    public void setStatus(String status) {
        if (info == null) {
            info = new WordInfo();
        }
        info.setStatus(status);
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

    public void insert(Context context) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(this);
        realm.commitTransaction();
    }

}
