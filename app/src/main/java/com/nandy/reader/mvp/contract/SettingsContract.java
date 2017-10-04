package com.nandy.reader.mvp.contract;

import android.util.Pair;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.translator.yandex.Language;

import java.util.List;

/**
 * Created by yana on 27.08.17.
 */

public class SettingsContract  {

    public interface View extends BaseView<Presenter>{

        void setupSpinners(List<String> values, int originSelection, int translationSelection);

        void onLanguageChanged(Pair<Language, Language> languagePair);
    }

    public interface Presenter extends BasePresenter{


        void onOriginLanguageSelected(int selection);

        void onTranslationLanguageSelected(int selection);
    }
}
