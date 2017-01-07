package com.stiletto.tr.model;

import android.graphics.Bitmap;

import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.readers.EPUBReader;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.utils.PDFBookParser;
import com.stiletto.tr.utils.TextUtils;

/**
 * Created by yana on 03.01.17.
 */

public class Book implements Comparable<Book> {

    private String path;
    private String name;
    private FileType fileType;
    private Bitmap cover;
    private long size;
    private boolean hasCover = false;

    public Book(String path, String name, long size) {
        this.path = path;
        this.name = name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name;
        this.fileType = name.contains(".") ? FileType.getType(name.substring(name.lastIndexOf(".")).toLowerCase()) : FileType.UNKNOWN;
        this.size = size;

    }


    private void setMetaData() {

        switch (fileType) {

            case EPUB:
                cover = EPUBReader.getCover(path);
                break;

            case PDF:
                cover = TextUtils.textAsBitmap(PDFReader.getPage(path, name, 5));
                break;

        }
        hasCover = cover != null;
    }


    @Override
    public String toString() {
        return "Book{" + "\n" +
                "path='" + path + '\'' + "\n" +
                ", name='" + name + '\'' + "\n" +
                ", fileType=" + fileType.name() + "\n" +
                ", cover=" + cover + "\n" +
                ", size=" + size + "\n" +
                ", hasCover=" + hasCover + "\n" +
                '}';
    }

    public boolean hasCover() {
        return hasCover;
    }

    public FileType getFileType() {
        return fileType;
    }


    public Bitmap getCover() {
        return cover;
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

    @Override
    public int compareTo(Book book) {
        return name.compareTo(book.getName());
    }
}
