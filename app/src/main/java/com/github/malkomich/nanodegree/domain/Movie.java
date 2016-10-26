package com.github.malkomich.nanodegree.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

/**
 * Model which represents a movie.
 */
public class Movie implements Parcelable {

    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185/";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "overview";
    private static final String DATE = "release_date";
    private static final String POSTER_PATH = "poster_path";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";

    @SerializedName(ID)
    @Expose
    private int id;
    @SerializedName(TITLE)
    @Expose
    private String title;
    @SerializedName(DESCRIPTION)
    @Expose
    private String description;
    @SerializedName(DATE)
    @Expose
    private String date;
    @SerializedName(POSTER_PATH)
    @Expose
    private String posterPath;
    @SerializedName(POPULARITY)
    @Expose
    private double popularity;
    @SerializedName(VOTE_COUNT)
    @Expose
    private int voteCount;
    @SerializedName(VOTE_AVERAGE)
    @Expose
    private double voteAverage;
    @SerializedName(VIDEOS)
    @Expose
    private VideoResults videoResults;
    @SerializedName(REVIEWS)
    @Expose
    private ReviewResults reviewResults;


    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        posterPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        voteAverage = in.readDouble();
        videoResults = in.readParcelable(VideoResults.class.getClassLoader());
        reviewResults = in.readParcelable(ReviewResults.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return new LocalDate(date);
    }

    public String getPosterPath() {
        return IMAGE_BASE_PATH + posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public VideoResults getVideoResults() {
        return videoResults;
    }

    public ReviewResults getReviewResults() {
        return reviewResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeParcelable(videoResults, flags);
        dest.writeParcelable(reviewResults, flags);
    }

    public String getDateString() {
        return date;
    }
}
