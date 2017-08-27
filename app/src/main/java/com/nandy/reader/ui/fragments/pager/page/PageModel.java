package com.nandy.reader.ui.fragments.pager.page;

import android.content.Context;
import android.util.Pair;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;

import java.util.List;

import rx.Observable;

/**
 * Created by yana on 27.08.17.
 */

public class PageModel implements PageContract.Model {

    private CharSequence content;
    private String bookId;
    private Pair<Language, Language> languages;

    public PageModel(String bookId, CharSequence content, Pair<Language, Language> languages){
        this.bookId = bookId;
        this.content = content;
        this.languages = languages;
    }

    @Override
    public String getContent() {
        return content.toString();
    }

    @Override
    public void translate(String text, Translator.Callback<Word> callback) {
        new Translator().setCallback(callback).translate(text, languages);
    }

    @Override
    public void requestDictionary(String text, Translator.Callback<Dictionary> callback) {
        new Translator().setCallback(callback).getDictionary(text, languages);

    }

    @Override
    public void saveWord(Context context, Word word) {
        word.setOriginLanguage(languages.first);
        word.setTranslationLanguage(languages.second);
        word.setBookId(bookId);
        word.insert(context);
    }
}
