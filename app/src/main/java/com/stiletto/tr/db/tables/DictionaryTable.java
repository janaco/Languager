package com.stiletto.tr.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stiletto.tr.db.ServiceOpenDB;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Translation;
import com.stiletto.tr.translator.yandex.Language;

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

        database.execSQL("CREATE TABLE IF NOT EXISTS dictionary(original TEXT, translation TEXT, transcription TEXT, pos VARCHAR(16), " +
                "lang_from VARCHAR(16), lang_to VARCHAR(16))");
    }

    public static String getName() {
        return "dictionary";
    }

    public static void insert(Context context, DictionaryItem item) {
        new DictionaryTable(context).insert(item);
    }

    public static void insert(Context context, List<DictionaryItem> list) {
        DictionaryTable table = new DictionaryTable(context);
        for (DictionaryItem item : list) {
            table.insert(item);
        }
    }

    public static List<DictionaryItem> getDictionary(Context context) {
        return new DictionaryTable(context).getDictionary();
    }

    private void insert(DictionaryItem item) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("original", item.getOriginText());
        contentValues.put("transcription", item.getTranscription());
        contentValues.put("lang_from", item.getOriginLanguage().toString());
        contentValues.put("lang_to", item.getTranslationLanguage().toString());
        contentValues.put("pos", item.getPartOfSpeech());

        for (Translation translation: item.getTranslations()){
            contentValues.remove("translation");
            contentValues.put("translation", translation.getText());

            getWritableDatabase().insert(getName(), null, contentValues);
        }

    }

    private List<DictionaryItem> getDictionary() {
        Map<String, DictionaryItem> map = new HashMap<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT original, translation, transcription, lang_from, lang_to, pos FROM dictionary ORDER BY original", null);

        if (cursor.moveToFirst()) {
            do {
                String original = cursor.getString(cursor.getColumnIndex("original"));
                String translatedText = cursor.getString(cursor.getColumnIndex("translation"));
                Translation translation = new Translation(translatedText);

                DictionaryItem item;
                String key = original.toLowerCase();
                if (map.containsKey(key)){
                    item = map.get(key);
                    item.addTranslation(translation);
                    map.put(key, item);
                    continue;
                }

                item = new DictionaryItem(original);
                item.addTranslation(translation);
                String transcription = cursor.getString(cursor.getColumnIndex("transcription"));
                String langFromCode = cursor.getString(cursor.getColumnIndex("lang_from"));
                String langToCode = cursor.getString(cursor.getColumnIndex("lang_to"));
                String pos = cursor.getString(cursor.getColumnIndex("pos"));

                item.setTranscription(transcription);
                item.setPartOfSpeech(pos);
                item.setOriginLanguage(Language.getLanguage(langFromCode));
                item.setTranslationLanguage(Language.getLanguage(langToCode));

                map.put(key, item);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return (List<DictionaryItem>) map.values();
    }

}
