package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yana on 08.01.17.
 */

 public class Text {

    private @SerializedName("text") String text;

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Text{" + "\n" +
                "text='" + text + '\'' + "\n" +
                '}' + "\n";
    }
}
