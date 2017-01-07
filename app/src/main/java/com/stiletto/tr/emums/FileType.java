package com.stiletto.tr.emums;

/**
 * Created by yana on 02.01.17.
 */

public enum FileType {

    PDF(".pdf"), EPUB(".epub"), TXT(".txt"), UNKNOWN("unknown");

    private String extension;


    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static FileType getType(String  extension){

        switch (extension.replace(" ", "")){

            case ".pdf":
                return PDF;

            case ".epub":
                return EPUB;

            case ".txt":
                return TXT;

            default:
                return UNKNOWN;
        }


    }
}
