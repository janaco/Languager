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



    public static String getBookContent(String pathToBookFile) {

            return PDFReader.parseAsText(pathToBookFile);
    }
}
