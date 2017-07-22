package com.stiletto.tr.readers.task;

import android.content.Context;
import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PagesParserCallback;
import com.stiletto.tr.utils.ReaderPrefs;

import java.io.IOException;

/**
 * Created by yana on 21.05.17.
 */

public class PDFParser extends AsyncTask<String, Void, Pagination> {

    private Context context;

    private PagesParserCallback parserCallback;

    public PDFParser(Context context) {
        this.context = context;
    }

    public PDFParser parserCallback(PagesParserCallback parserCallback) {
        this.parserCallback = parserCallback;

        return this;
    }

    @Override
    protected Pagination doInBackground(String... args) {

        Pagination pagination = new Pagination(ReaderPrefs.getPreferences(context));

        String filePath = args[0];

        try {
            PdfReader reader = new PdfReader(filePath);
            StringBuilder builder = new StringBuilder();

            for (int page = 1; page <= reader.getNumberOfPages(); page++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
            pagination.splitOnPages(builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pagination;
    }

    @Override
    protected void onPostExecute(Pagination pagination) {
        parserCallback.onPagesParsed(pagination);
        parserCallback.afterPagesParsingFinished(pagination);
    }
}
