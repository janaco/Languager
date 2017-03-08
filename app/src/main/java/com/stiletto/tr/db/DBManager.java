package com.stiletto.tr.db;

import android.database.sqlite.SQLiteDatabase;

import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.db.tables.DictionaryTable;

/**
 * Created by yana on 05.03.17.
 */

public class DBManager {

    public static void createTables(SQLiteDatabase database) {
        BooksTable.create(database);
        DictionaryTable.create(database);
    }
}
