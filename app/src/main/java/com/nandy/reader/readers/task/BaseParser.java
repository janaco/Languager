package com.nandy.reader.readers.task;

import com.nandy.reader.emums.FileType;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.readers.EPUBReader;
import com.nandy.reader.readers.PDFReader;
import com.nandy.reader.readers.TxtReader;
import com.nandy.reader.readers.XMLReader;
import com.nandy.reader.utils.ReaderPrefs;

import java.io.File;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 21.05.17.
 */

public class BaseParser {

    public Single<Pagination> parse(ReaderPrefs readerPrefs, String path) {

        return Single.create((SingleOnSubscribe<Pagination>) e -> {
            Pagination pagination = new Pagination(getContent(path), readerPrefs);
            e.onSuccess(pagination);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static CharSequence getContent(String path) {

        File file = new File(path);

        if (path.endsWith(FileType.PDF.getExtension())) {
            return PDFReader.parseAsText(file.getPath());

        } else if (path.endsWith(FileType.EPUB.getExtension())) {
            return EPUBReader.parseAsText(file);

        } else if (path.endsWith(FileType.FB2.getExtension())) {
            return XMLReader.parseAsText(file);

        } else if (path.endsWith(FileType.TXT.getExtension())) {
            return TxtReader.parseAsText(file);
        }

        return "";
    }

}
