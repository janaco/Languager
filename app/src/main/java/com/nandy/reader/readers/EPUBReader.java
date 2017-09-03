package com.nandy.reader.readers;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.nandy.reader.model.MetaData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * There are functions to get text content from file with .epub extension.
 * <p>
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


    public static MetaData getMetadata(String path) {


        try {
            InputStream inputStream = new FileInputStream(new File(path));
            Book book = (new EpubReader()).readEpub(inputStream);

            Metadata data =  book.getMetadata();

            MetaData metaData = new MetaData();
            metaData.setAuthor(getNames(data.getAuthors()));
            metaData.setContributors(getNames(data.getContributors()));
            metaData.setPublishers(getValue(data.getPublishers()));
            metaData.setDescription(getValue(data.getDescriptions()));
            metaData.setTitles(getValue(data.getTitles()));
            metaData.setLanguage(data.getLanguage());
            metaData.setRights(getValue(data.getRights()));
            metaData.setSubjects(getValue(data.getSubjects()));
            metaData.setTypes(getValue(data.getTypes()));

            return metaData;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getNames(List<Author> authors){
        StringBuilder builder = new StringBuilder();
        for (Author author: authors){
            builder.append(author.getFirstname()).append(" ").append(author.getLastname()).append(", ");
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 2) : builder.toString();

    }

    private static String getValue(List<String> list){
        StringBuilder builder = new StringBuilder();
        for (String s: list){
            builder.append(s.replace(" -0", "").replace("-0", "")).append(", ");
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 2) : builder.toString();
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
