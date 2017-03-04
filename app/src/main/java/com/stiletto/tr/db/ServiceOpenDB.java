package com.stiletto.tr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yana on 05.03.17.
 */

public class ServiceOpenDB {
    private SQLiteDatabase writableDatabase, readableDatabase;
    private Context context;

    public ServiceOpenDB(Context context){
        this.context = context;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        if (readableDatabase == null || !readableDatabase.isOpen()) {
            readableDatabase = new DBHelper(context).getReadableDatabase();
        }

        return readableDatabase;
    }

    public SQLiteDatabase getWritableDatabase() {

        if (writableDatabase == null || !writableDatabase.isOpen()) {
            writableDatabase = new DBHelper(context).getWritableDatabase();

        }

        return writableDatabase;
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public Context getContext() {
        return context;
    }
}