package com.stiletto.tr.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stiletto.tr.db.ServiceOpenDB;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.translator.PartOfSpeech;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.translator.yandex.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryTable extends ServiceOpenDB {

    public DictionaryTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase database){

        database.execSQL("CREATE TABLE IF NOT EXISTS dictionary(original TEXT PRIMARY KEY, " +
                "translation TEXT, transcription TEXT, pos VARCHAR(16), lang_from VARCHAR(16), lang_to VARCHAR(16))");
    }

    public static String getName(){
        return "dictionary";
    }

    public static void insert(Context context, DictionaryItem item){
        new DictionaryTable(context).insert(item);
    }

    public static List<DictionaryItem> getDictionary(Context context){
        return new DictionaryTable(context).getDictionary();
    }

    private void insert(DictionaryItem item){

        ContentValues contentValues = new ContentValues();
        contentValues.put("original", item.getOrigin());
        contentValues.put("translation", item.getTranslation());
        contentValues.put("transcription", item.getTranscription());
        contentValues.put("lang_from", item.getOriginLanguage().toString());
        contentValues.put("lang_to", item.getTranslationLanguage().toString());
        contentValues.put("pos", item.getPartOfSpeech().toString());

        getWritableDatabase().insert(getName(), null, contentValues);
    }

    private List<DictionaryItem> getDictionary(){
        List<DictionaryItem> list = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT original, translation, transcription, lang_from, lang_to, pos FROM dictionary ORDER BY original", null);

        if (cursor.moveToFirst()){
            do {
                String original = cursor.getString(cursor.getColumnIndex("original"));
                String translation = cursor.getString(cursor.getColumnIndex("translation"));
                String transcription = cursor.getString(cursor.getColumnIndex("transcription"));
                String langFromCode = cursor.getString(cursor.getColumnIndex("lang_from"));
                String langToCode = cursor.getString(cursor.getColumnIndex("lang_to"));
                String pos = cursor.getString(cursor.getColumnIndex("pos"));

                DictionaryItem item = new DictionaryItem(original, translation);
                item.setTranscription(transcription);
                item.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(pos));
                item.setOriginLanguage(Language.getLanguage(langFromCode));
                item.setTranslationLanguage(Language.getLanguage(langToCode));

                list.add(item);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
