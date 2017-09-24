package com.nandy.reader.mvp.contract;

import android.view.Window;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.model.Book;
import com.nandy.reader.ui.dialog.book_settings.BookSettingsDialog;

/**
 * Created by yana on 20.08.17.
 */

public class MenuContract {

    public interface Model{

        String getPrimaryLanguage();

        String getTranslationLanguage();

        String getTitle();

        String getAuthor();

        int getBookmark();

        void setPagesCount(int pagesCount);

        int getPagesCount();

        String getBookId();

        Book getBook();

        void setBookmark(int bookmark);

    }
    public interface View extends BaseView<Presenter>{

        void setLanguages(String languages);

        void setTitle(String title);

        void setAuthor(String author);

        void setCurrentPage(String page);

        void setPagesCount(String count);

        void setItemsInDictionary(String text);

        void setItemsToLearn(String text);

        void setNoItemsInTheDictionary();

        void setCurrentPagesProgress(int progress);

        void setMaxPagesProgress(int progress);

        void setFooterPagesText(String text);

        void onNextPageOpened(int page);

        void onBookParsingFinished(int pagesCount);

        void show();

        void hide();

        void setBrightnessOnStart(int value);

        void openSettings(Book book);

        void setCurrentItem(int index);

    }

    public interface Presenter extends BasePresenter, BookSettingsDialog.Callback{

        void onMenuClick();

        void onSettingsClick();

        void onDictionaryClick();

        void onInfoClick();

        void onCloseClick();

        void onBackgroundClick();

        void onPageChanged(int page);

        void onPagesProgressChanged(int progress);

        void onBookParsingFinished(int pagesCount);

        void onBrightnessChanged(Window window, int progress);

        void afterPagesProgressChanged(int progress);

    }
}
