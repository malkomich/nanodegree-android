package com.github.malkomich.nanodegree.callback;

import com.github.malkomich.nanodegree.domain.Movie;

/**
 * Triggered when a movie is selected from a collection.
 */
public interface OnMovieSelectedListener {

    void onMovieSelected(Movie movie);
}
