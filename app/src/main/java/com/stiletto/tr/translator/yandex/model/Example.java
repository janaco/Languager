package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by yana on 08.01.17.
 */

public class Example {

    private
    @SerializedName("text")
    String exampleText;
    private
    @SerializedName("tr")
    Text[] translations;

    public String getExampleText() {
        return exampleText;
    }

    public String getTranslations() {
        StringBuilder builder = new StringBuilder();
        for (Text text : translations) {
            builder.append(text + ",\n");
        }

        return builder.length() > 2 ? builder.substring(0, builder.length() - 2) : builder.toString();
    }

    @Override
    public String toString() {
        return "Example{" + "\n" +
                "exampleText='" + exampleText + '\'' + "\n" +
                ", translations=" + Arrays.toString(translations) + "\n" +
                '}' + "\n";
    }
}
