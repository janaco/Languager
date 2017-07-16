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
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains words with its translations and other information.
 *
 * Name: dictionary.
 *
 * Fields:
 * original TEXT
 * data TEXT
 * translation TEXT
 * book TEXT
 * lang_from VARCHAR(16)
 * lang_to VARCHAR(16)
 *
 * Created by yana on 08.03.17.
 */

public class DictionaryTable extends ServiceOpenDB {

    private DictionaryTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE IF NOT EXISTS dictionary(original TEXT, data TEXT, translation TEXT, book TEXT, " + "lang_from VARCHAR(16), lang_to VARCHAR(16))");
    }

    public static String getName() {
        return "dictionary";
    }


    public static void clean(Context context){
        new DictionaryTable(context).clean();
    }

    public static List<Word> getDictionary(Context context, Language []languages){
        return new DictionaryTable(context).getDictionary(languages);
    }

    public static void insert(Context context, Word word) {
//
//        String json = new Gson().toJson(word.getDictionaryItems());
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("original", word.getText());
//        contentValues.put("translation", word.g);
//        contentValues.put("data", json);
//        contentValues.put("lang_from", item.getOriginLanguage().toString());
//        contentValues.put("lang_to", item.getTranslationLanguage().toString());
//        contentValues.put("book", item.getBookId());
//
//        Log.d("DICTIONARY_SAVE", "" + json);
//
//        new DictionaryTable(context).insert(contentValues);
    }

//    public static void remove(Context context, DictionaryItem item){
//        new DictionaryTable(context).remove(item);
//    }

//    public static Map<String, ArrayList<Word>> getDictionaries(Context context) {
//        return new DictionaryTable(context).getDictionaries();
//    }
//

    private void insert(ContentValues contentValues) {

        getDatabase().insert(getName(), null, contentValues);

    }

    private Map<String, ArrayList<Word>> getDictionaries() {
        Map<String, ArrayList<Word>> map = new HashMap<>();

        Cursor cursor = getDatabase().rawQuery("SELECT original,  data, translation, lang_from, lang_to FROM dictionary ORDER BY original", null);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex("data"));
                String origin = cursor.getString(cursor.getColumnIndex("original"));
                String langFromCode = cursor.getString(cursor.getColumnIndex("lang_from"));
                String langToCode = cursor.getString(cursor.getColumnIndex("lang_to"));
                String tranlation = cursor.getString(cursor.getColumnIndex("translation"));

//                ArrayList<DictionaryItem> items = new ArrayList<>();
//                Gson gson = new Gson();
//                JsonParser jsonParser = new JsonParser();
//                JsonArray jsonArray = (JsonArray) jsonParser.parse(data);
//
//                DictionaryItem item;
//
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    item = gson.fromJson(jsonArray.get(i), DictionaryItem.class);
//                    item.setMainTranslation(tranlation);
//                    items.add(item);
//                }
//
//                Language originLanguage = Language.getLanguage(langFromCode);
//                Language translationLanguage = Language.getLanguage(langToCode);
//                Word word = new Word(origin, originLanguage, items, translationLanguage);

//                String key = originLanguage.name();
//                ArrayList<Word> list;
//
//                if (map.containsKey(key)){
//                    list = map.get(key);
//                }else {
//                    list = new ArrayList<>();
//                }
//                list.add(word);
//                map.put(key, list);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return map;
    }

    private List<Word> getDictionary(Language []languages) {

        List<Word> list = new ArrayList<>();

        Cursor cursor = getDatabase()
                .rawQuery("SELECT original,  data FROM dictionary WHERE lang_from=? AND lang_to=?",
                        new String[]{languages[0].toString(), languages[1].toString()});

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex("data"));
                String origin = cursor.getString(cursor.getColumnIndex("original"));

                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArray = (JsonArray) jsonParser.parse(data);

//                ArrayList<DictionaryItem> items = new ArrayList<>();
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    items.add(gson.fromJson(jsonArray.get(i), DictionaryItem.class));
//                }
//
//                Word word = new Word(origin, languages[0], items, languages[1]);
//                list.add(word);


            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }
//
//    private void remove(DictionaryItem item) {
//        getDatabase().delete(getName(), "original LIKE('" + item.getOriginText() + "')", null);
//    }

    private void clean(){
        getDatabase().delete(getName(), null, null);
    }

}
