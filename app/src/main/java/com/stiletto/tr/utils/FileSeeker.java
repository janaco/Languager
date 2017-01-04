package com.stiletto.tr.utils;

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

            Log.d("STORAGE_", "path: " + storageInfo.getPath());
            File file = new File(storageInfo.getPath());
            Log.d("STORAGE_", "isDirectory: " + file.isDirectory());

            if (file.isDirectory()) {
                textFilesSet.addAll(listDir(file));
            } else if (isTextFile(file) && file.length() > MIN_LENGTH) {
                textFilesSet.add(new Book(file.getPath(), file.getName(), file.length()));
            }

        }

        return textFilesSet;
    }


    public static List<Book> listDir(File directory) {

        List<Book> textFilesSet = new ArrayList<>();


        for (File file : directory.listFiles()) {

            if (file.isDirectory()) {
                Log.d("STORAGE_", "directory: " + directory.getPath());
                textFilesSet.addAll(listDir(file));
            } else if (isTextFile(file) && file.length() > MIN_LENGTH) {
                Log.d("STORAGE_", "file: " + file.getPath());
                textFilesSet.add(new Book(file.getPath(), file.getName(), file.length()));
            }
        }

        return textFilesSet;
    }

    private static Collection<? extends File> searchForTextFiles(File directory) {

        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return isTextFile(file);
            }
        });

        List<File> textFiles = new ArrayList<>();
        if (files != null) {
            Collections.addAll(textFiles, files);
        }
        return textFiles;
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
