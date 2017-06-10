package com.stiletto.tr.readers;

import android.util.Log;
import android.util.Xml;

import com.google.gson.annotations.SerializedName;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
//        title - FictionBook/body
//        epigraph - FictionBook/body
//
//        section title

        @SerializedName("body")
        Body body;


        public String getContent() {
            StringBuilder builder = new StringBuilder();
            builder.append(body.title.content.text);
            builder.append("\n\n");

            for (Section section: body.section){
                builder.append(section.title.content.text);
                builder.append("\n");
                builder.append(section.content.text);
            }

            return builder.toString();
        }
    }

    private static class Body{

        @SerializedName("title")
        private Title title;
        @SerializedName("section")
        private List<Section> section;

//<body>
//  <title>
//   <p> Suzanne Collins</p>
//        <p>The Hunger Games</p>
//  </title>
//  <section>
//   <title>
//        <p>Part I</p>
//    <p>"The Tributes"</p>
//   </title>
//   <section>
//    <title>
//     <p>1</p>
//    </title>
    }


    private static class Title{
        @SerializedName("p")
        Content content;
    }
    private static class Section{

        @SerializedName("title")
        private Title title;
        @SerializedName("p")
        private Content content;
    }


    private static class Content{

        @SerializedName("p")
        String text;
    }

}
