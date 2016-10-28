package com.github.malkomich.nanodegree.ui.view;

import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.ReviewResults;
import com.github.malkomich.nanodegree.domain.VideoResults;

/**
 * UI definition for the movie details section.
 */
public interface MovieDetailsView {

    /**
     * Sync video results with local persistence.
     *
     * @param videoResults Model with the collection of videos
     */
    void syncVideoResults(VideoResults videoResults);

    /**
     * Sync review results with local persistence.
     *
     * @param reviewResults Model with the collection of reviews
     * @param movieId Unique ID of the movie
     */
    void syncReviewResults(ReviewResults reviewResults, long movieId);

    /**
     * Sync movie details with local persistence.
     *
     * @param movie Movie to update
     */
    void syncMovieDetails(Movie movie);
}
