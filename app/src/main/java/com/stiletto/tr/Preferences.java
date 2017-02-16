package com.stiletto.tr;

import android.content.Context;

/**
 * Created by yana on 16.02.17.
 */

public class Preferences {

    public static final String PREF_NAME = "tr_pref";

    public static int getBookmark(Context context, String bookName) {

        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(bookName + "_page", 0);
    }

    public static void setBookmark(Context context, String bookName, int page) {

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putInt(bookName + "_page", page)
                .apply();
    }
}
