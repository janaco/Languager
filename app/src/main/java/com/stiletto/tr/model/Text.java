package com.stiletto.tr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yana on 11.03.17.
 */

public class Text {

    @SerializedName("text")
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
