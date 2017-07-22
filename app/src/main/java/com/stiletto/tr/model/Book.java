package com.stiletto.tr.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.translator.yandex.Language;

import java.io.File;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Data model to represent book item.
 * <p>
 * Created by yana on 03.01.17.
 */

public class Book extends RealmObject implements Comparable<Book>, Parcelable {

    @PrimaryKey
    private String path;
    private String name;
    private String fileType;
    private long size;

    private String originLanguage;
    private String translationLanguage;

    private int bookmark;
    private int pages;

    public Book(String path, String name, long size) {
        this.path = path;
        this.name = name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name;
        this.fileType = name.contains(".") ? FileType.getType(name.substring(name.lastIndexOf(".")).toLowerCase()).name() : FileType.UNKNOWN.name();
        this.size = size;
    }

    public Book(File file) {
        this(file.getPath(), file.getName(), file.length());
    }

    public Book(){}

    protected Book(Parcel in) {
        path = in.readString();
        name = in.readString();
        size = in.readLong();
        bookmark = in.readInt();
        pages = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(size);
        dest.writeInt(bookmark);
        dest.writeInt(pages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "Book{" + "\n" +
                "path='" + path + '\'' + "\n" +
                ", name='" + name + '\'' + "\n" +
                ", fileType=" + fileType + "\n" +
                ", size=" + size + "\n" +
                '}';
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

    public void setTranslationLanguage(Language translationLanguage) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        deleteFromRealm();
        this.translationLanguage = translationLanguage.name();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public Language getOriginLanguage() {
        return Language.valueOf(originLanguage);
    }

    public void setOriginLanguage(Language originLanguage) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        deleteFromRealm();
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

    @Override
    public int compareTo(@NonNull Book book) {
        return name.compareTo(book.getName());
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

    public void rename(String name, String path) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        deleteFromRealm();
        setName(name);
        setPath(path);
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public void remove() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        deleteFromRealm();
        realm.commitTransaction();
    }

    public void setLanguages(Language languagePrimary, Language languageTranslation) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        setOriginLanguage(languagePrimary);
        setTranslationLanguage(languageTranslation);
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public void setBookmark(int bookmark, int pagesCount){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        setBookmark(bookmark);
        setPages(pagesCount);
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();

    }


    public void insert(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();

    }
}
