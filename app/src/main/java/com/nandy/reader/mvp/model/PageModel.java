package com.nandy.reader.mvp.model;

import android.util.Pair;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;

/**
 * Created by yana on 27.08.17.
 */

public class PageModel {

    private CharSequence content;
    private String bookId;
    private Pair<Language, Language> languages;

    public PageModel( String bookId, CharSequence content, Pair<Language, Language> languages){
        this.bookId = bookId;
        this.content = content;
        this.languages = languages;
    }

    public Pair<Language, Language> getLanguages() {
        return languages;
    }

    public String getBookId() {
        return bookId;
    }

    public String getContent() {
        return content.toString();
    }

    public void translate(String text, Translator.Callback<Word> callback) {
        new Translator().setCallback(callback).translate(text, languages);
    }

    public void requestDictionary(String text, Translator.Callback<Dictionary> callback) {
        new Translator().setCallback(callback).getDictionary(text, languages);

    }

    public void saveWord(Word word) {
        word.setOriginLanguage(languages.first);
        word.setTranslationLanguage(languages.second);
        word.setBookId(bookId);
        word.insert();
    }
}
