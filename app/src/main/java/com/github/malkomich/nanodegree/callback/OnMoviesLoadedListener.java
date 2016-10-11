package com.github.malkomich.nanodegree.callback;

import com.github.malkomich.nanodegree.domain.MovieResults;

/**
 * Triggered when the movies data is successfully retrieved.
 */
public interface OnMoviesLoadedListener {

    void onMoviesLoaded(MovieResults results);
}
