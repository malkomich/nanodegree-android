package com.github.malkomich.nanodegree.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model which represents the list of available videos of a specific movie.
 */
public class VideoResults implements Parcelable {

    private static final String ID = "id";
    private static final String VIDEOS = "results";

    @SerializedName(ID)
    @Expose
    private int movieId;
    @SerializedName(VIDEOS)
    @Expose
    private List<Video> videos = new ArrayList<>();

    private VideoResults(Parcel in) {
        movieId = in.readInt();
        videos = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideoResults> CREATOR = new Creator<VideoResults>() {
        @Override
        public VideoResults createFromParcel(Parcel in) {
            return new VideoResults(in);
        }

        @Override
        public VideoResults[] newArray(int size) {
            return new VideoResults[size];
        }
    };

    public List<Video> getVideos() {
        return videos;
    }

    public Uri getTrailerLink() {
        for(Video video : videos) {
            if(Video.VideoType.TRAILER.equals(video.getType()) && Video.SITE_YOUTUBE.equals(video.getSite())) {
                return video.getUri();
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeTypedList(videos);
    }
}
