package com.nandy.reader.ui.dialog.book_settings;

import android.content.Context;

/**
 * Created by yana on 27.08.17.
 */

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.Model model;
    private SettingsContract.View view;

    public SettingsPresenter(SettingsContract.Model model, SettingsContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void start(Context context) {
        view.setupSpinners(model.getLanguages(), model.getOriginLanguageSelection(), model.getTranslationLanguageSelection());
    }

    @Override
    public void destroy() {

    }


    @Override
    public void onOriginLanguageSelected(int selection) {
        model.setOriginLanguage(selection);
        view.onLanguageChanged(model.getLanguagePair());
    }

    @Override
    public void onTranslationLanguageSelected(int selection) {
        model.setTranslationLanguage(selection);
        view.onLanguageChanged(model.getLanguagePair());
    }
}
