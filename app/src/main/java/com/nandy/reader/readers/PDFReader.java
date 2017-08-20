package com.nandy.reader.readers;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

/**
 * Parse .pdf files as text.
 * <p>
 * Created by yana on 24.12.16.
 */

public class PDFReader {

    public static String parseAsText(String filePath) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(filePath);

            int pages = reader.getNumberOfPages();
            for (int page = 1; page <= pages; page++) {
                stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static String getPage(String filePath, String  fileName, int pageNumber) {

        try {
            PdfReader reader = new PdfReader(filePath);

            int pagesCount = reader.getNumberOfPages();

            if (pageNumber > pagesCount){
                pageNumber = pagesCount > 1 ? pagesCount - 1 : pagesCount;
            }
            return PdfTextExtractor.getTextFromPage(reader, pageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;

    }


}
