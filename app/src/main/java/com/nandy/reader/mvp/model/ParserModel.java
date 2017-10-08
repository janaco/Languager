package com.nandy.reader.mvp.model;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.nandy.reader.emums.FileType;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.parser.EPUBParser;
import com.nandy.reader.parser.PDFParser;
import com.nandy.reader.parser.Reader;
import com.nandy.reader.parser.TXTParser;
import com.nandy.reader.parser.XMLParser;
import com.nandy.reader.utils.ReaderPrefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Single;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by yana on 08.10.17.
 */

public class ParserModel {

    private ReaderPrefs prefs;
    private String path;

    public ParserModel(String path, ReaderPrefs prefs) {
        this.prefs = prefs;
        this.path = path;
    }

    public Single<Pagination> parse() {
        return Single.create(e -> {

            File file = new File(path);
            CharSequence content = null;

            if (path.endsWith(FileType.PDF.getExtension())) {
                content = new PDFParser().parse(path);

            } else if (path.endsWith(FileType.EPUB.getExtension())) {
                content = new EPUBParser().parse(path);

            } else if (path.endsWith(FileType.FB2.getExtension())) {
                content = new XMLParser().parse(path);

            } else if (path.endsWith(FileType.TXT.getExtension())) {
                content = new TXTParser().parse(path);
            }

            if (!TextUtils.isEmpty(content)) {
                e.onSuccess(new Pagination(content, prefs));
            } else {
                //TODO
                e.onError(new Throwable());
            }
        });
    }






}
