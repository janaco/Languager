package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.BookCoverContract;

/**
 * Created by yana on 04.10.17.
 */

public class BookCoverPresenter implements BasePresenter {

    private BookCoverContract.View view;
    private Book book;

    public BookCoverPresenter(BookCoverContract.View view) {
        this.view = view;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public void start() {
        view.setTitle(book.hasMetadata() ? book.getMetaData().getTitles() : book.getName());
        view.setExtention(book.getFileType().name());
    }

    @Override
    public void destroy() {

    }
}
