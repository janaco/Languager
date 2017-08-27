package com.nandy.reader.readers.task;

import android.content.Context;
import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.nandy.reader.model.Book;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.readers.PagesParserCallback;
import com.nandy.reader.utils.ReaderPrefs;

import java.io.IOException;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 21.05.17.
 */

public class PDFParser {

    public Single<Pagination> parse(Context context, Book book) {

        return Single.create((SingleOnSubscribe<Pagination>) emitter -> {
            Pagination pagination = new Pagination(ReaderPrefs.getPreferences(context));

            String filePath = book.getPath();

            try {
                PdfReader reader = new PdfReader(filePath);
                StringBuilder builder = new StringBuilder();

                for (int page = 1; page <= reader.getNumberOfPages(); page++) {
                    builder.append(PdfTextExtractor.getTextFromPage(reader, page));
                }
                pagination.splitOnPages(builder.toString());

                emitter.onSuccess(pagination);
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    }
