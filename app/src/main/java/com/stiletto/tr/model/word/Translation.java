package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents Translation object of Yandex Dictionary Response Model.
 *
 * Created by yana on 11.03.17.
 */

public class Translation extends Text implements Parcelable {

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
    private List<UsageSample> usageSamples;

    public Translation(String text) {
        super(text);
    }

    protected Translation(Parcel in) {
        super(in);
        partOfSpeech = in.readString();
        gender = in.readString();
        number = in.readString();
        synonyms = in.createTypedArray(Translation.CREATOR);
        meanings = in.createTypedArray(Text.CREATOR);
        usageSamples = in.createTypedArrayList(UsageSample.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(partOfSpeech);
        dest.writeString(gender);
        dest.writeString(number);
        dest.writeTypedArray(synonyms, flags);
        dest.writeTypedArray(meanings, flags);
        dest.writeTypedList(usageSamples);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };

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

    public List<UsageSample> getUsageSamples() {
        return usageSamples;
    }

    public void setUsageSamples(List<UsageSample> usageSamples) {
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
        return usageSamples != null && usageSamples.size() > 0;
    }
}
