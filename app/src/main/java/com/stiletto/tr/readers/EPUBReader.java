package com.stiletto.tr.readers;

import android.text.Html;
import android.text.Spanned;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
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

    public static String readContent(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();


        for (TOCReference tocReference : tocReferences) {
            for (int i = 0; i < depth; i++) {
                builder.append("\t");
            }
//            builder.append(tocReference.getTitle());
//            tocString.append(tocReference.getTitle());
//            RowData row = new RowData();
//            row.setTitle(tocString.toString());
//            row.setResource(tocReference.getResource());
//            contentDetails.add(row);
            try {
                builder.append(Reader.readInputStream(tocReference.getResource().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(readContent(tocReference.getChildren(), ++depth));
        }

        return builder.toString();
    }

    public String readEpub(File epubFile) throws IOException {
        InputStream epubInputStream = new FileInputStream(epubFile);
        Book book = (new EpubReader()).readEpub(epubInputStream);

        InputStream is =
                book.getSpine().getSpineReferences()
                        .get(0).getResource().getInputStream();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();

        return sb.toString();
    }


}
