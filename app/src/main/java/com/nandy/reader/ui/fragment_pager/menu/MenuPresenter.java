package com.nandy.reader.ui.fragment_pager.menu;

import android.support.v7.widget.LinearLayoutManager;

import com.nandy.reader.adapter.BookDictionaryAdapter;
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
    public void start() {
        view.setLanguages(model.getPrimaryLanguage() + " - " + model.getTranslationLanguage());

        view.setCurrentPage(String.valueOf(model.getBookmark()));
        view.setCurrentPagesProgress(model.getBookmark());

        view.setTitle(model.getTitle());
        view.setAuthor(model.getAuthor());

        int itemsCount = getItemsInTheDictionaty();

        if (itemsCount > 0){
            int unknownItems = getUnknownItemsInTheDictionaty();
            view.setItemsInDictionary(itemsCount + " words in the dictionary.");
            view.setItemsToLearn(unknownItems + " words to learn");
        }else {
            view.setNoItemsInTheDictionary();
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
    public void onPagesProgressChanged(int progress, boolean byUser) {
        if (byUser) {
//            viewPager.setCurrentItem(value);
//
//            String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
//            itemPages.setText(textProgress);
//            pageNumber.setText(textProgress);
//
//            book.setBookmark(viewPager.getCurrentItem(), pagination.getPagesCount());
        }
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
}
