package com.stiletto.tr.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.stiletto.tr.db.ServiceOpenDB;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Word;
import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryTable extends ServiceOpenDB {

    public DictionaryTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE IF NOT EXISTS dictionary(original TEXT, data TEXT, book TEXT, " +
                "lang_from VARCHAR(16), lang_to VARCHAR(16))");
    }

    public static String getName() {
        return "dictionary";
    }

//    public static void insert(Context context, DictionaryItem item) {
//        new DictionaryTable(context).insert(item);
//    }

    public static void insert(Context context, List<DictionaryItem> list, DictionaryItem item) {

        String json = new Gson().toJson(list);
        ContentValues contentValues = new ContentValues();
        contentValues.put("original", item.getOriginText());
        contentValues.put("data", json);
        contentValues.put("lang_from", item.getOriginLanguage().toString());
        contentValues.put("lang_to", item.getTranslationLanguage().toString());
        contentValues.put("book", item.getBookId());

        Log.d("DICTIONARY_SAVE", "" + json);

        new DictionaryTable(context).insert(contentValues);
    }

    public static void remove(Context context, DictionaryItem item){
        new DictionaryTable(context).remove(item);
    }

    public static List<Word> getDictionary(Context context) {
        return new DictionaryTable(context).getDictionary();
    }

    private void insert(DictionaryItem item) {

        String json = item.getAsJson();
        ContentValues contentValues = new ContentValues();
        contentValues.put("original", item.getOriginText());
        contentValues.put("data", json);
        contentValues.put("lang_from", item.getOriginLanguage().toString());
        contentValues.put("lang_to", item.getTranslationLanguage().toString());
        contentValues.put("book", item.getBookId());

        Log.d("DICTIONARY_SAVE", "" + json);

        getWritableDatabase().insert(getName(), null, contentValues);

    }

    private void insert(ContentValues contentValues) {

        getWritableDatabase().insert(getName(), null, contentValues);

    }

    private List<Word> getDictionary() {
        List<Word> list = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT original,  data, lang_from, lang_to FROM dictionary ORDER BY original", null);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex("data"));
                String origin = cursor.getString(cursor.getColumnIndex("original"));
                String langFromCode = cursor.getString(cursor.getColumnIndex("lang_from"));
                String langToCode = cursor.getString(cursor.getColumnIndex("lang_to"));
                String key = origin.toLowerCase();

                ArrayList<DictionaryItem> items = new ArrayList<>();
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArray = (JsonArray) jsonParser.parse(data);

                for (int i = 0; i < jsonArray.size(); i++) {
                    items.add(gson.fromJson(jsonArray.get(i), DictionaryItem.class));
                }

                Language originLanguage = Language.getLanguage(langFromCode);
                Language translationLanguage = Language.getLanguage(langToCode);
                Word word = new Word(origin, originLanguage, items, translationLanguage);

                list.add(word);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    private void remove(DictionaryItem item) {
        getWritableDatabase().delete(getName(), "original LIKE('" + item.getOriginText() + "')", null);
    }

}
