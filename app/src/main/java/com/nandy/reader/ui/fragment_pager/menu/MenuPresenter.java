package com.nandy.reader.ui.fragment_pager.menu;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.nandy.reader.emums.Status;
import com.nandy.reader.model.word.WordInfo;

import io.realm.Realm;

/**
 * Created by yana on 20.08.17.
 */

public class MenuPresenter implements MenuContract.Presenter {

    private MenuContract.View view;
    private MenuContract.Model model;

    public MenuPresenter(MenuContract.Model model, MenuContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void start(Context context) {
        view.setLanguages(model.getPrimaryLanguage() + " - " + model.getTranslationLanguage());

        view.setCurrentPage(String.valueOf(model.getBookmark()));
        view.setPagesCount(String.valueOf(model.getPagesCount()));
        view.setCurrentPagesProgress(model.getBookmark());
        view.setMaxPagesProgress(model.getPagesCount());

        view.setTitle(model.getTitle());
        view.setAuthor(model.getAuthor());

        int itemsCount = getItemsInTheDictionaty();

        if (itemsCount > 0) {
            int unknownItems = getUnknownItemsInTheDictionaty();
            view.setItemsInDictionary(itemsCount + " words in the dictionary.");
            view.setItemsToLearn(unknownItems + " words to learn");
        } else {
            view.setNoItemsInTheDictionary();
        }

        setBrightnessOnStart(context);
    }


    private void setBrightnessOnStart(Context context) {

        try {
            ContentResolver contentResolver = context.getContentResolver();
            int brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            view.setBrightnessOnStart(brightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getItemsInTheDictionaty() {

        return (int) Realm.getDefaultInstance().where(WordInfo.class)
                .equalTo("bookId", model.getBookId()).count();
    }

    private int getUnknownItemsInTheDictionaty() {

        return (int) Realm.getDefaultInstance().where(WordInfo.class)
                .equalTo("bookId", model.getBookId())
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
        view.setFooterPagesText(page + "/" + model.getPagesCount());
    }

    @Override
    public void onPagesProgressChanged(int value) {

        view.setCurrentPage(String.valueOf(value));
        model.setBookmark(value);
    }

    @Override
    public void afterPagesProgressChanged(int page) {
        view.setCurrentPagesProgress(page);
        view.setCurrentPage(String.valueOf(page));
        view.setFooterPagesText(page + "/" + model.getPagesCount());
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
        model.setPagesCount(pagesCount);
    }

    @Override
    public void onBrightnessChanged(Window window, int progress) {
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.screenBrightness = progress / (float) 255;
        window.setAttributes(windowAttributes);
    }
}
