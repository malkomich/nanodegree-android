package com.github.malkomich.nanodegree.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model which represents the list of reviews made on a specific movie.
 */
public class ReviewResults implements Parcelable {

    private static final String PAGE = "page";
    private static final String REVIEWS = "results";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String TOTAL_REVIEWS = "total_results";

    @SerializedName(PAGE)
    @Expose
    private int page;
    @SerializedName(REVIEWS)
    @Expose
    private List<Review> reviews = new ArrayList<>();
    @SerializedName(TOTAL_PAGES)
    @Expose
    private int totalPages;
    @SerializedName(TOTAL_REVIEWS)
    @Expose
    private int totalReviews;


    protected ReviewResults(Parcel in) {
        page = in.readInt();
        reviews = in.createTypedArrayList(Review.CREATOR);
        totalPages = in.readInt();
        totalReviews = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(reviews);
        dest.writeInt(totalPages);
        dest.writeInt(totalReviews);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReviewResults> CREATOR = new Creator<ReviewResults>() {
        @Override
        public ReviewResults createFromParcel(Parcel in) {
            return new ReviewResults(in);
        }

        @Override
        public ReviewResults[] newArray(int size) {
            return new ReviewResults[size];
        }
    };
}
