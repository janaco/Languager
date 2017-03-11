package com.stiletto.tr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yana on 11.03.17.
 */

public class UsageSample extends  Text {

    @SerializedName("tr") private Text[] translations;

    public UsageSample(String text, Text[] translations) {
        super(text);
        this.translations = translations;
    }

    public Text[] getTranslations() {
        return translations;
    }

    public void setTranslations(Text[] translations) {
        this.translations = translations;
    }
}
