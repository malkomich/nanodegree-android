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
    private final int page;
    @SerializedName(REVIEWS)
    @Expose
    private List<Review> reviews = new ArrayList<>();
    @SerializedName(TOTAL_PAGES)
    @Expose
    private final int totalPages;
    @SerializedName(TOTAL_REVIEWS)
    @Expose
    private final int totalReviews;


    ReviewResults(Parcel in) {
        page = in.readInt();
        reviews = in.createTypedArrayList(Review.CREATOR);
        totalPages = in.readInt();
        totalReviews = in.readInt();
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

    public int getPage() {
        return page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel()
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(reviews);
        dest.writeInt(totalPages);
        dest.writeInt(totalReviews);
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

}
