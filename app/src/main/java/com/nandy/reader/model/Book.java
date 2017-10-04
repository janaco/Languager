package com.nandy.reader.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nandy.reader.emums.FileType;
import com.nandy.reader.readers.EPUBReader;
import com.nandy.reader.readers.XMLMetadataParser;
import com.nandy.reader.translator.yandex.Language;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Data model to represent book item.
 * <p>
 * Created by yana on 03.01.17.
 */

public class Book extends RealmObject implements Comparable<Book> {

    public static final String FIELD_NAME = "name";

    @PrimaryKey
    private String id;
    private String path;
    private String name;
    private String fileType;
    private String originLanguage;
    private String translationLanguage;

    private int bookmark;
    private int pages;
    private long size;

    private MetaData metaData;

    public Book() {
    }

    public Book(File file) {
        this(file.getPath(), file.getName(), file.length());
    }

    public Book(String path, String name, long size) {
        this.path = path;
        this.name = name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name;
        this.fileType = name.contains(".") ? FileType.getType(name.substring(name.lastIndexOf(".")).toLowerCase()).name() : FileType.UNKNOWN.name();
        this.size = size;
        this.id = generateId();
        setupMetaData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return getPath().equalsIgnoreCase(book.getPath());
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(getPath());
        }
        return 45 * path.length() + name.length();
    }

    @Override
    public int compareTo(@NonNull Book book) {
        return name.compareTo(book.getName());
    }

    private String generateId() {
        return new Random().nextInt(fileType.length())
                + fileType.hashCode()
                + new Random().nextInt(43 + new Random().nextInt(100))
                + fileType.substring(0, new Random().nextInt(fileType.length()))
                + size + size * 2 + 37 + new Random().nextInt(150);
    }

    private void setupMetaData() {

        switch (fileType) {

            case "EPUB":
                metaData = EPUBReader.getMetadata(path);
                break;

            case "FB2":
                metaData = XMLMetadataParser.getMetadata(path);
                break;

        }
    }

    public boolean hasMetadata(){
        return metaData != null;
    }
    public MetaData getMetaData() {
        return metaData;
    }

    public String getId() {
        return id;
    }

    public FileType getFileType() {
        return FileType.valueOf(fileType);
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public Language getTranslationLanguage() {
        return Language.valueOf(translationLanguage);
    }

    public boolean hasOriginLanguage() {
        return originLanguage != null;
    }

    public boolean hasTranslationLanguage() {
        return translationLanguage != null;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public Language getOriginLanguage() {
        return Language.valueOf(originLanguage);
    }

    public void setOriginLanguage(Language originLanguage) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.originLanguage = originLanguage.name();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }



    public void setTranslationLanguage(Language translationLanguage) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.translationLanguage = translationLanguage.name();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }


    public void remove() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        deleteFromRealm();
        realm.commitTransaction();
    }


    public void setBookmark(int bookmark, int pagesCount) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        setBookmark(bookmark);
        setPages(pagesCount);
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();

    }


    public void insert() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();

    }
}
