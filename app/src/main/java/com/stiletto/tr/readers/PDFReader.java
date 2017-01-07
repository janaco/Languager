package com.stiletto.tr.readers;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

/**
 * Created by yana on 24.12.16.
 */

public class PDFReader {

    public static String parseAsText(String filePath) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(filePath);

            int pages = reader.getNumberOfPages();
            for (int page = 1; page <= reader.getNumberOfPages(); page++) {

                Log.d("PDF_", "page: " + page + "     of " + pages);
                stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
