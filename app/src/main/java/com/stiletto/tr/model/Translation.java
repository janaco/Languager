package com.stiletto.tr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yana on 11.03.17.
 */

public class Translation extends Text {

    @SerializedName("pos")
    private String partOfSpeech;
    @SerializedName("gen")
    private String gender;
    @SerializedName("num")
    private String number;
    @SerializedName("syn")
    private Translation[] synonyms;
    @SerializedName("mean")
    private Text[] meanings;
    @SerializedName("ex")
    private UsageSample[] usageSamples;

    public Translation(String text) {
        super(text);
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Translation[] getSynonyms() {
        return synonyms;
    }

    public String getSynonymsAsString(){

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Text text: synonyms){
            builder.append(text.getText());

            if (index++ < synonyms.length-1){
                builder.append(", ");
            }
        }
        return builder.toString();
    }


    public void setSynonyms(Translation[] synonyms) {
        this.synonyms = synonyms;
    }

    public Text[] getMeanings() {
        return meanings;
    }

    public String getMeaningsAsString(){

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Text text: meanings){
            builder.append(text.getText());

            if (index++ < meanings.length-1){
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public void setMeanings(Text[] meanings) {
        this.meanings = meanings;
    }

    public UsageSample[] getUsageSamples() {
        return usageSamples;
    }

    public void setUsageSamples(UsageSample[] usageSamples) {
        this.usageSamples = usageSamples;
    }

    public boolean isKnownPartOfSpeech(){
        return partOfSpeech != null && !partOfSpeech.isEmpty();
    }

    public boolean hasMeanings(){
        return meanings != null && meanings.length > 0;
    }

    public boolean hasSynonyms(){
        return synonyms != null && synonyms.length > 0;
    }

    public boolean hasUsageExamples(){
        return usageSamples != null && usageSamples.length > 0;
    }
}
