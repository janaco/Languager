package com.stiletto.tr.emums;

/**
 * Created by yana on 02.01.17.
 */

public enum FileType {

    PDF(".pdf"), EPUB(".epub"), TXT(".txt");

    private String extension;


    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
