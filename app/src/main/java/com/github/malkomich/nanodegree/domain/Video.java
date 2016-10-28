package com.github.malkomich.nanodegree.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model which represents an online video information.
 */
public class Video implements Parcelable {

    public static final String SITE_YOUTUBE = "YouTube";
    private static final String YOUTUBE_BASE_PATH = "https://www.youtube.com/watch?";

    private static final String ID = "id";
    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final String SITE = "site";

    @SerializedName(ID)
    @Expose
    private String id;
    @SerializedName(KEY)
    @Expose
    private final String key;
    @SerializedName(TYPE)
    @Expose
    private final String type;
    @SerializedName(SITE)
    @Expose
    private final String site;

    private Video(Parcel in) {
        key = in.readString();
        type = in.readString();
        site = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public static Uri getUri(String key) {
        return Uri.parse(YOUTUBE_BASE_PATH)
            .buildUpon()
            .appendQueryParameter("v", key)
            .build();
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public VideoType getType() {
        return VideoType.from(type);
    }

    public String getSite() {
        return site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(type);
        dest.writeString(site);
    }

    public enum VideoType {
        TRAILER("Trailer"),
        TEASER("Teaser"),
        CLIP("Clip"),
        FEATURETTE("Featurette");

        private final String name;

        VideoType(String name) {
            this.name = name;
        }

        public static VideoType from(String type) {
            switch (type) {
                case "Trailer":
                    return TRAILER;
                case "Teaser":
                    return TEASER;
                case "Clip":
                    return CLIP;
                case "Featurette":
                    return FEATURETTE;
                default:
                    return null;
            }
        }

        public String getName() {
            return name;
        }
    }
}
