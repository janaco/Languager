package com.nandy.reader.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by yana on 08.10.17.
 */

public class TXTParser implements Parser{

    @Override
    public CharSequence parse(String path){
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
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
