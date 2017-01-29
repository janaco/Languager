package com.stiletto.tr.utils;

import android.content.Context;
import android.util.Log;

import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.model.Book;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by yana on 02.01.17.
 */

public class FileSeeker {

    private static long MIN_LENGTH = 15000;

    public static List<Book> getBooks() {
        List<Book> textFilesSet = new ArrayList<>();

        List<StorageInfo> storageList = StorageUtils.getStorageList();

        for (StorageInfo storageInfo : storageList) {

            File file = new File(storageInfo.getPath());

            if (file.isDirectory()) {
                textFilesSet.addAll(listDir( file));
            } else if (isTextFile(file) && file.length() > MIN_LENGTH) {
                textFilesSet.add(new Book( file.getPath(), file.getName(), file.length()));
            }

        }

        return textFilesSet;
    }


    private static List<Book> listDir(File directory) {

        List<Book> textFilesSet = new ArrayList<>();


        for (File file : directory.listFiles()) {

            if (file.isDirectory()) {
                textFilesSet.addAll(listDir( file));
            } else if (isTextFile(file) && file.length() > MIN_LENGTH) {
                textFilesSet.add(new Book( file.getPath(), file.getName(), file.length()));
            }
        }

        return textFilesSet;
    }


    private static boolean isTextFile(File file) {

        boolean isTextFile = false;
        String fileName = file.getName().toLowerCase();

        for (FileType fileType : FileType.values()) {

            isTextFile = fileName.endsWith(fileType.getExtension());

            if (isTextFile) {
                break;
            }
        }

        return isTextFile;
    }
}
