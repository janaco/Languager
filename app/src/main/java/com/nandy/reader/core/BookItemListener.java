package com.nandy.reader.core;

import com.nandy.reader.model.Book;

/**
 * Created by yana on 26.03.17.
 */

public interface BookItemListener {

    void rename(Book book, int position);

    void read(Book book);
}
