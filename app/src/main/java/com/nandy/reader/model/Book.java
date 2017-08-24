package com.nandy.reader.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nandy.reader.emums.FileType;
import com.nandy.reader.readers.EPUBReader;
import com.nandy.reader.translator.yandex.Language;

import java.io.File;
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

public class Book extends RealmObject implements Comparable<Book>, Parcelable {

    @PrimaryKey
    private String id;
    private String path;
    private String name;
    private String fileType;
    private long size;

    private String originLanguage;
    private String translationLanguage;

    private int bookmark;
    private int pages;
    private MetaData metaData;

    public Book(String path, String name, long size) {
        this.path = path;
        this.name = name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name;
        this.fileType = name.contains(".") ? FileType.getType(name.substring(name.lastIndexOf(".")).toLowerCase()).name() : FileType.UNKNOWN.name();
        this.size = size;
        this.id = new Random().nextInt(fileType.length())
                + fileType.hashCode()
                + new Random().nextInt(43 + new Random().nextInt(100))
                + fileType.substring(0, new Random().nextInt(fileType.length()))
                + size + size * 2 + 37 + new Random().nextInt(150);
        setupMetaData();
    }

    public Book(File file) {
        this(file.getPath(), file.getName(), file.length());
    }

    public Book() {
    }


    protected Book(Parcel in) {
        id = in.readString();
        path = in.readString();
        name = in.readString();
        fileType = in.readString();
        size = in.readLong();
        originLanguage = in.readString();
        translationLanguage = in.readString();
        bookmark = in.readInt();
        pages = in.readInt();
        metaData = in.readParcelable(MetaData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeString(fileType);
        dest.writeLong(size);
        dest.writeString(originLanguage);
        dest.writeString(translationLanguage);
        dest.writeInt(bookmark);
        dest.writeInt(pages);
        dest.writeParcelable(metaData, flags);
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

    private void setupMetaData(){

        switch (fileType){

            case "EPUB":
                metaData = EPUBReader.getMetadata(path);
                break;
        }
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public String getId() {
        return id;
    }

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

    public void rename(final String name, final String path) {

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                setName(name);
                setPath(path);
                realm.copyToRealmOrUpdate(Book.this);
            }
        });
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
