package com.github.malkomich.nanodegree.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model which represents a review made on a movie.
 */
public class Review implements Parcelable {

    private static final String ID = "id";
    private static final String AUTHOR = "author";
    private static final String TEXT = "content";
    private static final String URL = "url";

    @SerializedName(ID)
    @Expose
    private String id;
    @SerializedName(AUTHOR)
    @Expose
    private String author;
    @SerializedName(TEXT)
    @Expose
    private String text;
    @SerializedName(URL)
    @Expose
    private String url;

    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        text = in.readString();
        url = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(text);
        dest.writeString(url);
    }
}
