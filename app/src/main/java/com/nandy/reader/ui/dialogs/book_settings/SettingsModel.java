package com.nandy.reader.ui.dialogs.book_settings;

import android.util.Pair;

import com.nandy.reader.model.Book;
import com.nandy.reader.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yana on 27.08.17.
 */

public class SettingsModel implements SettingsContract.Model {

    private Book book;
    private Language[] languages = Language.values();
    private int positionOrigin = 0;
    private int positionTranslation = 0;


    public SettingsModel(Book book) {
        this.book = book;
    }

    @Override
    public List<String> getLanguages() {


        List<String> values = new ArrayList<>();

        for (int i = 0; i < languages.length; i++) {
            Language language = languages[i];
            values.add(new Locale(language.toString()).getDisplayLanguage());

            if (language == book.getOriginLanguage()) {
                positionOrigin = i;
            }

            if (language == book.getTranslationLanguage()) {
                positionTranslation = i;
            }
        }

        return values;
    }

    @Override
    public int getOriginLanguageSelection() {
        return positionOrigin;
    }

    @Override
    public int getTranslationLanguageSelection() {
        return positionTranslation;
    }

    @Override
    public void setOriginLanguage(int selection) {
        positionOrigin = selection;
        book.setOriginLanguage(languages[selection]);
    }

    @Override
    public void setTranslationLanguage(int selection) {
        positionTranslation = selection;
        book.setTranslationLanguage(languages[selection]);
    }

    @Override
    public Pair<Language, Language> getLanguagePair() {
        return new Pair<>(book.getOriginLanguage(), book.getTranslationLanguage());
    }
}
