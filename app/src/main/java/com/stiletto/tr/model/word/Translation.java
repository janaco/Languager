package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Represents Translation object of Yandex Dictionary Response Model.
 *
 * Created by yana on 11.03.17.
 */
 public class Translation extends RealmObject implements Parcelable {

    @SerializedName("text")
    private String text;
    @SerializedName("pos")
    private String partOfSpeech;
    @SerializedName("gen")
    private String gender;
    @SerializedName("num")
    private String number;
    @SerializedName("syn")
    private RealmList<Translation> synonyms;
    @SerializedName("mean")
    private RealmList<Text> meanings;
    @SerializedName("ex")
    private RealmList<UsageSample> usageSamples;

    public Translation(){}

    protected Translation(Parcel in) {
        text = in.readString();
        partOfSpeech = in.readString();
        gender = in.readString();
        number = in.readString();
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<Translation> getSynonyms() {
        return synonyms;
    }

    public String getSynonymsAsString(){

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Translation translation: synonyms){
            builder.append(translation.getText());

            if (index++ < synonyms.size()-1){
                builder.append(", ");
            }
        }
        return builder.toString();
    }


    public List<Text> getMeanings() {
        return meanings;
    }

    public String getMeaningsAsString(){

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Text text: meanings){
            builder.append(text.getText());

            if (index++ < meanings.size()-1){
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public void setMeanings(List<Text> meanings) {
        this.meanings = (RealmList<Text>) meanings;
    }

    public List<UsageSample> getUsageSamples() {
        return usageSamples;
    }

    public void setUsageSamples(List<UsageSample> usageSamples) {
        this.usageSamples = (RealmList<UsageSample>) usageSamples;
    }

    public boolean isKnownPartOfSpeech(){
        return partOfSpeech != null && !partOfSpeech.isEmpty();
    }

    public boolean hasMeanings(){
        return meanings != null && meanings.size() > 0;
    }

    public boolean hasSynonyms(){
        return synonyms != null && synonyms.size() > 0;
    }

    public boolean hasUsageExamples(){
        return usageSamples != null && usageSamples.size() > 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(partOfSpeech);
        dest.writeString(gender);
        dest.writeString(number);
    }
}
