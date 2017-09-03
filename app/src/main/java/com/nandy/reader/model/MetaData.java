package com.nandy.reader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

import io.realm.RealmObject;
import nl.siegmann.epublib.domain.Date;

/**
 * Created by yana on 24.08.17.
 */

public class MetaData extends RealmObject implements Parcelable{

    private String author;
    private String description;
    private String contributors;
    private String language;
    private String rights;
    private String titles;
    private String subjects;
    private String types;
    private String publishers;

    public MetaData(){}

    protected MetaData(Parcel in) {
        author = in.readString();
        description = in.readString();
        contributors = in.readString();
        language = in.readString();
        rights = in.readString();
        titles = in.readString();
        subjects = in.readString();
        types = in.readString();
        publishers = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(contributors);
        dest.writeString(language);
        dest.writeString(rights);
        dest.writeString(titles);
        dest.writeString(subjects);
        dest.writeString(types);
        dest.writeString(publishers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MetaData> CREATOR = new Creator<MetaData>() {
        @Override
        public MetaData createFromParcel(Parcel in) {
            return new MetaData(in);
        }

        @Override
        public MetaData[] newArray(int size) {
            return new MetaData[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

}
