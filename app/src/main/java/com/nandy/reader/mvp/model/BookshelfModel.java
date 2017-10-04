package com.nandy.reader.mvp.model;

import android.content.Context;

import com.nandy.reader.R;
import com.nandy.reader.adapter.BooksAdapter;
import com.nandy.reader.emums.FileType;
import com.nandy.reader.model.Book;
import com.nandy.reader.utils.StorageInfo;
import com.nandy.reader.utils.StorageUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by yana on 24.09.17.
 */

public class BookshelfModel {

    private Context context;

    public BookshelfModel(Context context) {
        this.context = context;
    }

    public Observable<Book> loadBooks() {

        return Observable.create(e -> {
            loadFromDB(e);
            loadFromFilesystem(e);
            e.onComplete();
        });
    }

    /**
     * This method will be called after search action will be performed by user.
     * It will search for the book with the entered name in the list
     * (in future in the file system) and scroll to it.
     */
    public Single<Integer> searchForBook(BooksAdapter adapter, String searchRequest) {

        return Single.create(e -> {
            int index = adapter.getPositionOf(searchRequest);
            if (index >= 0) {
                e.onSuccess(index);
            } else {
                e.onError(new Throwable(context.getString(R.string.nothing_found)));
            }

        });

    }

    private void loadFromDB(ObservableEmitter<Book> e) {
        //select and display books that are already available in db.
        //it is too long to search for them in file system
        //(search in file system will be performed but after previous search results will be displayed)
        RealmResults<Book> results = Realm.getDefaultInstance().where(Book.class)
                .findAllSortedAsync(Book.FIELD_NAME);
        results.load();

        if (results.isLoaded()) {
            for (Book book : results) {
                e.onNext(book);
            }
        }
    }

    private void loadFromFilesystem(ObservableEmitter<Book> e) {

        List<StorageInfo> storageList = StorageUtils.getStorageList();

        for (StorageInfo storageInfo : storageList) {

            File storage = new File(storageInfo.getPath());

            File[] files = storage.listFiles(getBooksFilter());

            if (files == null) {
                break;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    listDir(file, e);
                } else {
                    Book book = new Book(file);
                    book.insert();
                    e.onNext(book);
                }
            }

        }
    }


    private void listDir(File directory, ObservableEmitter<Book> emitter) {


        for (File file : directory.listFiles(getBooksFilter())) {
            if (file.isDirectory()) {
                listDir(file, emitter);
            } else {
                Book book = new Book(file);
                book.insert();
                emitter.onNext(book);
            }
        }
    }


    private FileFilter getBooksFilter() {

        return file -> isBooksFolder(file) || isBook(file);
    }

    private boolean isBook(File file) {

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

    private boolean isBooksFolder(File file) {

        String path = file.getPath().toLowerCase();

        return file.isDirectory()
                && (path.contains("book") || path.contains("download")
                || path.contains("doc") || path.contains("data"));
    }
}
