package com.stiletto.tr.readers.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.stiletto.tr.model.Book;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.EPUBReader;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.readers.PagesParserCallback;
import com.stiletto.tr.readers.TxtReader;
import com.stiletto.tr.readers.XMLReader;
import com.stiletto.tr.utils.ReaderPrefs;

import java.io.File;

/**
 * Created by yana on 21.05.17.
 */

public class BaseParser extends AsyncTask<Book, Void, Pagination> {

    private Context context;

    private PagesParserCallback pagesParserCallback;

    public BaseParser(Context context) {
        this.context = context;
    }

    public BaseParser pagesParserCallback(PagesParserCallback pagesParserCallback) {
        this.pagesParserCallback = pagesParserCallback;

        return this;
    }

    @Override
    protected Pagination doInBackground(Book... books) {

        return new Pagination(getBookContent(books[0]), ReaderPrefs.getPreferences(context));
    }

    @Override
    protected void onPostExecute(Pagination pagination) {
        pagesParserCallback.onPagesParsed(pagination);
        pagesParserCallback.afterPagesParsingFinished(pagination);
    }


    private static CharSequence getBookContent(Book book) {

        File file = new File(book.getPath());

        switch (book.getFileType()) {

            case PDF:
                return PDFReader.parseAsText(file.getPath());

            case EPUB:
                return EPUBReader.parseAsText(file);

            case FB2:
                return XMLReader.parseAsText(file);

            case TXT:
                return TxtReader.parseAsText(file);
        }


        return "";
    }

}
