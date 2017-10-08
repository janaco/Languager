package com.nandy.reader.mvp.model;

import com.nandy.reader.model.Book;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.translator.yandex.Language;

import java.util.Locale;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerModel {

    private Book book;
    private Pagination pagination;

    public ViewerModel(Book book) {
        this.book = book;
        setupLanguages();
    }

    private void setupLanguages() {
        if (!book.hasOriginLanguage()) {
            Language originLanguage = book.getMetaData() == null ? Language.ENGLISH
                    : Language.getLanguage(book.getMetaData().getLanguage());
            book.setOriginLanguage(originLanguage);
        }

        if (!book.hasTranslationLanguage()) {
            Language translationLanguage = Language.getLanguage(Locale.getDefault().getLanguage());
            book.setTranslationLanguage(translationLanguage);
        }
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }


    public String getPath() {
        return book.getPath();
    }

    public int getBookmark() {
        return book.getBookmark();
    }

    public Book getBook() {
        return book;
    }

    public void setBookmark(int bookmark) {
        book.setBookmark(bookmark, pagination.getPagesCount());
    }

    public String getTitle() {
        return null;
    }

}
