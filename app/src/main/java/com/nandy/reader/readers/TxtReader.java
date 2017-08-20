package com.nandy.reader.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Used to parse .txt files as text.
 * <p>
 * Created by yana on 25.12.16.
 */

public class TxtReader {

    public static CharSequence parseAsText(File file) {

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

}
