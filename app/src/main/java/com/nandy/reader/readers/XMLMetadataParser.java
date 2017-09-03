package com.nandy.reader.readers;

import android.util.Log;
import android.util.Xml;

import com.nandy.reader.model.MetaData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by yana on 03.09.17.
 */

public class XMLMetadataParser {


    public static MetaData getMetadata(String path) {

        String xml = TxtReader.parseAsText(new File(path)).toString();

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xml));
            parser.nextTag();

            MetaData metaData = new MetaData();
            parser.require(XmlPullParser.START_TAG, null, "FictionBook");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                } else if (parser.getName().equals("description")) {

                    parser.require(
                            XmlPullParser.START_TAG, null, "description");


                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        if (parser.getName().equals("title-info")) {
                            parseTitleInfo(parser, metaData);
                        } else if (parser.getName().equals("publish-info")) {
                            parsePublishInfo(parser, metaData);
                        }
                    }
                }

            }

return metaData;

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return new MetaData();
    }


    private static void parseAnnotation(XmlPullParser parser, MetaData metaData) throws IOException, XmlPullParserException {


        StringBuilder builder = new StringBuilder();
        parser.require(XmlPullParser.START_TAG, null, "annotation");

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() == XmlPullParser.END_TAG
                    && parser.getName() != null && parser.getName().equals("annotation")) {
                break;
            } else if (parser.getEventType() == XmlPullParser.TEXT) {
                builder.append(parser.getText());
            }
        }
        metaData.setDescription(builder.toString());
    }

    private static void parseTitleInfo(XmlPullParser parser, MetaData metaData) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title-info");

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() == XmlPullParser.END_TAG
                    && parser.getName() != null && parser.getName().equals("title-info")) {
                break;
            } else if (parser.getName() != null && parser.getName().equals("genre")) {
                metaData.setTypes(read(parser, "genre"));
            } else if (parser.getName() != null && parser.getName().equals("author")) {
                parseAuthor(parser, metaData);
            } else if (parser.getName() != null && parser.getName().equals("book-title")) {
                metaData.setTitles(read(parser, "book-title"));
            } else if (parser.getName() != null && parser.getName().equals("annotation")) {
                parseAnnotation(parser, metaData);
            } else if (parser.getName() != null && parser.getName().equals("lang")) {
                metaData.setLanguage(read(parser, "lang"));
            }
        }
    }

    private static void parsePublishInfo(XmlPullParser parser, MetaData metaData) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "publish-info");

        StringBuilder builder = new StringBuilder();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() == XmlPullParser.END_TAG
                    && parser.getName() != null && parser.getName().equals("publish-info")) {
                break;
            } else if (parser.getName() != null && parser.getName().equals("book-name")) {
                builder.append(read(parser, "book-name")).append(", ");
            } else if (parser.getName() != null && parser.getName().equals("publisher")) {
                builder.append(read(parser, "publisher")).append(", ");
            } else if (parser.getName() != null && parser.getName().equals("city")) {
                builder.append(read(parser, "city")).append(", ");
            } else if (parser.getName() != null && parser.getName().equals("year")) {
                builder.append(read(parser, "year"));
            }
        }
        metaData.setPublishers(builder.toString());
    }


    private static void parseAuthor(XmlPullParser parser, MetaData metaData) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "author");

        String firstName = "";
        String lastName = "";
        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() == XmlPullParser.END_TAG
                    && parser.getName() != null && parser.getName().equals("author")) {
                break;
            } else if (parser.getName() != null && parser.getName().equals("first-name")) {
                firstName = read(parser, "first-name");
            } else if (parser.getName() != null && parser.getName().equals("last-name")) {
                lastName = read(parser, "last-name");
            }
        }
        metaData.setAuthor(firstName.concat(" ").concat(lastName));
    }


    private static String read(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, name);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, name);
        return title;
    }

    // For the tags title and summary, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


}
