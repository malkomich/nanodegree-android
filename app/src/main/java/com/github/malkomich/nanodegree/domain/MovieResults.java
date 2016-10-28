package com.github.malkomich.nanodegree.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model which represents a page of movies.
 */
public class MovieResults implements Parcelable {

    private static final String MOVIES = "results";
    private static final String PAGE = "page";

    @SerializedName(MOVIES)
    @Expose
    private List<Movie> movies = new ArrayList<>();
    @SerializedName(PAGE)
    @Expose
    private final int page;

    private MovieResults(Parcel in) {
        movies = in.createTypedArrayList(Movie.CREATOR);
        page = in.readInt();
    }

    public static final Creator<MovieResults> CREATOR = new Creator<MovieResults>() {
        @Override
        public MovieResults createFromParcel(Parcel in) {
            return new MovieResults(in);
        }

        @Override
        public MovieResults[] newArray(int size) {
            return new MovieResults[size];
        }
    };

    public List<Movie> getMovies() {
        return movies;
    }

    public int getPage() {
        return page;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel()
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(movies);
    }

}
