package com.stiletto.tr.utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.stiletto.tr.app.Constants;
import com.stiletto.tr.core.FileSeekerCallback;
import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.model.Book;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 02.01.17.
 */

public class FileSeeker {

    public static void getBooks(final FileSeekerCallback callback) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);

                Book book = message.getData().getParcelable("book");
                callback.onBookFound(book);
            }
        };

        new AsyncTask<Void, Void, List<Book>>() {
            @Override
            protected List<Book> doInBackground(Void... voids) {
                List<StorageInfo> storageList = StorageUtils.getStorageList();
                List<Book> books = new ArrayList<>();

                for (StorageInfo storageInfo : storageList) {

                    File storage = new File(storageInfo.getPath());

                    File[] files = storage.listFiles(getBooksFilter());

                    if (files == null) {
                        break;
                    }

                    for (File file : files) {

                        if (file.isDirectory()) {
                            books.addAll(listDir(file, handler));
                        } else {
                            books.add(notifyBookFound(file, handler));
                        }
                    }

                }

                return books;
            }

            @Override
            protected void onPostExecute(List<Book> books) {
                callback.afterBookSearchResults(books);
            }
        }.execute();


    }


    private static List<Book> listDir(File directory, Handler handler) {

        List<Book> books = new ArrayList<>();

        for (File file : directory.listFiles(getBooksFilter())) {
            if (file.isDirectory()) {
                books.addAll(listDir(file, handler));
            } else {
                books.add(notifyBookFound(file, handler));
            }
        }

        return books;
    }

    private static Book notifyBookFound(File file, Handler handler) {

        Book book = new Book(file.getPath(), file.getName(), file.length());
        Bundle bundle = new Bundle();
        bundle.putParcelable("book", book);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);

        return book;
    }

    private static FileFilter getBooksFilter() {

        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return isBooksFolder(file) || isBook(file);
            }
        };
    }

    private static boolean isBook(File file) {

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

    private static boolean isBooksFolder(File file) {

        String path = file.getPath().toLowerCase();

        return file.isDirectory()
                && (path.contains("book") || path.contains("download")
                || path.contains("doc") || path.contains("data"));
    }
}
