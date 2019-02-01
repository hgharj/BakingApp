package com.example.android.bakingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Step implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("videoURL")
    @Expose
    private String videoUrl;

    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailUrl;

    public Step(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Step(Parcel parcel) {
        this.id = parcel.readInt();
        this.shortDescription = parcel.readString();
        this.description = parcel.readString();
        this.videoUrl = parcel.readString();
        this.thumbnailUrl= parcel.readString();

    }

    //creator - used when un-parceling our parcel (creating the object)
    public static final Creator CREATOR = new Creator() {
        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int i) {
            return new Step[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }
}
