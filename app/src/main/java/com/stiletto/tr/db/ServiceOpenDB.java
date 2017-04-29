package com.stiletto.tr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yana on 05.03.17.
 */

public abstract class ServiceOpenDB {

    private SQLiteDatabase database;
    private Context context;

    public ServiceOpenDB(Context context){
        this.context = context;
    }

    protected SQLiteDatabase getDatabase() {

        if (database == null || !database.isOpen()) {
            database = new DBHelper(context).getWritableDatabase();
        }

        return database;
    }

    public Context getContext() {
        return context;
    }
}