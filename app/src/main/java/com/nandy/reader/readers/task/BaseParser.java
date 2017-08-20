package com.nandy.reader.readers.task;

import android.content.Context;
import android.os.AsyncTask;

import com.nandy.reader.emums.FileType;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.readers.EPUBReader;
import com.nandy.reader.readers.PDFReader;
import com.nandy.reader.readers.PagesParserCallback;
import com.nandy.reader.readers.TxtReader;
import com.nandy.reader.readers.XMLReader;
import com.nandy.reader.utils.ReaderPrefs;

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
