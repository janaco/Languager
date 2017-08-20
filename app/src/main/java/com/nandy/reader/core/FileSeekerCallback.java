package com.nandy.reader.core;

import com.nandy.reader.model.Book;

import java.util.List;

/**
 * Created by yana on 01.03.17.
 */

public interface FileSeekerCallback {

    void onBookFound(Book book);

    void afterBookSearchResults(List<Book> books, boolean fromDB);
}
