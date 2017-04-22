package com.stiletto.tr.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.translator.yandex.Language;

import java.io.File;
import java.util.Objects;

/**
 * Created by yana on 03.01.17.
 */

public class Book implements Comparable<Book>, Parcelable {

    private String path;
    private String name;
    private FileType fileType;
    private long size;

    private Language originLanguage;
    private Language translationLanguage;

    private int bookmark;

    public Book(String path, String name, long size) {
        this.path = path;
        this.name = name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name;
        this.fileType = name.contains(".") ? FileType.getType(name.substring(name.lastIndexOf(".")).toLowerCase()) : FileType.UNKNOWN;
        this.size = size;
    }

    public Book(File file) {
        this(file.getPath(), file.getName(), file.length());
    }


    protected Book(Parcel in) {
        path = in.readString();
        name = in.readString();
        size = in.readLong();
        bookmark = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(size);
        dest.writeInt(bookmark);
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
                ", fileType=" + fileType.name() + "\n" +
                ", size=" + size + "\n" +
                '}';
    }

    public FileType getFileType() {
        return fileType;
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
        return translationLanguage;
    }

    public boolean hasOriginLanguage(){
        return originLanguage != null;
    }

    public boolean hasTranslationLanguage(){
        return translationLanguage != null;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTranslationLanguage(Language translationLanguage) {
        this.translationLanguage = translationLanguage;
    }

    public Language getOriginLanguage() {
        return originLanguage;
    }

    public void setOriginLanguage(Language originLanguage) {
        this.originLanguage = originLanguage;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Book book) {
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
}
