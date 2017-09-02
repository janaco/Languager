package com.nandy.reader.ui.dialogs.floating_translation_dialog;

import android.content.Context;
import android.util.Pair;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;

/**
 * Created by yana on 02.09.17.
 */

public class TranslationsModel {

    private Context context;
    private String bookId;
    private Pair<Language, Language> languages;

    public TranslationsModel(Context context, String bookId, Pair<Language, Language> languages){
        this.bookId = bookId;
        this.context = context;
        this.languages = languages;
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
        word.insert(context);
    }
}
