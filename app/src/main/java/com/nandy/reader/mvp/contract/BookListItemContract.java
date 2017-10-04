package com.nandy.reader.mvp.contract;

import android.support.annotation.StringRes;

import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 04.10.17.
 */

public class BookListItemContract {

    public interface View extends BaseView<Presenter>{

        void setLanguages(String languages);

        void setLanguageAlert(@StringRes int resId);

        void setProgress(int current, int max);

        void openBook(Book book);

    }

    public interface Presenter extends BasePresenter{

        void onReadBookClick();
    }
}
