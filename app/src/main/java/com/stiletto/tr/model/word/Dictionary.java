package com.stiletto.tr.model.word;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 02.07.17.
 */

public class Dictionary implements Parcelable{

    @SerializedName("def")
    private List<Item> items;


    protected Dictionary(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    public static final Creator<Dictionary> CREATOR = new Creator<Dictionary>() {
        @Override
        public Dictionary createFromParcel(Parcel in) {
            return new Dictionary(in);
        }

        @Override
        public Dictionary[] newArray(int size) {
            return new Dictionary[size];
        }
    };


    public static class Item implements Parcelable{
        @SerializedName("text")
        private String originText;
        @SerializedName("pos")
        private String partOfSpeech;
        @SerializedName("ts")
        private String transcription;
        @SerializedName("tr")
        private List<Translation> translations;

        public Item(String originText, List<Translation> translations) {
            this.translations = translations;
            this.originText = originText;
        }

        public Item(String originText) {
            this.originText = originText;
        }

        protected Item(Parcel in) {
            originText = in.readString();
            partOfSpeech = in.readString();
            transcription = in.readString();
            translations = in.createTypedArrayList(Translation.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(originText);
            dest.writeString(partOfSpeech);
            dest.writeString(transcription);
            dest.writeTypedList(translations);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        public String getOriginText() {
            return originText;
        }

        public void setOriginText(String originText) {
            this.originText = originText;
        }

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public void setPartOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
        }

        public String getTranscription() {
            return transcription;
        }

        public void setTranscription(String transcription) {
            this.transcription = transcription;
        }

        public List<Translation> getTranslations() {
            return translations;
        }

        public String getTranslationsAsString() {
            StringBuilder builder = new StringBuilder();
            int index = 0;

            for (Translation translation : translations) {

                builder.append(translation.getText());
                if (index++ < translations.size() - 1) {
                    builder.append(", ");
                }
            }

            return builder.toString();
        }

        public void setTranslations(List<Translation> translations) {
            this.translations = translations;
        }

        public void addTranslation(Translation translation) {
            if (translations == null) {
                setTranslations(new ArrayList<Translation>());
            }
            translations.add(translation);
        }

        public boolean isKnownPartOfSpeech() {
            return partOfSpeech != null && !partOfSpeech.isEmpty();
        }

        public boolean hasTranscription() {
            return transcription != null && !transcription.isEmpty();
        }

        public String getAsJson() {
            return new Gson().toJson(this);
        }


    }
}
