package com.nandy.reader.readers;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.utils.ReaderPrefs;

import java.io.IOException;

/**
 * Parse .pdf files as text.
 * <p>
 * Created by yana on 24.12.16.
 */

public class PDFReader {

    public static String parseAsText(String filePath) {

        StringBuilder builder = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(filePath);

            for (int page = 1; page <= reader.getNumberOfPages(); page++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
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
