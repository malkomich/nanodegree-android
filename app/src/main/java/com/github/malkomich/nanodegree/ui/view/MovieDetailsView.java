package com.github.malkomich.nanodegree.ui.view;

import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.ReviewResults;
import com.github.malkomich.nanodegree.domain.VideoResults;

/**
 * Created by malkomich on 27/10/2016.
 */

public interface MovieDetailsView {

    void syncVideoResults(VideoResults videoResults);

    void syncReviewResults(ReviewResults reviewResults, long movieId);

    void syncMovieDetails(Movie movie);
}
