package com.stiletto.tr.readers.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.stiletto.tr.emums.FileType;
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

public class BaseParser extends AsyncTask<String, Void, Pagination> {

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
    protected Pagination doInBackground(String... args) {

        return new Pagination(getBookContent(args[0]), ReaderPrefs.getPreferences(context));
    }

    @Override
    protected void onPostExecute(Pagination pagination) {
        pagesParserCallback.onPagesParsed(pagination);
        pagesParserCallback.afterPagesParsingFinished(pagination);
    }


    private static CharSequence getBookContent(String path) {

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
