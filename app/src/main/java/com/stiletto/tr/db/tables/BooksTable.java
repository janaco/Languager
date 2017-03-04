package com.stiletto.tr.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stiletto.tr.db.ServiceOpenDB;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.translator.yandex.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 05.03.17.
 */

public class BooksTable extends ServiceOpenDB {


    public BooksTable(Context context) {
        super(context);
    }

    private void insert(Book book) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("path", book.getPath());
        contentValues.put("name", book.getName());
        contentValues.put("length", book.getSize());

        getWritableDatabase().insertWithOnConflict(getName(), null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private boolean update(Book book) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("path", book.getPath());
        contentValues.put("name", book.getName());
        contentValues.put("length", book.getSize());

        int count = getWritableDatabase().updateWithOnConflict(getName(), contentValues,
                "path LIKE('" + book.getPath() + "')", null, SQLiteDatabase.CONFLICT_REPLACE);

        return count > 0;
    }

    private void setTranslationLanguage(Language language, String bookPath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("lang_tr", language.toString());

        getWritableDatabase().updateWithOnConflict(getName(), contentValues, "path LIKE('" + bookPath + "')", null, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void setOriginLanguage(Language language, String bookPath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("lang_origin", language.toString());

        getWritableDatabase().updateWithOnConflict(getName(), contentValues, "path LIKE('" + bookPath + "')", null, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void setLanguages(Language[] languages, String bookPath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("lang_origin", languages[0].toString());
        contentValues.put("lang_tr", languages[1].toString());

        getWritableDatabase().updateWithOnConflict(getName(), contentValues, "path LIKE('" + bookPath + "')", null, SQLiteDatabase.CONFLICT_IGNORE);
    }
    private void setBookmark(int bookmark, String bookPath){
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookmark", bookmark);

        getWritableDatabase().updateWithOnConflict(getName(), contentValues, "path LIKE('" + bookPath + "')", null, SQLiteDatabase.CONFLICT_IGNORE);

    }

    private void setBooksList(List<Book> list) {

        for (Book book : list) {

            boolean updated = update(book);

            if (!updated) {
                insert(book);
            }
        }
    }

    private List<Book> getBooks() {

        List<Book> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT path, bookmark, lang_origin, lang_tr FROM books ORDER BY name, length", null);

        if (cursor.moveToFirst()) {
            do {

                String path = cursor.getString(cursor.getColumnIndex("path"));
                String originLanguageCode = cursor.getString(cursor.getColumnIndex("lang_origin"));
                String translationLanguageCode = cursor.getString(cursor.getColumnIndex("lang_tr"));
                int bookmark = cursor.getInt(cursor.getColumnIndex("bookmark"));

                Book book = new Book(new File(path));
                book.setBookmark(bookmark);
                if (originLanguageCode != null && !originLanguageCode.isEmpty()){
                    book.setOriginLanguage(Language.getLanguage(originLanguageCode));
                }

                if (translationLanguageCode != null && !translationLanguageCode.isEmpty()){
                    book.setTranslationLanguage(Language.getLanguage(translationLanguageCode));
                }

                list.add(book);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static void insert(Context context, Book book) {
        new BooksTable(context).insert(book);
    }

    public static void setBooksList(Context context, List<Book> books) {
        new BooksTable(context).setBooksList(books);
    }

    public static void setBookmark(Context context, int bookmark, String pathToBook){
        new BooksTable(context).setBookmark(bookmark, pathToBook);
    }

    public static void setOriginLanguage(Context context, Language language, String pathToBook){
        new BooksTable(context).setOriginLanguage(language,pathToBook);
    }

    public static void setTranslationLanguage(Context context, Language language, String pathToBook){
        new BooksTable(context).setTranslationLanguage(language, pathToBook);
    }

    public static void setLanguages(Context context, Language []languages, String pathToBook){
        new BooksTable(context).setLanguages(languages, pathToBook);
    }

    public static List<Book> getBooks(Context context) {
        return new BooksTable(context).getBooks();
    }

    public static void create(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS books(path VARCHAR(216) PRIMARY KEY, name VARCHAR(108), length LONG, bookmark INTEGER, lang_origin VARCHAR(32), lang_tr VARCHAR(32));");
    }

    private static String getName() {
        return "books";
    }
}
