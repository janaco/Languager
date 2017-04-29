package com.stiletto.tr.db;

import android.database.sqlite.SQLiteDatabase;

import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.db.tables.DictionaryTable;

/**
 * Manage tables and all db data when it`s created or updated.
 *
 * Created by yana on 05.03.17.
 */
 class DBManager {

    static void createTables(SQLiteDatabase database) {
        BooksTable.create(database);
        DictionaryTable.create(database);
    }
}
