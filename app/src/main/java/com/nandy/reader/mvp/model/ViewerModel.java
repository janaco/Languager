package com.nandy.reader.mvp.model;

import android.content.Context;

import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.contract.ViewerContract;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.readers.task.BaseParser;
import com.nandy.reader.utils.ReaderPrefs;

import io.reactivex.Single;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerModel {

    private Book book;
    private Pagination pagination;
    private Context context;

    public ViewerModel(Context context, Book book) {
        this.context=context;
        this.book = book;
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

    public Single<Pagination> parseBook() {
        return new BaseParser().parse(ReaderPrefs.getPreferences(context), book.getPath());

    }
}
