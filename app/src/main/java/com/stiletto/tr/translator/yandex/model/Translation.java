package com.stiletto.tr.translator.yandex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by yana on 08.01.17.
 */

public class Translation {

    @SerializedName("text")
    private String translatedText;

    @SerializedName("pos")
    private String translatedWordType;

    @SerializedName("syn")
    private Text[] translatedSynonims;

    @SerializedName("mean")
    private Text[] originalMeanings;

    @SerializedName("ex")
    private Example[] usageExamples;

    public String getTranslatedText() {
        return translatedText;
    }

    public String getTranslatedWordType() {
        return translatedWordType;
    }

    public String getTranslatedSynonyms() {
        if (translatedSynonims != null) {
            StringBuilder builder = new StringBuilder();
            for (Text text : translatedSynonims) {
                builder.append(text.getText() + ", ");
            }

            return builder.length() > 2 ? builder.substring(0, builder.length() - 2) : builder.toString();
        }
        return "";
    }

    public String getOriginalMeanings() {
        if (hasMeanings()) {
            StringBuilder builder = new StringBuilder();
            for (Text text : originalMeanings) {
                builder.append(text.getText() + ", ");
            }

            return builder.length() > 2 ? builder.substring(0, builder.length() - 2) : builder.toString();
        }
        return "";
    }


    public Example[] getUsageExamples() {
        return usageExamples;
    }

    public boolean hasUsages(){

        return usageExamples !=null && usageExamples.length > 0;
    }

    public boolean hasSynonyms(){

        return translatedSynonims !=null && translatedSynonims.length > 0;
    }

    public boolean hasMeanings(){

        return originalMeanings !=null && originalMeanings.length > 0;
    }



    @Override
    public String toString() {
        return "Translation{" + "\n" +
                "translatedText='" + translatedText + '\'' + "\n" +
                ", translatedWordType='" + translatedWordType + '\'' + "\n" +
                ", translatedSynonims=" + Arrays.toString(translatedSynonims) + "\n" +
                ", originalMeanings=" + Arrays.toString(originalMeanings) + "\n" +
                ", usageExamples=" + Arrays.toString(usageExamples) + "\n" +
                '}' + "\n";
    }
}
