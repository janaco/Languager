package com.nandy.reader.mvp.model;

import android.content.Context;
import android.util.Pair;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.realm.Realm;

/**
 * Created by yana on 02.09.17.
 */

public class TranslationsModel {

    private String bookId;
    private Pair<Language, Language> languages;

    public TranslationsModel(String bookId, Pair<Language, Language> languages){
        this.bookId = bookId;
        this.languages = languages;
    }

    public Single<Word> translate(String text) {
        return Translator.translate(text, languages);
    }

    public Single<Dictionary> requestDictionary(String text) {
       return Translator.getDictionary(text, languages);
    }

    public void saveWord(Word word) {
        word.setOriginLanguage(languages.first);
        word.setTranslationLanguage(languages.second);
        word.setBookId(bookId);
        word.insert();
    }

//    public void saveDictionary(String text, Dictionary dictionary) {
//        Word word = Realm.getDefaultInstance().where(Word.class)
//                .equalTo("original", text)
//                .equalTo("info.originLanguage", languages.first.toString())
//                .equalTo("info.translationLanguage", languages.second.toString()).findFirst();
//        word.setDictionary(dictionary);
//        word.insert();
//    }
}
