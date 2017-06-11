package com.stiletto.tr.readers;

import android.util.Log;
import android.util.Xml;

import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.model.Book;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by yana on 10.06.17.
 */

public class XMLReader {


    public static CharSequence parseAsText(File file) {
        String xml = TxtReader.parseAsText(file).toString();

        StringBuilder builder = new StringBuilder();

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xml));
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, null, "FictionBook");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d("FB2_", "tag name: " + name);
                // Starts by looking for the entry tag
                if (name.equals("body")) {
                    builder.append(parseBook(parser));
                }
            }


        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.d("FB2_", "ERROR");
        }

        return builder.toString();
    }

    private static String parseBook(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "body");

        StringBuilder builder = new StringBuilder();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("FB2_", "BODY: tag name: " + name);
            // Starts by looking for the entry tag
            if (name.equals("title")) {
                builder.append(parseTitle(parser));
            }
//            else if (name.equals("section")){
//
//            }
        }

        return builder.toString();
    }

    private static String parseTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
//        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    // For the tags title and summary, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        StringBuilder builder = new StringBuilder();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            Log.d("FB2_", "TEXT: eventType " + parser.getEventType() + ", " + parser.getText());

            if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName() != null &&parser.getName().equals("title")) {
                break;
            } else if (parser.getEventType() == XmlPullParser.TEXT) {
                builder.append(parser.getText());
            }
        }
        return builder.toString();
    }

    public static class FictionBook {

        @SerializedName("body")
        Body body;


        public String getContent() {
            return body.getText();
        }
    }


    /**
     *
     */
    private static class Body {

        @SerializedName("title")
        private Title title;
//        @SerializedName("section")
//        private Chapter[] chapters;

        public String getText() {

            StringBuilder builder = new StringBuilder();


            builder.append(title.getText());
            builder.append("\n\n");

//                for (Chapter chapter: chapters){
//                    builder.append(chapter.getText());
//                }


            return builder.toString();
        }
    }


    private static class Title {
        @SerializedName("p")
        P[] parts;

        public String getText() {

            StringBuilder builder = new StringBuilder();

            for (P part : parts) {
                builder.append(part.content);
            }


            return builder.toString();
        }

    }

    private static class P {

        @SerializedName("$")
        String content;
    }

    private static class Chapter {

        @SerializedName("title")
        private Title title;
        @SerializedName("section")
        private List<Page> pages;

        public String getText() {

            StringBuilder builder = new StringBuilder();
            builder.append(title.getText());
            builder.append("\n");

            for (Page page : pages) {
                builder.append(page.getText());
            }

            builder.append("\n\n");
            return builder.toString();
        }
    }


    private static class Page {
        @SerializedName("p")
        Object[] content;

        public String getText() {

            StringBuilder builder = new StringBuilder();

            for (Object text : content) {
                if (text instanceof String)
                    builder.append((String) text);
            }

            return builder.toString();
        }
    }


}
