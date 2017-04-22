package com.stiletto.tr.core;

import com.stiletto.tr.model.Book;

/**
 * Created by yana on 22.04.17.
 */

public interface RenameModeCallback {

    void onRenameModeEnabled();

    void onRenameModeCanceled();

    void onBookRenamed(Book book, int position);
}
