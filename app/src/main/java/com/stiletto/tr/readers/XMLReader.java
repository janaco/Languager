package com.stiletto.tr.readers;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by yana on 10.06.17.
 */

public class XMLReader {


    public static CharSequence parseAsText(File file) {
        String xml = TxtReader.parseAsText(file).toString();

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xml));
            parser.nextTag();

            return parseFBook(parser);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String parseFBook(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "FictionBook");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } else if (parser.getName().equals("body")) {
                return parseBook(parser);
            }
        }
        return null;
    }

    private static String parseBook(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "body");

        StringBuilder builder = new StringBuilder();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("title")) {
                builder.append(parseTitle(parser));
            } else if (parser.getName().equals("section")) {
                builder.append(parseSection(parser));
            }
        }

        return builder.toString();
    }

    private static String parseSection(XmlPullParser parser) throws IOException, XmlPullParserException {

        StringBuilder builder = new StringBuilder();

        parser.require(XmlPullParser.START_TAG, null, "section");

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() == XmlPullParser.END_TAG
                    && parser.getName() != null && parser.getName().equals("section")) {
                break;
            } else if (parser.getEventType() == XmlPullParser.TEXT) {
                builder.append(parser.getText());
            }
        }

        return builder.toString();
    }

    private static String parseTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        StringBuilder builder = new StringBuilder();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() == XmlPullParser.END_TAG
                    && parser.getName() != null && parser.getName().equals("title")) {
                break;
            } else if (parser.getEventType() == XmlPullParser.TEXT) {
                builder.append(parser.getText());
            }
        }
        return builder.toString();
    }


}
