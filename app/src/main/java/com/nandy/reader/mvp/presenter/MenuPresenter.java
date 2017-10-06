package com.nandy.reader.mvp.presenter;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;

import com.nandy.reader.emums.Status;
import com.nandy.reader.model.word.WordInfo;
import com.nandy.reader.mvp.contract.MenuContract;
import com.nandy.reader.mvp.model.MenuModel;
import com.nandy.reader.translator.yandex.Language;

import java.util.Locale;

import io.realm.Realm;

/**
 * Created by yana on 20.08.17.
 */

public class MenuPresenter implements MenuContract.Presenter{

    private MenuContract.View view;
    private MenuModel menuModel;

    public MenuPresenter(MenuContract.View view) {
        this.view = view;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    @Override
    public void start() {
        view.setLanguages(menuModel.getPrimaryLanguage() + " - " + menuModel.getTranslationLanguage());

        view.setCurrentPage(String.valueOf(menuModel.getBookmark()));
        view.setPagesCount(String.valueOf(menuModel.getPagesCount()));
        view.setCurrentPagesProgress(menuModel.getBookmark());
        view.setMaxPagesProgress(menuModel.getPagesCount());

        view.setTitle(menuModel.getTitle());
        view.setAuthor(menuModel.getAuthor());

        int itemsCount = getItemsInTheDictionaty();

        if (itemsCount > 0) {
            int unknownItems = getUnknownItemsInTheDictionaty();
            view.setItemsInDictionary(itemsCount + " hasWords in the dictionary.");
            view.setItemsToLearn(unknownItems + " hasWords to learn");
        } else {
            view.setNoItemsInTheDictionary();
        }

        view.setBrightnessOnStart(menuModel.getStartBrightness());
    }



    private int getItemsInTheDictionaty() {

        return (int) Realm.getDefaultInstance().where(WordInfo.class)
                .equalTo("bookId", menuModel.getBookId()).count();
    }

    private int getUnknownItemsInTheDictionaty() {

        return (int) Realm.getDefaultInstance().where(WordInfo.class)
                .equalTo("bookId", menuModel.getBookId())
                .equalTo("status", Status.UNKNOWN.name()).count();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onMenuClick() {
        view.show();
    }

    @Override
    public void onSettingsClick() {
        view.openSettings(menuModel.getBook());
    }

    @Override
    public void onDictionaryClick() {

    }

    @Override
    public void onInfoClick() {

    }

    @Override
    public void onCloseClick() {
        view.hide();
    }

    @Override
    public void onPageChanged(int page) {
        page += 1;
        view.setCurrentPagesProgress(page);
        view.setCurrentPage(String.valueOf(page));
        view.setFooterPagesText(page + "/" + menuModel.getPagesCount());
    }

    @Override
    public void onPagesProgressChanged(int value) {

        view.setCurrentPage(String.valueOf(value));
        menuModel.setBookmark(value);
    }

    @Override
    public void afterPagesProgressChanged(int page) {
        view.setCurrentPagesProgress(page);
        view.setCurrentPage(String.valueOf(page));
        view.setFooterPagesText(page + "/" + menuModel.getPagesCount());
        view.setCurrentItem(page);
    }

    @Override
    public void onBackgroundClick() {
        view.hide();
    }

    @Override
    public void onBookParsingFinished(int pagesCount) {
        view.setMaxPagesProgress(pagesCount);
        view.setPagesCount(String.valueOf(pagesCount));
        menuModel.setPagesCount(pagesCount);
    }

    @Override
    public void onBrightnessChanged(Window window, int progress) {
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.screenBrightness = progress / (float) 255;
        window.setAttributes(windowAttributes);
    }

    @Override
    public void onBookLanguageChanged(Pair<Language, Language> languagePair) {
        view.setLanguages(new Locale(languagePair.first.toString()).getDisplayLanguage() + " - "
                + new Locale(languagePair.second.toString()).getDisplayLanguage());
    }
}
