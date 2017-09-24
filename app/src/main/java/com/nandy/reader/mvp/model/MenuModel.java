package com.nandy.reader.mvp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.contract.MenuContract;

import java.util.Locale;

/**
 * Created by yana on 20.08.17.
 */

public class MenuModel {

    private Context context;
    private Book book;
    private int pagesCount;

    public MenuModel(Context context, Book book){
        this.context = context;
        this.book = book;
    }

    public String getTitle() {
        return book.getMetaData() == null ? book.getName() : book.getMetaData().getTitles() ;
    }

    public String getAuthor() {
        return book.getMetaData() == null ? book.getName() : book.getMetaData().getAuthor();
    }

    public String getPrimaryLanguage() {
        return  new Locale(book.getOriginLanguage().toString()).getDisplayLanguage();
    }

    public String getTranslationLanguage() {
        return new Locale(book.getTranslationLanguage().toString()).getDisplayLanguage();
    }

    public int getBookmark() {
        return book.getBookmark();
    }

    public Book getBook() {
        return book;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public String getBookId() {
        return book.getId();
    }

    public void setBookmark(int bookmark) {
        book.setBookmark(bookmark, book.getPages());
    }

    public int getStartBrightness(){
        try {
            ContentResolver contentResolver = context.getContentResolver();
           return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return 75;
    }
}
