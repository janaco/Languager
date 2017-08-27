package com.nandy.reader.ui.fragments.pager;

import android.content.Context;

import com.nandy.reader.model.Book;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.readers.task.BaseParser;
import com.nandy.reader.utils.ReaderPrefs;

import io.reactivex.Single;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerModel implements ViewerContract.Model {

    private Book book;
    private Pagination pagination;

    ViewerModel(Book book) {
        this.book = book;
    }

    @Override
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }


    @Override
    public String getPath() {
        return book.getPath();
    }

    @Override
    public int getBookmark() {
        return book.getBookmark();
    }


    @Override
    public Book getBook() {
        return book;
    }

    @Override
    public void setBookmark(int bookmark) {
        book.setBookmark(bookmark, pagination.getPagesCount());
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Single<Pagination> parseBook(Context context) {
        return new BaseParser().parse(ReaderPrefs.getPreferences(context), book.getPath());

    }
}
