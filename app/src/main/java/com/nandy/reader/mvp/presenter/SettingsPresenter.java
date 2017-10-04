package com.nandy.reader.mvp.presenter;

import android.util.Pair;

import com.nandy.reader.mvp.contract.SettingsContract;
import com.nandy.reader.mvp.model.SettingsModel;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.ui.dialog.BookSettingsDialog;

/**
 * Created by yana on 27.08.17.
 */

public class SettingsPresenter implements SettingsContract.Presenter, BookSettingsDialog.Callback {

    private SettingsModel settingsModel;
    private SettingsContract.View view;

    public SettingsPresenter(SettingsContract.View view) {
        this.view = view;
    }

    public void setSettingsModel(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    @Override
    public void start() {
        view.setupSpinners(settingsModel.getLanguages(), settingsModel.getOriginLanguageSelection(), settingsModel.getTranslationLanguageSelection());
    }

    @Override
    public void destroy() {

    }


    @Override
    public void onOriginLanguageSelected(int selection) {
        settingsModel.setOriginLanguage(selection);
        view.onLanguageChanged(settingsModel.getLanguagePair());
    }

    @Override
    public void onTranslationLanguageSelected(int selection) {
        settingsModel.setTranslationLanguage(selection);
        view.onLanguageChanged(settingsModel.getLanguagePair());
    }

    @Override
    public void onBookLanguageChanged(Pair<Language, Language> languagePair) {
        //TODO

    }
}
