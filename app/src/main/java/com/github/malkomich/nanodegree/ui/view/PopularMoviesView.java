package com.github.malkomich.nanodegree.ui.view;

import com.github.malkomich.nanodegree.domain.MovieResults;

/**
 * UI definition for the popular movies section.
 */
public interface PopularMoviesView {

    /**
     * Sync movie results with local persistence.
     *
     * @param movieResults Model with the collection of movies
     */
    void syncMovieResults(MovieResults movieResults);
}
