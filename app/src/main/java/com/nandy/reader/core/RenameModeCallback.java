package com.nandy.reader.core;

import com.nandy.reader.model.Book;

/**
 * Created by yana on 22.04.17.
 */

public interface RenameModeCallback {

    void onRenameModeEnabled();

    void onRenameModeCanceled();

    void onBookRenamed(Book book, int position);
}
