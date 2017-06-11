package com.stiletto.tr.readers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by yana on 10.06.17.
 */

public class XMLReader {


    public static CharSequence parseAsText(File file){
        XmlParserCreator parserCreator = new XmlParserCreator() {
            @Override
            public XmlPullParser createParser() {
                try {
                    return XmlPullParserFactory.newInstance().newPullParser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        GsonXml gsonXml = new GsonXmlBuilder()
                .setXmlParserCreator(parserCreator)
                .create();

        String xml = TxtReader.parseAsText(file).toString();
        FictionBook model = gsonXml.fromXml(xml, FictionBook.class);


        return model.getContent();

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
    private static class Body{

        @SerializedName("title")
        private Title title;
//        @SerializedName("section")
//        private Chapter[] chapters;

        public String getText(){

            StringBuilder builder = new StringBuilder();


            builder.append(title.getText());
            builder.append("\n\n");

//                for (Chapter chapter: chapters){
//                    builder.append(chapter.getText());
//                }


            return builder.toString();
        }
}


    private static class Title{
        @SerializedName("p")
        P  []parts;

        public String getText(){

            StringBuilder builder = new StringBuilder();

            for (P part:parts){
                builder.append(part.content);
            }


            return builder.toString();
        }

    }

    private static class P{

        @SerializedName("$")
        String content;
    }
    private static class Chapter {

        @SerializedName("title")
        private Title title;
        @SerializedName("section")
        private List<Page> pages;

        public String getText(){

            StringBuilder builder = new StringBuilder();
            builder.append(title.getText());
            builder.append("\n");

            for (Page page: pages){
                builder.append(page.getText());
            }

            builder.append("\n\n");
            return builder.toString();
        }
    }


    private static class Page{
        @SerializedName("p")
        Object [] content;

        public String getText(){

            StringBuilder builder = new StringBuilder();

            for (Object text: content){
                if (text instanceof String)
                builder.append((String) text);
            }

            return builder.toString();
        }
    }


}
