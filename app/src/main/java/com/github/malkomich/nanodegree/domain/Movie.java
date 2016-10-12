package com.github.malkomich.nanodegree.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;
import org.json.JSONObject;

/**
 * Model which represents a movie.
 */
public class Movie implements Parcelable {

    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185/";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "overview";
    private static final String DATE = "release_date";
    private static final String POSTER_PATH = "poster_path";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VOTE_AVERAGE = "vote_average";

    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private String posterPath;
    private double popularity;
    private int voteCount;
    private double voteAverage;

    public Movie(JSONObject json) {
        id = json.optInt(ID);
        title = json.optString(TITLE);
        description = json.optString(DESCRIPTION);
        date = new LocalDate(json.optString(DATE));
        posterPath = json.optString(POSTER_PATH);
        popularity = json.optDouble(POPULARITY);
        voteCount = json.optInt(VOTE_COUNT);
        voteAverage = json.optDouble(VOTE_AVERAGE);
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = new LocalDate(in.readString());
        posterPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        voteAverage = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date.toString(DATE_FORMAT));
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
    }

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
        return date;
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

}
