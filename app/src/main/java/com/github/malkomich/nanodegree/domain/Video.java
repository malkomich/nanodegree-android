package com.github.malkomich.nanodegree.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Model which represents an online video information.
 */
public class Video implements Parcelable {

    public static final String SITE_YOUTUBE = "YouTube";
    private static final String YOUTUBE_BASE_PATH = "https://www.youtube.com/watch?";

    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final String SITE = "site";

    private String key;
    private VideoType type;
    private String site;

    public Video(JSONObject json) {
        key = json.optString(KEY);
        type = VideoType.from(json.optString(TYPE));
        site = json.optString(SITE);
    }

    private Video(Parcel in) {
        key = in.readString();
        type = VideoType.from(in.readString());
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

    public String getKey() {
        return key;
    }

    public VideoType getType() {
        return type;
    }

    public Uri getUri() {
        return Uri.parse(YOUTUBE_BASE_PATH)
            .buildUpon()
            .appendQueryParameter("v", key)
            .build();
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
        dest.writeString(type.name);
        dest.writeString(site);
    }

    enum VideoType {
        TRAILER("Trailer"),
        TEASER("Teaser"),
        CLIP("Clip"),
        FEATURETTE("Featurette");

        private String name;

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
    }
}
