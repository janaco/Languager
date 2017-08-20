package com.nandy.reader.utils;

/**
 * Created by yana on 02.01.17.
 */

public class StorageInfo {


    private final String path;
    private final boolean internal;
    private final boolean readOnly;
    private final int displayNumber;

    StorageInfo(String path, boolean internal, boolean readOnly, int displayNumber) {
        this.path = path;
        this.internal = internal;
        this.readOnly = readOnly;
        this.displayNumber = displayNumber;
    }

    public String getPath() {
        return path;
    }

    public boolean isInternal() {
        return internal;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public int getDisplayNumber() {
        return displayNumber;
    }
}
