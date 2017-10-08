package com.nandy.reader.parser;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

/**
 * Created by yana on 08.10.17.
 */

public class PDFParser implements Parser{

    @Override
    public CharSequence parse(String path) {
        StringBuilder builder = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(path);
            for (int page = 1; page <= reader.getNumberOfPages(); page++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
