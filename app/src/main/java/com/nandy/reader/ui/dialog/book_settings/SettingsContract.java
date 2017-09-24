package com.nandy.reader.ui.dialog.book_settings;

import android.util.Pair;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.translator.yandex.Language;

import java.util.List;

/**
 * Created by yana on 27.08.17.
 */

public class SettingsContract  {

    interface Model{

        List<String> getLanguages();

        int getOriginLanguageSelection();

        int getTranslationLanguageSelection();

        void setOriginLanguage(int selection);

        void setTranslationLanguage(int selection);

        Pair<Language, Language> getLanguagePair();
    }

    interface View extends BaseView<Presenter>{

        void setupSpinners(List<String> values, int originSelection, int translationSelection);

        void onLanguageChanged(Pair<Language, Language> languagePair);
    }

    interface Presenter extends BasePresenter{


        void onOriginLanguageSelected(int selection);

        void onTranslationLanguageSelected(int selection);
    }
}
