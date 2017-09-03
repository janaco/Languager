package com.nandy.reader.ui.fragments.pager.menu;

import com.nandy.reader.model.Book;

import java.util.Locale;

/**
 * Created by yana on 20.08.17.
 */

public class MenuModel implements MenuContract.Model {

    private Book book;
    private int pagesCount;

    public MenuModel(Book book){
        this.book = book;
    }

    @Override
    public String getTitle() {
        return book.getMetaData() == null ? book.getName() : book.getMetaData().getTitles() ;
    }

    @Override
    public String getAuthor() {
        return book.getMetaData() == null ? book.getName() : book.getMetaData().getAuthor();
    }

    @Override
    public String getPrimaryLanguage() {
        return  new Locale(book.getOriginLanguage().toString()).getDisplayLanguage();
    }

    @Override
    public String getTranslationLanguage() {
        return new Locale(book.getTranslationLanguage().toString()).getDisplayLanguage();
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
    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    @Override
    public int getPagesCount() {
        return pagesCount;
    }

    @Override
    public String getBookId() {
        return book.getId();
    }

    @Override
    public void setBookmark(int bookmark) {
        book.setBookmark(bookmark, book.getPages());
    }
}
