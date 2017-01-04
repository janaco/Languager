package com.stiletto.tr.utils;

import com.stiletto.tr.readers.PDFReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 30.12.16.
 */

public class PDFBookParser {

    public static String ZA_FARENHEITOM = "/storage/emulated/0/Download/451_za_Farenheitom.pdf";

    private List<String> splitOnPages(String text) {
        List<String> list = new ArrayList<>();

        int range = 32 * 25;
        int indexStart = 0;
        int indexEnd = range;


        while (text.length() > indexEnd) {
            list.add(text.substring(indexStart, indexEnd));
            indexStart = indexEnd;
            indexEnd += range;
        }
        list.add(text.substring(indexStart));

        return list;
    }

    public static String getBookContent(String pathToBookFile, int pageToStart, int pagesCount) {

            return PDFReader.parseAsText(pathToBookFile, pageToStart, pagesCount);
    }
}
