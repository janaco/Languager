package com.nandy.reader.readers;

import android.text.Html;
import android.text.Spanned;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * There are functions to get text content from file with .epub extension.
 *
 * Created by yana on 25.12.16.
 */

public class EPUBReader {

    public static CharSequence parseAsText(File file) {

        try {
            InputStream inputStream = new FileInputStream(file);
            Book book = (new EpubReader()).readEpub(inputStream);


            String text = readContent(book.getTableOfContents().getTocReferences(), 0);
            Spanned spanned;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
            } else {
                spanned = Html.fromHtml(text);
            }

            return spanned;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    private static String readContent(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (TOCReference tocReference : tocReferences) {
            for (int i = 0; i < depth; i++) {
                builder.append("\t");
            }
            try {
                builder.append(Reader.readInputStream(tocReference.getResource().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(readContent(tocReference.getChildren(), ++depth));
        }

        return builder.toString();
    }


}
