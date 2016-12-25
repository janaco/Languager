package com.stiletto.tr.readers;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

/**
 * Created by yana on 24.12.16.
 */

public class PDFReader {

    public static String parseAsText(String filePath, int pageToStart, int pagesCount) throws IOException {

        PdfReader reader = new PdfReader(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        for (int page = pageToStart; page <= pageToStart + pagesCount; page++) {

            stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, page));
        }

        return stringBuilder.toString();
    }}
