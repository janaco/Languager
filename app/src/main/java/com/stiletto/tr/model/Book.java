package com.stiletto.tr.model;

/**
 * Created by yana on 03.01.17.
 */

public class Book implements Comparable<Book>{

    private String path;
    private String name;
    private long size;

    public Book(String path, String name, long size) {
        this.path = path;
        this.name = name;
        this.size = size;
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
