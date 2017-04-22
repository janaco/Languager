package com.stiletto.tr.core;

import com.stiletto.tr.model.Book;

import java.io.File;
import java.util.List;

/**
 * Created by yana on 01.03.17.
 */

public interface FileSeekerCallback {

    void onBookFound(Book book);

    void afterBookSearchResults(List<Book> books, boolean fromDB);
}
