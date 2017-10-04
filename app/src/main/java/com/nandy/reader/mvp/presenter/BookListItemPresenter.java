package com.nandy.reader.mvp.presenter;

import android.view.View;

import com.nandy.reader.R;
import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.BookListItemContract;

import java.util.Locale;

/**
 * Created by yana on 04.10.17.
 */

public class BookListItemPresenter implements BookListItemContract.Presenter {

    private BookListItemContract.View view;
    private Book book;

    public BookListItemPresenter(BookListItemContract.View view) {
        this.view = view;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public void start() {
        displayLanguages();
        displayProgress();
    }

    @Override
    public void onReadBookClick() {
        view.openBook(book);
    }

    private void displayLanguages() {
        if (book.hasOriginLanguage() && book.hasTranslationLanguage()) {
            String origin = new Locale(book.getOriginLanguage().toString()).getDisplayLanguage();
            String translation = new Locale(book.getTranslationLanguage().toString()).getDisplayLanguage();

            view.setLanguages(origin.concat(" - ").concat(translation));
        } else if (book.hasMetadata()) {
            String origin = new Locale(book.getMetaData().getLanguage()).getDisplayLanguage();
            String translation = Locale.getDefault().getDisplayLanguage();

            view.setLanguages(origin.concat(" - ").concat(translation));
        } else {
            view.setLanguageAlert(R.string.you_have_not_read_book);
        }
    }

    private void displayProgress() {
        int bookmark = book.getBookmark();
        if (bookmark > 0) {
            view.setProgress(bookmark, book.getPages());
        }
    }

    @Override
    public void destroy() {

    }
}
