package com.nandy.reader.mvp.model;

import android.content.Context;
import android.util.Pair;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;

import io.reactivex.Observable;

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

    public Observable<Word> translate(String text) {
        return Translator.translate(text, languages);
    }

    public Observable<Dictionary> requestDictionary(String text) {
       return Translator.getDictionary(text, languages);
    }

    public void saveWord(Word word) {
        word.setOriginLanguage(languages.first);
        word.setTranslationLanguage(languages.second);
        word.setBookId(bookId);
        word.insert();
    }
}
